package com.pahanaedu.servlets;

import com.pahanaedu.dao.InvoiceDAO;
import com.pahanaedu.dao.impl.InvoiceDaoImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Integer.parseInt;

@WebServlet(urlPatterns ="/bill")
public class PrintBillServlet extends HttpServlet {

    InvoiceDAO invDetail = new InvoiceDaoImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int invoiceId = parseInt(req.getParameter("id"));
        try {
            Bill invoiceDetails = invDetail.getInvoiceDetails(invoiceId);
            if (invoiceDetails == null) {
                ResponseUtil.send(resp, "Invoice not found", HttpServletResponse.SC_NOT_FOUND, null, false);
                return;
            }
            ResponseUtil.send(resp, "Invoice details fetched", HttpServletResponse.SC_OK, invoiceDetails, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
