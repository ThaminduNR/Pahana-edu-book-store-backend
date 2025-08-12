package com.pahanaedu.service;

import com.pahanaedu.dto.InvoiceItemDto;
import com.pahanaedu.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemService extends CrudService<InvoiceItemDto,Integer>{

    List<InvoiceItemDto> findByInvoiceId(Integer invoiceId) throws Exception;
    boolean deleteByInvoiceId(Integer invoiceId) throws Exception;
}
