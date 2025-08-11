package com.pahanaedu.service;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.model.Customer;

import java.util.List;

public class CustomerService {
    private final CustomerDAO dao;

    public CustomerService(CustomerDAO dao) {
        this.dao = dao;
    }
    public List<Customer> listAll() throws Exception {
        return dao.findAll();
    }

    public Customer getById(int id) throws Exception {
        Customer c = dao.findById(id);
        if (c == null) throw new NotFoundException("Customer not found");
        return c;
    }
    public int create(Customer c) throws Exception {
        //validateForCreate(c);
        return dao.create(c);
    }

    public boolean update(int id, Customer update) throws Exception {
        Customer existing = dao.findById(id);
        if (existing == null) throw new NotFoundException("Customer not found");


        if (update.getName()    != null) existing.setName(update.getName());
        if (update.getAddress() != null) existing.setAddress(update.getAddress());
        if (update.getPhone()   != null) existing.setPhone(update.getPhone());
        if (update.getEmail()   != null) existing.setEmail(update.getEmail());

        //validateForUpdate(existing);
        return dao.update(existing);
    }

    public boolean delete(int id) throws Exception {
        boolean ok = dao.delete(id);
        if (!ok) throw new NotFoundException("Customer not found");
        return true;
    }


}
