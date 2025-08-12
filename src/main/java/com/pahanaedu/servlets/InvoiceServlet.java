package com.pahanaedu.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.dto.CustomerDto;
import com.pahanaedu.dto.InvoiceDto;
import com.pahanaedu.dto.ItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.service.InvoiceService;
import com.pahanaedu.service.impl.InvoiceServiceImpl;
import com.pahanaedu.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            InvoiceDto body = gson.fromJson(req.getReader(), InvoiceDto.class);
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
}
