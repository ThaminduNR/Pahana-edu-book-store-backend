package com.pahanaedu.model;

import com.pahanaedu.dto.InvoiceDto;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceWithItemsRequest {

    private InvoiceDto invoice;          // header data
    private BigDecimal taxRate;          // e.g. 0.15
    private List<ItemLine> items;


    public InvoiceDto getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDto invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public List<ItemLine> getItems() {
        return items;
    }

    public void setItems(List<ItemLine> items) {
        this.items = items;
    }

    public static class ItemLine {
        private int itemId;
        private int quantity;
        private BigDecimal unitPrice;

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
}
