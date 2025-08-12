package com.pahanaedu.service.impl;

import com.pahanaedu.dto.InvoiceItemDto;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.service.InvoiceItemService;

import java.util.Collections;
import java.util.List;

public class InvoiceItemServiceImpl implements InvoiceItemService {
    @Override
    public List<InvoiceItemDto> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public InvoiceItemDto findById(Integer integer) throws Exception {
        return null;
    }

    @Override
    public int create(InvoiceItemDto entity) throws Exception {
        return 0;
    }

    @Override
    public boolean update(InvoiceItemDto entity, Integer integer) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        return false;
    }

    private InvoiceItemDto toDto(InvoiceItem m) {
        InvoiceItemDto d = new InvoiceItemDto();
        d.setId(m.getId());
        d.setInvoiceId(m.getInvoiceId());
        d.setItemId(m.getItemId());
        d.setQuantity(m.getQuantity());
        d.setUnitPrice(m.getUnitPrice());
        return d;
    }

    private InvoiceItem fromDto(InvoiceItemDto d) {
        InvoiceItem m = new InvoiceItem();

        if (d.getId() != null) m.setId(d.getId());
        if (d.getInvoiceId() != null) m.setInvoiceId(d.getInvoiceId());
        if (d.getItemId() != null)    m.setItemId(d.getItemId());
        if (d.getQuantity() != null)  m.setQuantity(d.getQuantity());
        if (d.getUnitPrice() != null) m.setUnitPrice(d.getUnitPrice());
        return m;
    }
}
