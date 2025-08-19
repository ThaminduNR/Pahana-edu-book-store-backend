package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.Invoice;


public interface InvoiceDAO extends CrudDAO<Invoice, Integer> {
    boolean updateStatus(int id, String status) throws Exception;

    int getLastInvoiceId() throws Exception;

    Bill getInvoiceDetails(int invoiceId) throws Exception;


}
