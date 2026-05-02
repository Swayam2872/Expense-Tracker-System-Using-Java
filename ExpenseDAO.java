package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import java.util.List;
import java.util.Map;

public interface ExpenseDAO {
    int            addExpense(Expense expense);           // CREATE
    Expense        getExpenseById(int id);                // READ (single)
    List<Expense>  getAllExpenses();                       // READ (all)
    boolean        updateExpense(Expense expense);        // UPDATE
    boolean        deleteExpense(int id);                 // DELETE
    double         getTotalExpenses();                    // SUM
    Map<String, Double> getTotalByCategory();             // SUM per category
}
