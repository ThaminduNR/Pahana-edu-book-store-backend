package com.pahanaedu.servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.model.Customer;
import com.pahanaedu.service.CustomerService;
import com.pahanaedu.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    private CustomerService service;
    private Gson gson;

    CustomerDAO customerDAO = new CustomerDAO();


    @Override
    public void init() throws ServletException {
        service = new CustomerService(new CustomerDAO());
        gson = new GsonBuilder().serializeNulls().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            System.out.println("CustomerServlet doGet");
            List<Customer> customers = service.listAll();
            if (customers.isEmpty()) {
                System.out.println("Customer list is empty");
            }


            ResponseUtil.send(resp, "Customer list", 200, customers, true);
        }catch (Exception e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        }
    }
}
