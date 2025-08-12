package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.InvoiceItemDAO;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.sql.Types.INTEGER;

public class InvoiceItemDaoImpl implements InvoiceItemDAO {
    @Override
    public List<InvoiceItem> findAll() throws Exception {
        String sql = "SELECT id, invoice_id, item_id, quantity, unit_price FROM invoice_items ORDER BY id DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<InvoiceItem> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    @Override
    public InvoiceItem findById(Integer id) throws Exception {
        String sql = "SELECT id,invoice_id,item_id,quantity,unit_price FROM invoice_items WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public int create(InvoiceItem item) throws Exception {
        String sql = "INSERT INTO invoice_items (invoice_id,item_id,quantity,unit_price) VALUES (?,?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (item.getInvoiceId() == 0) ps.setNull(1, INTEGER); else ps.setInt(1, item.getInvoiceId());
            if (item.getItemId() == 0)    ps.setNull(2, INTEGER); else ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());

            int affected = ps.executeUpdate();
            if (affected == 0) return 0;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean update(InvoiceItem item) throws Exception {
        String sql = "UPDATE invoice_items SET invoice_id=?, item_id=?, quantity=?, unit_price=? WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (item.getInvoiceId() == 0) ps.setNull(1, INTEGER); else ps.setInt(1, item.getInvoiceId());
            if (item.getItemId() == 0)    ps.setNull(2, INTEGER); else ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setInt(5, item.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM invoice_items WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<InvoiceItem> findByInvoiceId(Integer invoiceId) throws Exception {
        String sql = "SELECT id,invoice_id,item_id,quantity,unit_price FROM invoice_items WHERE invoice_id=? ORDER BY id";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                List<InvoiceItem> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    @Override
    public boolean deleteByInvoiceId(Integer invoiceId) throws Exception {
        String sql = "DELETE FROM invoice_items WHERE invoice_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            return ps.executeUpdate() > 0;
        }
    }

    private InvoiceItem map(ResultSet rs) throws SQLException {
        InvoiceItem it = new InvoiceItem();
        it.setId(rs.getInt("id"));
        it.setInvoiceId(rs.getInt("invoice_id"));
        it.setItemId(rs.getInt("item_id"));
        it.setQuantity(rs.getInt("quantity"));
        it.setUnitPrice(rs.getBigDecimal("unit_price"));
        return it;
    }
}
