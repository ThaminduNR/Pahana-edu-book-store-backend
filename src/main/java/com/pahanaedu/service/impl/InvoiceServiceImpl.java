package com.pahanaedu.service.impl;

import com.pahanaedu.dao.InvoiceDAO;
import com.pahanaedu.dao.impl.InvoiceDaoImpl;
import com.pahanaedu.dto.InvoiceDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.model.Invoice;
import com.pahanaedu.service.InvoiceService;
import com.pahanaedu.util.DateConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    InvoiceDAO invoiceDAO = new InvoiceDaoImpl();
    DateConverter dc = new DateConverter();


    @Override
    public List<InvoiceDto> findAll() throws Exception {
        List<Invoice> allList = invoiceDAO.findAll();
        List<InvoiceDto> invoiceDtoList = new ArrayList<InvoiceDto>();
        for (Invoice inv : allList) {
            InvoiceDto dto = new InvoiceDto();
            dto.setId(inv.getId());
            dto.setInvoiceNo(inv.getInvoiceNo());
            dto.setCustomerId(inv.getCustomerId());
            dto.setInvoiceDate(dc.toStringLdt(inv.getInvoiceDate()));
            dto.setSubtotal(inv.getSubtotal());
            dto.setTaxAmount(inv.getTaxAmount());
            dto.setDiscountAmt(inv.getDiscountAmt());
            dto.setTotalAmount(inv.getTotalAmount());
            dto.setStatus(inv.getStatus());
            dto.setCreatedBy(inv.getCreatedBy());
            invoiceDtoList.add(dto);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto findById(Integer id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid invoice id");
        Invoice byId = invoiceDAO.findById(id);

        if (byId == null) throw new NotFoundException("Invoice not found");


        InvoiceDto dto = new InvoiceDto();
        dto.setId(byId.getId());
        dto.setInvoiceNo(byId.getInvoiceNo());
        dto.setCustomerId(byId.getCustomerId());
        dto.setInvoiceDate(dc.toStringLdt(byId.getInvoiceDate()));
        dto.setSubtotal(byId.getSubtotal());
        dto.setTaxAmount(byId.getTaxAmount());
        dto.setDiscountAmt(byId.getDiscountAmt());
        dto.setTotalAmount(byId.getTotalAmount());
        dto.setStatus(byId.getStatus());
        dto.setCreatedBy(byId.getCreatedBy());
        return dto;
    }

    @Override
    public int create(InvoiceDto dto) throws Exception {
        //validateForCreate(inv);

        // Sensible defaults
        if (dto.getInvoiceDate() == null) dto.setInvoiceDate(dc.toStringLdt(LocalDateTime.now()));
        if (dto.getSubtotal() == null) dto.setSubtotal(BigDecimal.ZERO);
        if (dto.getTaxAmount() == null) dto.setTaxAmount(BigDecimal.ZERO);
        if (dto.getDiscountAmt() == null) dto.setDiscountAmt(BigDecimal.ZERO);
        if (dto.getTotalAmount() == null) {
            dto.setTotalAmount(dto.getSubtotal()
                    .add(dto.getTaxAmount())
                    .subtract(dto.getDiscountAmt()));
        }
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) dto.setStatus("DRAFT");

        Invoice invoice = new Invoice();
        invoice.setInvoiceNo(dto.getInvoiceNo());
        invoice.setCustomerId(dto.getCustomerId());
        invoice.setInvoiceDate(dc.toLdt(dto.getInvoiceDate()));
        invoice.setSubtotal(dto.getSubtotal());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setDiscountAmt(dto.getDiscountAmt());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setStatus(dto.getStatus());
        invoice.setCreatedBy(dto.getCreatedBy());


        int id = invoiceDAO.create(invoice);
        dto.setId(id); // handy for callers
        return id;
    }

    @Override
    public boolean update(InvoiceDto dto, Integer id) throws Exception {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid invoice id");

        Invoice existing = invoiceDAO.findById(id);
        if (existing == null) throw new NotFoundException("Invoice not found");

        // Patch only provided fields (nulls are ignored)
        if (dto.getInvoiceNo() != null) existing.setInvoiceNo(dto.getInvoiceNo());
        if (dto.getCustomerId() != 0) existing.setCustomerId(dto.getCustomerId());
        if (dto.getInvoiceDate() != null) existing.setInvoiceDate(dc.toLdt(dto.getInvoiceDate()));
        if (dto.getSubtotal() != null) existing.setSubtotal(dto.getSubtotal());
        if (dto.getTaxAmount() != null) existing.setTaxAmount(dto.getTaxAmount());
        if (dto.getDiscountAmt() != null) existing.setDiscountAmt(dto.getDiscountAmt());
        if (dto.getTotalAmount() != null) existing.setTotalAmount(dto.getTotalAmount());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getCreatedBy() != null) existing.setCreatedBy(dto.getCreatedBy());

        // If total not provided, recompute from parts (when all parts available)
        if (existing.getTotalAmount() == null
                && existing.getSubtotal() != null
                && existing.getTaxAmount() != null
                && existing.getDiscountAmt() != null) {
            existing.setTotalAmount(
                    existing.getSubtotal()
                            .add(existing.getTaxAmount())
                            .subtract(existing.getDiscountAmt())
            );
        }

        // validateAmounts(existing);

        return invoiceDAO.update(existing);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid invoice id");
        Invoice byId = invoiceDAO.findById(id);
        if (byId == null) throw new NotFoundException("Invoice not found");
        return invoiceDAO.delete(id);
    }

    @Override
    public boolean updateStatus(int id, String status) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid invoice id");
        if (status == null || status.isEmpty()) throw new IllegalArgumentException("Status is required");
        return invoiceDAO.updateStatus(id, status);
    }


}
