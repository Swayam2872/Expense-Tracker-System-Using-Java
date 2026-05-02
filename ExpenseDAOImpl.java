package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ExpenseDAOImpl implements ExpenseDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    @Override
    public int addExpense(Expense e) {
        String sql = "INSERT INTO expenses (category, description, amount, date) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getCategory());
            ps.setString(2, e.getDescription());
            ps.setDouble(3, e.getAmount());
            ps.setString(4, e.getDate());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    // ── READ (single) ─────────────────────────────────────────────────────────
    @Override
    public Expense getExpenseById(int id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // ── READ (all) ────────────────────────────────────────────────────────────
    @Override
    public List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY date DESC, id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @Override
    public boolean updateExpense(Expense e) {
        String sql = "UPDATE expenses SET category=?, description=?, amount=?, date=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getCategory());
            ps.setString(2, e.getDescription());
            ps.setDouble(3, e.getAmount());
            ps.setString(4, e.getDate());
            ps.setInt(5, e.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ── TOTAL ─────────────────────────────────────────────────────────────────
    @Override
    public double getTotalExpenses() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM expenses";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // ── TOTAL BY CATEGORY ─────────────────────────────────────────────────────
    @Override
    public Map<String, Double> getTotalByCategory() {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT category, SUM(amount) AS total FROM expenses GROUP BY category ORDER BY total DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) map.put(rs.getString("category"), rs.getDouble("total"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    // ── Helper: map ResultSet row → Expense ───────────────────────────────────
    private Expense map(ResultSet rs) throws SQLException {
        return new Expense(
            rs.getInt("id"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getDouble("amount"),
            rs.getString("date")
        );
    }
}
