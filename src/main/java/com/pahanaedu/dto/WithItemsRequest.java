package com.pahanaedu.dto;

import com.pahanaedu.servlets.InvoiceServlet;

import java.math.BigDecimal;
import java.util.List;

public class WithItemsRequest {
    InvoiceDto invoice;
    List<InvoiceServlet.ItemLine> items;
    BigDecimal taxRate;
}
