package com.pahanaedu.dao;

import com.pahanaedu.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemDAO extends CrudDAO<InvoiceItem,Integer>{
    List<InvoiceItem> findByInvoiceId(Integer invoiceId) throws Exception;
    boolean deleteByInvoiceId(Integer invoiceId) throws Exception;
}
