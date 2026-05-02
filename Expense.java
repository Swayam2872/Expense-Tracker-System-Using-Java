package com.expensetracker.model;

public class Expense {

    private int    id;
    private String category;
    private String description;
    private double amount;
    private String date;

    public Expense() {}

    public Expense(String category, String description, double amount, String date) {
        this.category    = category;
        this.description = description;
        this.amount      = amount;
        this.date        = date;
    }

    public Expense(int id, String category, String description, double amount, String date) {
        this.id          = id;
        this.category    = category;
        this.description = description;
        this.amount      = amount;
        this.date        = date;
    }

    public int    getId()            { return id; }
    public void   setId(int id)      { this.id = id; }

    public String getCategory()             { return category; }
    public void   setCategory(String c)     { this.category = c; }

    public String getDescription()             { return description; }
    public void   setDescription(String d)     { this.description = d; }

    public double getAmount()               { return amount; }
    public void   setAmount(double a)       { this.amount = a; }

    public String getDate()               { return date; }
    public void   setDate(String d)       { this.date = d; }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", category='" + category + "', description='" +
               description + "', amount=" + amount + ", date='" + date + "'}";
    }
}
