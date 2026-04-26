import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ExpenseTracker {

    static ArrayList<Expense> expenses = new ArrayList<Expense>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadFromFile();

        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Total");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addExpense();
                    break;

                case 2:
                    viewExpenses();
                    break;

                case 3:
                    viewTotal();
                    break;

                case 4:
                    saveToFile();
                    System.out.println("Saved! Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void addExpense() {
        sc.nextLine(); // clear buffer

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();

        expenses.add(new Expense(category, amount));
        System.out.println("Expense added!");
    }

    static void viewExpenses() {
        System.out.println("\n--- All Expenses ---");

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        for (Expense e : expenses) {
            System.out.println(e.getCategory() + " - " + nf.format(e.getAmount()));
        }
    }

    static void viewTotal() {
        double total = 0;

        for (Expense e : expenses) {
            total += e.getAmount();
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        System.out.println("Total Spending: " + nf.format(total));
    }

    static void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("expenses.txt"));

            for (Expense e : expenses) {
                bw.write(e.toString());
                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadFromFile() {
        try {
            File file = new File("expenses.txt");

            if (!file.exists())
                return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String category = data[0];
                double amount = Double.parseDouble(data[1]);

                expenses.add(new Expense(category, amount));
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}