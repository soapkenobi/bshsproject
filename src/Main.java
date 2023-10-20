import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;

class Transaction {
    private String description;
    private boolean isImportant;
    private double amount;
    private LocalDateTime timeCreated;

    public Transaction(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.timeCreated = LocalDateTime.now();
        isImportant = false;
    }

    public LocalDateTime getTime() {
        return timeCreated;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }
}

class PersonalFinanceManager {
    public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private double balance;
    private ArrayList<Transaction> transactions;

    public PersonalFinanceManager() {
        balance = 0.0;
        transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(String description, double amount) {
        Transaction transaction = new Transaction(description, amount);
        transactions.add(transaction);
        balance += amount;
    }

    public void displayTransactions() {
        System.out.println("---------------------------- Transaction History ----------------------------");
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("ID", "Date", "Time", "Description", "Amount");
        rows.add(headers);
        for (int i = 0; i < transactions.size(); i++) {
            displayTransaction(rows, i);
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat("---------------------------- Transaction History ----------------------------".length()));
    }

    public void displayImportant() {
        System.out.println("---------------------------- Important Transaction History ----------------------------");
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("ID", "Date", "Time", "Description", "Amount");
        rows.add(headers);
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).isImportant()) {
                displayTransaction(rows, i);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat("---------------------------- Important Transaction History ----------------------------".length()));
    }

    public void displayOptional() {
        System.out.println("---------------------------- Optional Transaction History ----------------------------");
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("ID", "Date", "Time", "Description", "Amount");
        rows.add(headers);
        for (int i = 0; i < transactions.size(); i++) {
            if (!(transactions.get(i).isImportant())) {
                displayTransaction(rows, i);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat("---------------------------- Optional Transaction History ----------------------------".length()));
    }

    private void displayTransaction(List<List<String>> rows, int i) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Transaction transaction = transactions.get(i);
        String id = i + "";
        String dateCreated = df.format(transaction.getTime());
        String timeCreated = tf.format(transaction.getTime());
        String description = transaction.getDescription();
        String amount = transaction.getAmount() + "";
        rows.add(Arrays.asList(id, dateCreated, timeCreated, description, amount));
    }

    public void displayByDate(String date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("---------------------------- Transactions on " + date + " ----------------------------");
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("ID", "Time", "Description", "Amount");
        rows.add(headers);
        for (int i = 0; i < transactions.size(); i++) {
            if (df.format(transactions.get(i).getTime()).equalsIgnoreCase(date)) {
                Transaction transaction = transactions.get(i);
                String id = i + "";
                String timeCreated = tf.format(transaction.getTime());
                String description = transaction.getDescription();
                String amount = transaction.getAmount() + "";
                rows.add(Arrays.asList(id, timeCreated, description, amount));
            }
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat(("---------------------------- Transactions on " + date + " ----------------------------").length()));
    }


    public Transaction deleteTransaction(String description) {
        Transaction deleted = new Transaction("null_trans", 0);
        for (Transaction transaction : transactions) {
            if (transaction.getDescription().equalsIgnoreCase(description)) {
                deleted = transaction;
                transactions.remove(transaction);
            }
        }
        return deleted;
    }

    public double totalIncome() {
        double income = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() >= 0) income += transaction.getAmount();
        }
        return income;
    }

    public double totalExpenses() {
        double expense = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() <= 0) expense += Math.abs(transaction.getAmount());
        }
        return expense;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Transaction transaction : transactions) {
            dos.writeUTF("Transaction:");
            dos.writeUTF(dtf.format(transaction.getTime()));
            dos.writeUTF(transaction.getDescription());
            dos.writeDouble(transaction.getAmount());
            dos.writeBoolean(transaction.isImportant());
        }
        dos.close();
    }

    public void read(File fl) throws IOException {
        fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        transactions = new ArrayList<>();
        try {
            while (true) {
                if (dis.readUTF().equals("Transaction:")) {
                    String time = dis.readUTF();
                    LocalDateTime parser = LocalDateTime.parse(time, dtf);
                    String description = dis.readUTF();
                    double amount = dis.readDouble();
                    boolean important = dis.readBoolean();
                    Transaction transaction = new Transaction(description, amount);
                    transaction.setTime(parser);
                    transaction.setImportant(important);
                    transactions.add(transaction);
                }
            }
        } catch (EOFException ignored) {
        }
        dis.close();
    }
}

class Main {
    static File fl = new File("transactionHistory.log");
    static Scanner sc = new Scanner(System.in);
    static PersonalFinanceManager manager;

    public static void main(String[] args) {
        manager = new PersonalFinanceManager();
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            try {
                manager.read(fl);
            } catch (IOException e) {
                System.out.println("ERROR: Couldn't read file");
                System.exit(0);
            }
            int choice = menu("Personal Money Management Application", getMainMenuOptions());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter income description: ");
                    String incomeDescription = sc.nextLine();
                    System.out.print("Enter income amount: ₹");
                    double incomeAmount = sc.nextDouble();
                    manager.addTransaction(incomeDescription, incomeAmount);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 2 -> {
                    System.out.print("Enter expense description: ");
                    String expenseDescription = sc.nextLine();
                    System.out.print("Enter expense amount: ₹");
                    double expenseAmount = sc.nextDouble();
                    manager.addTransaction(expenseDescription, -expenseAmount);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 3 -> {
                    System.out.println("Enter expense ID to mark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions().get(id).setImportant(true);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                }
                case 4 -> {
                    System.out.println("Enter expense ID to unmark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions().get(id).setImportant(false);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                }
                case 5 -> {
                    System.out.print("Enter transaction ID to delete: ");
                    int toDelete = sc.nextInt();
                    if (toDelete < manager.getTransactions().size()) {
                        manager.getTransactions().remove(toDelete);
                        System.out.println("Transaction deleted successfully");
                    } else System.out.println("Transaction with ID " + toDelete + " not found");
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 6 -> {
                    System.out.print("Enter transaction description to delete: ");
                    String toDelete = sc.nextLine();
                    if(manager.deleteTransaction(toDelete).getDescription().equals("null_trans")) System.out.println("No such transaction found");
                    else System.out.println("Transaction deleted successfully");
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 7 -> {
                    System.out.print("Enter transaction ID to edit: ");
                    int toEdit = sc.nextInt();
                    boolean exists = manager.getTransactions().size() > toEdit;
                    ArrayList<String> menuOptions = new ArrayList<>();
                    if (exists) {
                        menuOptions.add("Go Back");
                        menuOptions.add("Description");
                        menuOptions.add("Amount");
                        menuOptions.add("Time of creation");
                        switch (menu("What would you like to edit?", menuOptions)) {
                            case 1 -> {
                                System.out.print("Enter new description for transaction: ");
                                String str = sc.nextLine();
                                manager.getTransactions().get(toEdit).setDescription(str);
                            }
                            case 2 -> {
                                System.out.print("Enter updated amount for transaction: ");
                                double newAmount = sc.nextDouble();
                                manager.getTransactions().get(toEdit).setAmount(newAmount);
                            }
                            case 3 -> {
                                System.out.print("Enter new date and time(format yyyy/MM/dd HH:mm:ss):");
                                LocalDateTime localDateTime = LocalDateTime.parse(sc.nextLine(), manager.dtf);
                                manager.getTransactions().get(toEdit).setTime(localDateTime);
                            }
                        }
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 8 -> System.out.println("Total income recorded: " + manager.totalIncome());
                case 9 -> System.out.println("Total expenditure recorded: " + manager.totalExpenses());
                case 10 -> System.out.println("Current Balance: $" + manager.getBalance());
                case 11 -> manager.displayImportant();
                case 12 -> manager.displayOptional();
                case 13 -> manager.displayTransactions();
                case 14 -> {
                    System.out.println("Enter date to filter(yyyy/MM/dd): ");
                    String date = sc.nextLine();
                    manager.displayByDate(date);
                }
                case 15 -> {
                    System.out.print("Deleting all user data: ");
                    System.out.println(fl.delete()?"Successful" : "Unsuccessful");
                    main(new String[0]);
                }
                default -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
            }
        }
    }

    static int menu(String header, ArrayList<String> options) {
        int choice;
        int c = 0;
        System.out.println("\n|=============== * " + header + " * ===============|");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again or press Ctrl+D to exit");
            boolean hasExitFirst = options.get(0).equalsIgnoreCase("Exit") || options.get(0).equalsIgnoreCase("Go Back");
            for (int i = 0; i < options.size(); i++) {
                if (hasExitFirst && i == 0)
                    continue;
                printOption(i, options.get(i));
            }
            if (hasExitFirst)
                printOption(0, options.get(0));
            System.out.print("Select an option to proceed:");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.size());
        System.out.println("=".repeat(("|==============- * " + header + " * -==============|").length()));
        sc.nextLine();
        return choice;
    }

    public static String formatAsTable(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }
        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();
        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(format, (Object[]) row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }

    static void printOption(int selNumber, String option) {
        System.out.println("[" + selNumber + "] " + option);
    }

    private static ArrayList<String> getMainMenuOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Exit");
        options.add("Add Income");
        options.add("Add Expense");
        options.add("Mark Transaction Important");
        options.add("Unmark Transaction Important");
        options.add("Delete Transaction by ID");
        options.add("Delete Transactions by description");
        options.add("Edit Transaction");
        options.add("View Total Income");
        options.add("View Total Expenditure");
        options.add("View Balance");
        options.add("View Important Transactions");
        options.add("View Optional Transactions");
        options.add("View Transaction History");
        options.add("View Transactions By Date");
        options.add("Delete All Transaction Records");
        return options;
    }
}
