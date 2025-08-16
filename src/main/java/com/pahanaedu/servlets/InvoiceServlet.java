package com.pahanaedu.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.dto.CustomerDto;
import com.pahanaedu.dto.InvoiceDto;
import com.pahanaedu.dto.ItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.service.InvoiceService;
import com.pahanaedu.service.impl.InvoiceServiceImpl;
import com.pahanaedu.util.ResponseUtil;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static java.lang.Integer.parseInt;

@WebServlet(urlPatterns = "/invoices")
public class InvoiceServlet extends HttpServlet {

    InvoiceService invoiceService = new InvoiceServiceImpl();
    Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Invoice Servlet doGet");
            List<InvoiceDto> Invoicelist = invoiceService.findAll();
            if (Invoicelist.isEmpty()) {
                System.out.println("Invoice list is empty");
            }

            ResponseUtil.send(resp, "Invoice list", 200, Invoicelist, true);
        } catch (Exception e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read raw JSON (so we can flexibly support both legacy and extended payloads)
            String json = req.getReader().lines().collect(Collectors.joining());

            // Try the extended payload first: { invoice: {..}, items: [...], taxRate: 0.15 }
            WithItemsRequest extended = null;
            try {
                extended = gson.fromJson(json, WithItemsRequest.class);
            } catch (Exception ignore) {}

            if (extended != null && extended.items != null && !extended.items.isEmpty()) {
                // Extended mode – requires InvoiceServiceImpl (for createWithItems)
                if (!(invoiceService instanceof InvoiceServiceImpl)) {
                    ResponseUtil.send(resp, "Server configuration error: createWithItems not supported", 500, null, false);
                    return;
                }
                InvoiceServiceImpl impl = (InvoiceServiceImpl) invoiceService;

                // Basic validations on extended payload
                if (extended.invoice == null) {
                    ResponseUtil.send(resp, "Request must include 'invoice' object", 400, null, false);
                    return;
                }
                if (extended.invoice.getCustomerId() <= 0) {
                    ResponseUtil.send(resp, "customerId is required", 400, null, false);
                    return;
                }
                // Build model lines
                List<InvoiceItem> lines = new ArrayList<>();
                for (ItemLine l : extended.items) {
                    if (l == null) continue;
                    if (l.itemId <= 0 || l.quantity <= 0 || l.unitPrice == null) {
                        ResponseUtil.send(resp, "Each item requires itemId, quantity (>0) and unitPrice", 400, null, false);
                        return;
                    }
                    InvoiceItem li = new InvoiceItem();
                    li.setItemId(l.itemId);
                    li.setQuantity(l.quantity);
                    li.setUnitPrice(l.unitPrice);
                    lines.add(li);
                }

                BigDecimal taxRate = extended.taxRate == null ? BigDecimal.ZERO : extended.taxRate;

                // Create invoice with items (transactional: header + lines + stock)
                int id = impl.createWithItems(extended.invoice, lines, taxRate);

                Map<String, Object> data = new LinkedHashMap<>();
                data.put("id", id);
                data.put("invoiceNo", extended.invoice.getInvoiceNo());
                data.put("subtotal", extended.invoice.getSubtotal());
                data.put("taxAmount", extended.invoice.getTaxAmount());
                data.put("discountAmt", extended.invoice.getDiscountAmt());
                data.put("totalAmount", extended.invoice.getTotalAmount());
                data.put("status", extended.invoice.getStatus());

                // Keep response style consistent with your existing code (200 + message)
                ResponseUtil.send(resp, "Invoice created with items", 200, data, true);
                return;
            }

            // ---- Legacy mode: plain InvoiceDto (header-only) ----
            InvoiceDto body = gson.fromJson(json, InvoiceDto.class);
            if (body == null || body.getInvoiceNo() == null || body.getInvoiceNo().isEmpty()) {
                ResponseUtil.send(resp, "Invoice number is required", 400, null, false);
                return;
            }
            int id = invoiceService.create(body);
            Map<String, Object> data = Collections.singletonMap("id", id);
            ResponseUtil.send(resp, "Invoice created", 200, data, true);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.send(resp, "Server error: " + e.getMessage(), 500, null, false);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = parseInt(req.getParameter("id"));
            System.out.println("ID" + id);
            if (id <= 0) {
                ResponseUtil.send(resp, "Invalid id", 400, null, false);
                return;
            }

            InvoiceDto body = gson.fromJson(req.getReader(), InvoiceDto.class);
            if (body == null) {
                ResponseUtil.send(resp, "Request body is required", 400, null, false);
                return;
            }

            boolean updated = invoiceService.update(body, id);
            ResponseUtil.send(resp, updated ? "Invoice updated" : "Update failed",
                    updated ? 200 : 400, null, updated);

        } catch (NotFoundException e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        } catch (IllegalArgumentException | ValidationException e) {
            ResponseUtil.send(resp, e.getMessage(), 400, null, false);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.send(resp, "Server error: " + e.getMessage(), 500, null, false);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            InvoiceDto byId = invoiceService.findById(id);
            if (id <= 0) {
                ResponseUtil.send(resp, "Invalid id", 400, null, false);
                return;
            }
            if (byId == null) {
                ResponseUtil.send(resp, "Invoice not found", 400, null, false);
                return;
            }
            boolean deleted = invoiceService.delete(id);
            if (deleted) {
                ResponseUtil.send(resp, "Invoice deleted", 200, null, true);
            }
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Invoice not found"))
                ResponseUtil.send(resp, e.getMessage(), 400, null, false);
            else {
                ResponseUtil.send(resp, "Server error: " + e.getMessage(), 500, null, false);
            }
        }
    }


    static class WithItemsRequest {
        InvoiceDto invoice;
        List<ItemLine> items;
        BigDecimal taxRate;
    }

    public static class ItemLine {
        int itemId;
        int quantity;
        BigDecimal unitPrice;
    }
}
