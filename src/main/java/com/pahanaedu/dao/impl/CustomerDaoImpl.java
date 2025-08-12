package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerDaoImpl implements CustomerDAO {
    @Override
    public List<Customer> findAll() throws Exception {
        String sql = "SELECT id,name,address,phone,email FROM customers ORDER BY id DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Customer> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    @Override
    public Customer findById(Integer id) throws Exception {
        String sql = "SELECT id,name,address,phone,email FROM customers WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public int create(Customer cus) throws Exception {
        String sql = "INSERT INTO customers (name,address,phone,email) VALUES (?,?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cus.getName());
            ps.setString(2, cus.getAddress());
            ps.setString(3, cus.getPhone());
            ps.setString(4, cus.getEmail());
            int affected = ps.executeUpdate();
            if (affected == 0) return 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }

        }
    }

    @Override
    public boolean update(Customer cus) throws Exception {
        String sql = "UPDATE customers SET name=?,address=?,phone=?,email=? WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cus.getName());
            ps.setString(2, cus.getAddress());
            ps.setString(3, cus.getPhone());
            ps.setString(4, cus.getEmail());
            ps.setInt(5, cus.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM customers WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }


    private Customer map(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email")
        );
    }
}
