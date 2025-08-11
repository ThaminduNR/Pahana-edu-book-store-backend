package com.pahanaedu.dto;

import java.math.BigDecimal;

public class ItemDto {

    private int id;
    private String code;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private int qty;

    public ItemDto() {
    }

    public ItemDto(int id, String code, String name, String description, BigDecimal unitPrice, int qty) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public ItemDto(String code, String name, String description, BigDecimal unitPrice, int qty) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
