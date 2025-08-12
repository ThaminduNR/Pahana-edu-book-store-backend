package com.pahanaedu.service;

import com.pahanaedu.dto.InvoiceDto;

public interface InvoiceService extends CrudService<InvoiceDto,Integer>{

    boolean updateStatus(int id, String status) throws Exception;
}
