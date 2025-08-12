package com.pahanaedu.service.impl;

import com.pahanaedu.dto.InvoiceDto;
import com.pahanaedu.service.InvoiceService;

import java.util.Collections;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    @Override
    public List<InvoiceDto> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public InvoiceDto findById(Integer id) throws Exception {
        return null;
    }

    @Override
    public int create(InvoiceDto dto) throws Exception {
        return 0;
    }

    @Override
    public boolean update(InvoiceDto dto, Integer id) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return false;
    }
}
