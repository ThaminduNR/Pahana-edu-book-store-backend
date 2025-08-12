package com.pahanaedu.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class InvoiceDto {
    private int id;
    private String invoiceNo;
    private int customerId;
    private String invoiceDate;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal discountAmt = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String status;
    private Integer createdBy;

    public InvoiceDto() {
    }

    public InvoiceDto(int id, String invoiceNo, int customerId, String invoiceDate, BigDecimal subtotal, BigDecimal taxAmount, BigDecimal discountAmt, BigDecimal totalAmount, String status, Integer createdBy) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmt = discountAmt;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(BigDecimal discountAmt) {
        this.discountAmt = discountAmt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }


}