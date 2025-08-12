package com.pahanaedu.dto;

import java.math.BigDecimal;

public class InvoiceItemDto {

    private int id;
    private int invoiceId;
    private int itemId;
    private int quantity;
    private BigDecimal unitPrice;

    public InvoiceItemDto() {
    }

    public InvoiceItemDto(int id, int invoiceId, int itemId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
