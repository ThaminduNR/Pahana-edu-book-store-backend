package com.pahanaedu.service.impl;

import com.pahanaedu.dao.InvoiceItemDAO;
import com.pahanaedu.dao.impl.InvoiceItemDaoImpl;
import com.pahanaedu.dto.InvoiceItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.service.InvoiceItemService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvoiceItemServiceImpl implements InvoiceItemService {

    InvoiceItemDAO invItm = new InvoiceItemDaoImpl();


    @Override
    public List<InvoiceItemDto> findAll() throws Exception {
        List<InvoiceItem> items = invItm.findAll();
        if (items == null || items.isEmpty()) return Collections.emptyList();
        List<InvoiceItemDto> out = new ArrayList<>(items.size());
        for (InvoiceItem it : items) out.add(toDto(it));
        return out;
    }

    @Override
    public InvoiceItemDto findById(Integer id) throws Exception {
        if (id == null || id <= 0) throw new ValidationException("Invalid id");
        InvoiceItem byId = invItm.findById(id);
        if (byId == null) throw new NotFoundException("Id not found");
        InvoiceItem m = invItm.findById(id);
        return (m == null) ? null : toDto(m);
    }

    @Override
    public int create(InvoiceItemDto invItem) throws Exception {
        InvoiceItem m = fromDto(invItem);
        //m.setId(0);
        return invItm.create(m);
    }

    @Override
    public boolean update(InvoiceItemDto dto, Integer id) throws Exception {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid id");
        if (dto == null) throw new IllegalArgumentException("Body is required");

        // load existing
        InvoiceItem existing = invItm.findById(id);
        if (existing == null) throw new NotFoundException("Invoice item not found");


        if (dto.getInvoiceId() != null) existing.setInvoiceId(dto.getInvoiceId());
        if (dto.getItemId()    != null) existing.setItemId(dto.getItemId());
        if (dto.getQuantity()  != null) existing.setQuantity(dto.getQuantity());
        if (dto.getUnitPrice() != null) existing.setUnitPrice(dto.getUnitPrice());

        //validateUpdate(existing);
        return invItm.update(existing);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid id");
        InvoiceItem byId = invItm.findById(id);
        if (byId == null) throw new NotFoundException("Id not found");
        return invItm.delete(id);
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
