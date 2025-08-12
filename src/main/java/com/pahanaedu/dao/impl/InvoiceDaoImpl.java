package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.InvoiceDAO;
import com.pahanaedu.model.Invoice;
import com.pahanaedu.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.sql.Types.INTEGER;

public class InvoiceDaoImpl implements InvoiceDAO {
    @Override
    public List<Invoice> findAll() throws Exception {
        String sql = "SELECT id,invoice_no,customer_id,invoice_date,subtotal,tax_amount,discount_amt,total_amount,status,created_by FROM invoices ORDER BY id DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Invoice> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    @Override
    public Invoice findById(Integer id) throws Exception {
        String sql = "SELECT id,invoice_no,customer_id,invoice_date,subtotal,tax_amount,discount_amt,total_amount,status,created_by FROM invoices WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public int create(Invoice inv) throws Exception {
        String sql = "INSERT INTO invoices (invoice_no, customer_id, invoice_date, subtotal, tax_amount, discount_amt, total_amount, status, created_by) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Optional but helpful:
            // ps.setQueryTimeout(5);

            ps.setString(1, inv.getInvoiceNo());
            ps.setInt(2, inv.getCustomerId());
            ps.setTimestamp(3, Timestamp.valueOf(
                    inv.getInvoiceDate() != null ? inv.getInvoiceDate() : java.time.LocalDateTime.now()
            ));
            ps.setBigDecimal(4, inv.getSubtotal());
            ps.setBigDecimal(5, inv.getTaxAmount());
            ps.setBigDecimal(6, inv.getDiscountAmt());
            ps.setBigDecimal(7, inv.getTotalAmount());
            ps.setString(8, inv.getStatus());
            if (inv.getCreatedBy() == null) ps.setNull(9, INTEGER);
            else ps.setInt(9, inv.getCreatedBy());

            int affected = ps.executeUpdate();
            if (affected == 0) return 0;

            try (ResultSet keys = ps.getGeneratedKeys()) {

                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean update(Invoice inv) throws Exception {
        String sql = "UPDATE invoices SET invoice_no=?, customer_id=?, invoice_date=?, " +
                "subtotal=?, tax_amount=?, discount_amt=?, total_amount=?, status=?, created_by=? " +
                "WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inv.getInvoiceNo());
            ps.setInt(2, inv.getCustomerId());
            if (inv.getInvoiceDate() != null)
                ps.setTimestamp(3, Timestamp.valueOf(inv.getInvoiceDate()));
            else
                ps.setNull(3, java.sql.Types.TIMESTAMP);

            ps.setBigDecimal(4, inv.getSubtotal());
            ps.setBigDecimal(5, inv.getTaxAmount());
            ps.setBigDecimal(6, inv.getDiscountAmt());
            ps.setBigDecimal(7, inv.getTotalAmount());
            ps.setString(8, inv.getStatus());

            if (inv.getCreatedBy() == null)
                ps.setNull(9, INTEGER);
            else
                ps.setInt(9, inv.getCreatedBy());

            ps.setInt(10, inv.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM invoices WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE invoices SET status=? WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Invoice map(ResultSet rs) throws SQLException {
        Invoice inv = new Invoice();
        inv.setId(rs.getInt("id"));
        inv.setInvoiceNo(rs.getString("invoice_no"));
        inv.setCustomerId(rs.getInt("customer_id"));
        Timestamp ts = rs.getTimestamp("invoice_date");
        inv.setInvoiceDate(ts != null ? ts.toLocalDateTime() : null);
        inv.setSubtotal(rs.getBigDecimal("subtotal"));
        inv.setTaxAmount(rs.getBigDecimal("tax_amount"));
        inv.setDiscountAmt(rs.getBigDecimal("discount_amt"));
        inv.setTotalAmount(rs.getBigDecimal("total_amount"));
        inv.setStatus(rs.getString("status"));
        int createdBy = rs.getInt("created_by");
        inv.setCreatedBy(rs.wasNull() ? null : createdBy);
        return inv;
    }


}
