package com.pahanaedu.model;

import com.pahanaedu.dto.BillItemDTO;

import java.math.BigDecimal;
import java.util.List;

public class Bill {
    private int invoiceId;
    private String invoiceNumber;
    private String invoiceDate;
    private String customerID;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal total;
    private String status;
    private List<BillItemDTO> items;

    public Bill() {
    }

    public Bill(int invoiceId, String invoiceNumber, String invoiceDate, String customerID, BigDecimal subTotal, BigDecimal tax, BigDecimal discount, BigDecimal total, String status, List<BillItemDTO> items) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.customerID = customerID;
        this.subTotal = subTotal;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
        this.status = status;
        this.items = items;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BillItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BillItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "invoiceId=" + invoiceId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", customerID='" + customerID + '\'' +
                ", subTotal=" + subTotal +
                ", tax=" + tax +
                ", discount=" + discount +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", items=" + items +
                '}';
    }
}
