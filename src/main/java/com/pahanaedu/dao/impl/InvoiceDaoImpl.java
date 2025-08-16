package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.InvoiceDAO;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.model.Invoice;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.util.DBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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


    //updated code
    //Generate a simple invoice number like "INV-2025-0012"
    public String generateNextInvoiceNo(LocalDate date) throws Exception {
        String pattern = "INV-%d-%04d";
        String sql = "SELECT IFNULL(MAX(id),0)+1 AS n FROM invoices";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            int next = rs.getInt("n");
            return String.format(pattern, date.getYear(), next);
        }
    }



    public int createInvoiceWithItems(Invoice inv,
                                      List<InvoiceItem> lines,
                                      BigDecimal taxRate) throws Exception {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Invoice requires at least one item");
        }

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1) Lock items & check stock
                Map<Integer, Integer> stock = lockAndFetchStock(con, lines.stream()
                        .map(InvoiceItem::getItemId).distinct().collect(Collectors.toList()));

                for (InvoiceItem li : lines) {
                    Integer have = stock.get(li.getItemId());
                    if (have == null) throw new SQLException("Item " + li.getItemId() + " not found");
                    if (have < li.getQuantity())
                        throw new SQLException("Insufficient stock for item " + li.getItemId());
                }

                // 2) Recompute money (server-side)
                MoneyTotals totals = computeTotals(lines, inv.getDiscountAmt(), taxRate);
                inv.setSubtotal(totals.subtotal);
                inv.setTaxAmount(totals.tax);
                inv.setTotalAmount(totals.total);
                if (inv.getInvoiceNo() == null || inv.getInvoiceNo().isEmpty()) {
                    LocalDate d = (inv.getInvoiceDate() != null)
                            ? inv.getInvoiceDate().toLocalDate()
                            : LocalDate.now();
                    inv.setInvoiceNo(generateNextInvoiceNo(d));
                }
                if (inv.getInvoiceDate() == null) {
                    inv.setInvoiceDate(LocalDateTime.now());
                }
                if (inv.getStatus() == null) inv.setStatus("ISSUED");

                // 3) Insert header
                int invoiceId = insertInvoiceHeader(con, inv);

                // 4) Insert lines (batch)
                batchInsertLines(con, invoiceId, lines);

                // 5) Decrement stock (defensive)
                for (InvoiceItem li : lines) {
                    int n = decrementStock(con, li.getItemId(), li.getQuantity());
                    if (n == 0) throw new SQLException("Stock update failed for item " + li.getItemId());
                }

                con.commit();
                return invoiceId;

            } catch (Exception e) {
                try { con.rollback(); } catch (Exception ignore) {}
                throw e;
            } finally {
                try { con.setAutoCommit(true); } catch (Exception ignore) {}
            }
        }
    }


    private Map<Integer, Integer> lockAndFetchStock(Connection con, List<Integer> itemIds) throws Exception {
        if (itemIds == null || itemIds.isEmpty()) return Collections.emptyMap();
        String placeholders = itemIds.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT id, qty FROM items WHERE id IN (" + placeholders + ") FOR UPDATE";
        Map<Integer, Integer> map = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (Integer id : itemIds) ps.setInt(i++, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("id"), rs.getInt("qty"));
                }
            }
        }
        return map;
    }


    private static class MoneyTotals {
        BigDecimal subtotal;
        BigDecimal tax;
        BigDecimal total;
    }


    private MoneyTotals computeTotals(List<InvoiceItem> lines,
                                      BigDecimal discountAmt,
                                      BigDecimal taxRate) {
        MoneyTotals mt = new MoneyTotals();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (InvoiceItem li : lines) {
            BigDecimal line = li.getUnitPrice().multiply(BigDecimal.valueOf(li.getQuantity()));
            subtotal = subtotal.add(line);
        }
        BigDecimal discount = (discountAmt == null) ? BigDecimal.ZERO : discountAmt;
        BigDecimal taxable = subtotal.subtract(discount);
        if (taxable.signum() < 0) taxable = BigDecimal.ZERO;
        BigDecimal rate = (taxRate == null) ? BigDecimal.ZERO : taxRate;
        BigDecimal tax = taxable.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = taxable.add(tax);

        mt.subtotal = subtotal;
        mt.tax = tax;
        mt.total = total;
        return mt;
    }


    private int insertInvoiceHeader(Connection con, Invoice inv) throws Exception {
        String sql = "INSERT INTO invoices (invoice_no, customer_id, invoice_date, subtotal, tax_amount, discount_amt, total_amount, status, created_by) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, inv.getInvoiceNo());
            ps.setInt(2, inv.getCustomerId());
            ps.setTimestamp(3, Timestamp.valueOf(inv.getInvoiceDate() != null ? inv.getInvoiceDate() : LocalDateTime.now()));
            ps.setBigDecimal(4, inv.getSubtotal());
            ps.setBigDecimal(5, inv.getTaxAmount());
            ps.setBigDecimal(6, inv.getDiscountAmt() == null ? BigDecimal.ZERO : inv.getDiscountAmt());
            ps.setBigDecimal(7, inv.getTotalAmount());
            ps.setString(8, inv.getStatus());
            if (inv.getCreatedBy() == null) ps.setNull(9, INTEGER);
            else ps.setInt(9, inv.getCreatedBy());
            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("insertInvoiceHeader: no rows affected");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new NotFoundException("insertInvoiceHeader: no generated key");
    }

    private void batchInsertLines(Connection con, int invoiceId, List<InvoiceItem> lines) throws Exception {
        String sql = "INSERT INTO invoice_items (invoice_id, item_id, quantity, unit_price) VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (InvoiceItem li : lines) {
                ps.setInt(1, invoiceId);
                ps.setInt(2, li.getItemId());
                ps.setInt(3, li.getQuantity());
                ps.setBigDecimal(4, li.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    private int decrementStock(Connection con, int itemId, int qty) throws Exception {
        String upd = "UPDATE items SET qty = qty - ? WHERE id = ? AND qty >= ?";
        try (PreparedStatement ps = con.prepareStatement(upd)) {
            ps.setInt(1, qty);
            ps.setInt(2, itemId);
            ps.setInt(3, qty);
            return ps.executeUpdate();
        }
    }


}
