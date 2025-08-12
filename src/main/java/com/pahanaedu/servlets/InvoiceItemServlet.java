package com.pahanaedu.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.dto.CustomerDto;
import com.pahanaedu.dto.InvoiceItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.service.InvoiceItemService;
import com.pahanaedu.service.InvoiceService;
import com.pahanaedu.service.impl.InvoiceItemServiceImpl;
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

@WebServlet(urlPatterns = "/invoiceIem")
public class InvoiceItemServlet extends HttpServlet {

    InvoiceItemService invoiceItemService = new InvoiceItemServiceImpl();
    Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Invoice Item Servlet doGet");
            List<InvoiceItemDto> list = invoiceItemService.findAll();

            if (list.isEmpty()) {
                System.out.println("Invoice Item list is empty");
            }

            ResponseUtil.send(resp, "Invoice Item list", 200, list, true);
        } catch (Exception e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            InvoiceItemDto body = gson.fromJson(req.getReader(), InvoiceItemDto.class);
            if (body == null) {
                ResponseUtil.send(resp, "Body is required", 400, null, false);
                return;
            }
            int id = invoiceItemService.create(body);
            Map<String, Object> data = Collections.singletonMap("id", id);
            ResponseUtil.send(resp, "Invoice Item created", 200, data, true);
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
                ResponseUtil.send(resp, "Invalid Invoice Item id", 400, null, false);
                return;
            }

            InvoiceItemDto body = gson.fromJson(req.getReader(), InvoiceItemDto.class);
            if (body == null) {
                ResponseUtil.send(resp, "Request body is required", 400, null, false);
                return;
            }

            boolean updated = invoiceItemService.update(body, id);
            ResponseUtil.send(resp, updated ? "Invoice Item updated" : "Update failed",
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
            if (id <= 0) {
                ResponseUtil.send(resp, "Invalid id", 400, null, false);
                return;
            }
            boolean deleted = invoiceItemService.delete(id);
            if (deleted) {
                ResponseUtil.send(resp, "Invoice Item deleted", 200, null, true);
            }
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Invoice Item not found"))
                ResponseUtil.send(resp, e.getMessage(), 400, null, false);
            else {
                ResponseUtil.send(resp, "Server error: " + e.getMessage(), 500, null, false);
            }
        }
    }

}
