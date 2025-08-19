package com.pahanaedu.servlets;

import com.pahanaedu.service.InvoiceService;
import com.pahanaedu.service.impl.InvoiceServiceImpl;
import com.pahanaedu.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/util")
public class UtilServlet extends HttpServlet {

    InvoiceService invoiceService = new InvoiceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int lastInvoiceId = invoiceService.getLastInvoiceId();
            ResponseUtil.send(resp, "last Invoice", 200, lastInvoiceId, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
