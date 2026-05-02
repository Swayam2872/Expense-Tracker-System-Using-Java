package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.ExpenseDAOImpl;
import com.expensetracker.model.Expense;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.*;

@WebServlet("/api/expenses")
public class ExpenseServlet extends HttpServlet {

    private ExpenseDAO dao = new ExpenseDAOImpl();

    // ── GET — fetch all or by id, or totals ───────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        cors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        // Prevent browser caching of GET requests
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        PrintWriter out = resp.getWriter();

        String id     = req.getParameter("id");
        String totals = req.getParameter("totals");

        if ("1".equals(totals)) {
            double total = dao.getTotalExpenses();
            Map<String, Double> byCategory = dao.getTotalByCategory();
            StringBuilder sb = new StringBuilder("{\"total\":").append(total).append(",\"byCategory\":{");
            boolean first = true;
            for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
                first = false;
            }
            out.print(sb.append("}}"));

        } else if (id != null) {
            Expense e = dao.getExpenseById(Integer.parseInt(id));
            out.print(e == null ? "null" : toJson(e));

        } else {
            out.print(toJsonArray(dao.getAllExpenses()));
        }
    }

    // ── POST — create ─────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        cors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        Expense e = parseBody(req);
        int newId = dao.addExpense(e);
        if (newId > 0) {
            resp.setStatus(201);
            resp.getWriter().print("{\"success\":true,\"id\":" + newId + "}");
        } else {
            resp.setStatus(500);
            resp.getWriter().print("{\"success\":false}");
        }
    }

    // ── PUT — update ──────────────────────────────────────────────────────────
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        cors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        Expense e = parseBody(req);
        boolean ok = dao.updateExpense(e);
        resp.getWriter().print("{\"success\":" + ok + "}");
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        cors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        String id = req.getParameter("id");
        boolean ok = dao.deleteExpense(Integer.parseInt(id));
        resp.getWriter().print("{\"success\":" + ok + "}");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        cors(resp);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void cors(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private Expense parseBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = req.getReader()) {
            while ((line = br.readLine()) != null) sb.append(line);
        }
        String body = sb.toString();
        Expense e = new Expense();
        e.setId         (intVal(body, "id"));
        e.setCategory   (strVal(body, "category"));
        e.setDescription(strVal(body, "description"));
        e.setAmount     (dblVal(body, "amount"));
        e.setDate       (strVal(body, "date"));
        return e;
    }

    private String toJson(Expense e) {
        return String.format(
            "{\"id\":%d,\"category\":\"%s\",\"description\":\"%s\",\"amount\":%.2f,\"date\":\"%s\"}",
            e.getId(), esc(e.getCategory()), esc(e.getDescription()), e.getAmount(),
            e.getDate() == null ? "" : e.getDate());
    }

    private String toJsonArray(List<Expense> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    // Minimal JSON field extractors (no external lib needed)
    private String strVal(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return "";
        i = json.indexOf(":", i) + 1;
        while (json.charAt(i) == ' ') i++;
        if (json.charAt(i) == '"') { int s = i+1, end = json.indexOf('"', s); return json.substring(s, end); }
        return "";
    }
    private int intVal(String json, String key) {
        try { String s = strVal2(json, key); return s.isEmpty() ? 0 : Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private double dblVal(String json, String key) {
        try { String s = strVal2(json, key); return s.isEmpty() ? 0 : Double.parseDouble(s); } catch (Exception e) { return 0; }
    }
    private String strVal2(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return "";
        i = json.indexOf(":", i) + 1;
        while (i < json.length() && json.charAt(i) == ' ') i++;
        int end = i;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
        return json.substring(i, end).trim().replace("\"", "");
    }
    private String esc(String s) { return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\""); }
}
