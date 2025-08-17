package com.pahanaedu.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.dto.ItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.model.Customer;
import com.pahanaedu.service.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
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

@WebServlet(urlPatterns = "/items")
public class ItemServlet extends HttpServlet {

    ItemService itemService = new ItemServiceImpl();
    Gson gson = new GsonBuilder().serializeNulls().create();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("ItemServlet doGet");
            List<ItemDto> itemList = itemService.findAll();
            if (itemList.isEmpty()) {
                System.out.println("Item list is empty");
            }

            ResponseUtil.send(resp, "Item list", 200, itemList, true);
        } catch (Exception e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ItemDto body = gson.fromJson(req.getReader(), ItemDto.class);
            if (body == null || body.getName() == null || body.getName().isEmpty()) {
                ResponseUtil.send(resp, "name is required", 400, null, false);
                return;
            }
            int id = itemService.create(body);
            Map<String, Object> data = Collections.singletonMap("id", id);
            ResponseUtil.send(resp, "Item created", 200, data, true);
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

            ItemDto body = gson.fromJson(req.getReader(), ItemDto.class);
            if (body == null) {
                ResponseUtil.send(resp, "Request body is required", 400, null, false);
                return;
            }

            boolean updated = itemService.update(body, id);
            ResponseUtil.send(resp, updated ? "Item updated" : "Update failed",
                    updated ? 200 : 400, null, updated);
        } catch (NotFoundException e) {
            ResponseUtil.send(resp, e.getMessage(), 404, null, false);

        } catch (IllegalArgumentException | ValidationException e) {
            ResponseUtil.send(resp, e.getMessage(), 400, null, false);

        } catch (Exception e) {
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
            boolean deleted = itemService.delete(id);
            if (deleted) {
                ResponseUtil.send(resp, "Item deleted", 200, null, true);
            }
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Item not found"))
                ResponseUtil.send(resp, e.getMessage(), 00, null, false);
            else {
                ResponseUtil.send(resp, "Server error: " + e.getMessage(), 500, null, false);
            }
        }
    }
}
