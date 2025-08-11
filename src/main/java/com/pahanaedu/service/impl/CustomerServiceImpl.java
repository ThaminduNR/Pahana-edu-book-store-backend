package com.pahanaedu.service.impl;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.impl.CustomerDaoImpl;
import com.pahanaedu.dto.CustomerDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.model.Customer;
import com.pahanaedu.service.CustomerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    CustomerDAO customerDAO = new CustomerDaoImpl();


    @Override
    public List<CustomerDto> findAll() throws Exception {
        List<Customer> custList = customerDAO.findAll();
        List<CustomerDto> customerDtoList = new ArrayList<CustomerDto>();
        for (Customer cust : custList) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(cust.getId());
            customerDto.setName(cust.getName());
            customerDto.setAddress(cust.getAddress());
            customerDto.setPhone(cust.getPhone());
            customerDto.setEmail(cust.getEmail());
            customerDtoList.add(customerDto);

        }
        return customerDtoList;
    }

    @Override
    public CustomerDto findById(Integer id) throws Exception {
        Customer c = customerDAO.findById(id);
        if (c == null) throw new NotFoundException("Customer not found");
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(c.getId());
        customerDto.setName(c.getName());
        customerDto.setAddress(c.getAddress());
        customerDto.setPhone(c.getPhone());
        customerDto.setEmail(c.getEmail());

        return customerDto;
    }

    @Override
    public int create(CustomerDto dto) throws Exception {

        Customer c = new Customer();
        c.setName(dto.getName());
        c.setAddress(dto.getAddress());
        c.setPhone(dto.getPhone());
        c.setEmail(dto.getEmail());

        return customerDAO.create(c);
    }

    @Override
    public boolean update(CustomerDto dto, Integer id) throws Exception {
        Customer existing = customerDAO.findById(id);
        if (existing == null) throw new NotFoundException("Customer not found");
        if (dto.getName()    != null) existing.setName(dto.getName());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getPhone()   != null) existing.setPhone(dto.getPhone());
        if (dto.getEmail()   != null) existing.setEmail(dto.getEmail());

        //validateForUpdate(existing);
        return customerDAO.update(existing);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        boolean ok = customerDAO.delete(id);
        if (!ok) throw new NotFoundException("Customer not found");
        return true;
    }
}
