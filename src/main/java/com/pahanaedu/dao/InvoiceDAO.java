package com.pahanaedu.dao;

import com.pahanaedu.model.Invoice;


public interface InvoiceDAO extends CrudDAO<Invoice, Integer> {
    boolean updateStatus(int id, String status) throws Exception;
}
