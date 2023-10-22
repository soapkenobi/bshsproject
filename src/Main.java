import java.util.*;
import java.io.*;

class Transaction {
    private String description;
    private boolean isImportant;
    private double amount;
    private Date timeCreated;

    public Transaction(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.timeCreated = new Date(System.currentTimeMillis());
        isImportant = false;
    }

    public Date getTime() {
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

    public void setTime(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}

class PersonalFinanceManager {
    private double balance;
    private Transaction[] transactions;
    public String currencySymbol;

    public PersonalFinanceManager(Currency currency) {
        balance = 0.0;
        transactions = new Transaction[0];
        currencySymbol = currency.getCurrencyCode();
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(String description, double amount) {
        Transaction transaction = new Transaction(description, amount);
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
        System.out.println("Added transaction with ID: " + (transactions.length - 1));
        balance += amount;
    }

    public void addTransaction(Transaction transaction) {
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
        System.out.println("Added transaction with ID: " + (transactions.length - 1));
        balance += transaction.getAmount();
    }

    public void add(Transaction transaction) {
        Transaction[] tempList = transactions;
        transactions = new Transaction[tempList.length + 1];
        System.arraycopy(tempList, 0, transactions, 0, tempList.length);
        transactions[transactions.length - 1] = transaction;
    }

    public void remove(int index) {
        Transaction[] tempArray = new Transaction[transactions.length - 1];
        int oldIndex = 0;
        for (int i = 0; i < transactions.length; i++) {
            if (i != index) tempArray[oldIndex++] = transactions[i];
        }
        transactions = tempArray;
    }

    public void displayTransactions() {
        System.out.println("-".repeat(28) + " Transaction History " + "-".repeat(28));
        String[][] rows = new String[transactions.length + 1][5];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")", "Important"};
        for (int i = 0; i < transactions.length; i++) {
            displayTransaction(rows, i, true);
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat(("-".repeat(28) + " Transaction History " + "-".repeat(28)).length()));
    }

    public void displayImportant() {
        System.out.println("-".repeat(28) + " Important Transaction History " + "-".repeat(28));
        String[][] rows = new String[transactions.length + 1][4];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")"};
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i].isImportant()) {
                displayTransaction(rows, i, false);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat(("-".repeat(28) + " Important Transaction History " + "-".repeat(28)).length()));
    }

    public void displayOptional() {
        System.out.println("-".repeat(28) + " Optional Transaction History " + "-".repeat(28));
        String[][] rows = new String[transactions.length + 1][4];
        rows[0] = new String[]{"ID", "Date", "Time", "Description", "Amount(" + currencySymbol + ")"};
        for (int i = 0; i < transactions.length; i++) {
            if (!(transactions[i].isImportant())) {
                displayTransaction(rows, i, false);
            }
        }
        System.out.println(Main.formatAsTable(rows));
        System.out.println("-".repeat(("-".repeat(28) + " Optional Transaction History " + "-".repeat(28)).length()));
    }

    private void displayTransaction(String[][] rows, int i, boolean showImportant) {
        Transaction transaction = transactions[i];
        String id = i + "";
        String dateCreated = Main.parseDateToString(transaction.getTime()).split(" ")[0];
        String timeCreated = Main.parseDateToString(transaction.getTime()).split(" ")[1];
        String description = transaction.getDescription();
        String amount = transaction.getAmount() + "";
        if (showImportant) {
            boolean important = transaction.isImportant();
            rows[i + 1] = new String[]{id, dateCreated, timeCreated, description, amount, important ? "*" : " "};
        } else rows[i + 1] = new String[]{id, dateCreated, timeCreated, description, amount};
    }

    public void displayByDate(String date) {
        System.out.println("-".repeat(28) + " Transactions on " + date + "-".repeat(28));
        String[][] rows = new String[transactions.length + 1][5]; // Adding 1 for headers
        String[] headers = {"ID", "Time", "Description", "Amount(" + currencySymbol + ")", "Important"};
        rows[0] = headers;
        int rowCount = 1; // Start from the first row (after headers)
        for (int i = 0; i < transactions.length; i++) {
            if (date.equalsIgnoreCase(Main.parseDateToString(transactions[i].getTime()).split(" ")[0])) {
                Transaction transaction = transactions[i];
                String id = String.valueOf(i);
                String timeCreated = Main.parseDateToString(transaction.getTime()).split(" ")[1];
                String description = transaction.getDescription();
                String amount = transaction.getAmount() + "";
                String important = transaction.isImportant() ? "*" : " ";
                rows[rowCount] = new String[]{id, timeCreated, description, amount, important};
                rowCount++;
            }
        }
        String table = Main.formatAsTable(rows);
        System.out.println(table);
        System.out.println("-".repeat(table.length()));
    }


    public Transaction deleteTransaction(String description) {
        Transaction deleted = new Transaction("null_trans", 0);
        for (int i = 0; i < transactions.length; i++) {
            Transaction transaction = transactions[i];
            if (transaction.getDescription().equalsIgnoreCase(description)) {
                deleted = transaction;
                remove(i);
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

    public Transaction[] getTransactions() {
        return transactions;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Transaction transaction : transactions) {
            dos.writeUTF("Transaction:");
            dos.writeUTF(Main.parseDateToString(transaction.getTime()));
            dos.writeUTF(transaction.getDescription());
            dos.writeDouble(transaction.getAmount());
            dos.writeBoolean(transaction.isImportant());
        }
        dos.close();
    }

    public void read(File fl) throws IOException {
        boolean emptyFile = fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        transactions = new Transaction[0];
        if (!emptyFile) {
            while (true) {
                try {
                    if (dis.readUTF().equals("Transaction:")) {
                        String time = dis.readUTF();
                        String description = dis.readUTF();
                        double amount = dis.readDouble();
                        boolean important = dis.readBoolean();
                        Transaction transaction = new Transaction(description, amount);
                        transaction.setTime(Main.parseDate(time));
                        transaction.setImportant(important);
                        add(transaction);
                    }
                } catch (EOFException ignored) {
                    break;
                }
            }
        }
        dis.close();
    }
}

class FinancialCalculators {
    private final Scanner input;

    public FinancialCalculators(Scanner input) {
        this.input = input;
    }

    public Transaction recurringDeposit() {
        System.out.print("Enter amount paid per deposit: ");
        double ed = input.nextDouble();
        System.out.print("Enter rate of interest: ");
        double r = input.nextDouble();
        System.out.println("How many months would this recurring deposit last?");
        int n = input.nextInt();
        double simpleInterest = (ed * n * (n + 1) * r) / (2d * 12 * 100);
        double maturityValue = ed * n + simpleInterest;
        System.out.println("Maturity value of recurring deposit: " + maturityValue);
        return new Transaction("Recurring Deposit", maturityValue);
    }

    public Transaction simpleInterest() {
        System.out.print("Enter amount paid: ");
        double ed = input.nextDouble();
        System.out.print("Enter rate of interest: ");
        double r = input.nextDouble();
        System.out.println("How many months would this account last?");
        int n = input.nextInt();
        double simpleInterest = (ed * n * (n + 1) * r) / (2d * 12 * 100);
        System.out.println("Interest earned on account: " + simpleInterest);
        return new Transaction("Simple Interest", simpleInterest);
    }
}

class Main {
    static File fl;
    static Scanner sc;
    static FinancialCalculators fc;
    static PersonalFinanceManager manager;
    static Currency currency;

    public static void main(String[] args) {
        fl = new File("transactionHistory.log");
        sc = new Scanner(System.in);
        fc = new FinancialCalculators(sc);
        System.out.println("Enter your currency(for example, INR for Indian Rupees):");
        currency = Currency.getInstance(sc.next().toUpperCase());
        manager = new PersonalFinanceManager(currency);
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
                    System.out.print("Enter income amount: " + manager.currencySymbol + " ");
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
                    System.out.print("Enter expense amount: " + manager.currencySymbol + " ");
                    double expenseAmount = sc.nextDouble();
                    manager.addTransaction(expenseDescription, -expenseAmount);
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 3 -> {
                    System.out.println("Enter transaction ID to mark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions()[id].setImportant(true);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 4 -> {
                    System.out.println("Enter transaction ID to unmark important: ");
                    int id = sc.nextInt();
                    try {
                        manager.getTransactions()[id].setImportant(false);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Element with id " + id + " not present in transaction history");
                    }
                    try {
                        manager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                }
                case 5 -> {
                    System.out.print("Enter transaction ID to delete: ");
                    int toDelete = sc.nextInt();
                    if (toDelete < manager.getTransactions().length && toDelete >= 0) {
                        manager.remove(toDelete);
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
                    if (manager.deleteTransaction(toDelete).getDescription().equals("null_trans"))
                        System.out.println("No such transaction found");
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
                    boolean exists = manager.getTransactions().length > toEdit;
                    String[] menuOptions;
                    if (exists) {
                        menuOptions = new String[]{"Go Back", "Description", "Amount", "Time of Creation"};
                        switch (menu("What would you like to edit?", menuOptions)) {
                            case 1 -> {
                                System.out.print("Enter new description for transaction: ");
                                String str = sc.nextLine();
                                manager.getTransactions()[toEdit].setDescription(str);
                            }
                            case 2 -> {
                                System.out.print("Enter updated amount for transaction: ");
                                double newAmount = sc.nextDouble();
                                manager.getTransactions()[toEdit].setAmount(newAmount);
                            }
                            case 3 -> {
                                System.out.print("Enter new date and time(format yyyy/MM/dd HH:mm:ss):");
                                String inputDateTime = sc.nextLine();
                                manager.getTransactions()[toEdit].setTime(parseDate(inputDateTime));
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
                case 10 ->
                        System.out.println("Current Balance: " + manager.currencySymbol + " " + manager.getBalance());
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
                    System.out.println(fl.delete() ? "Successful" : "Unsuccessful");
                    manager = new PersonalFinanceManager(currency);
                }
                case 16 -> {
                    Transaction tr = fc.recurringDeposit();
                    char ch;
                    System.out.println("Would you like to add the maturity value as an income transaction?(Y/N)");
                    ch = sc.next().charAt(0);
                    if (ch == 'Y' || ch == 'y') manager.addTransaction(tr);
                }
                case 17 -> {
                    Transaction tr = fc.simpleInterest();
                    char ch;
                    System.out.println("Would you like to add the interest as an income transaction?(Y/N)");
                    ch = sc.next().charAt(0);
                    if (ch == 'Y' || ch == 'y') manager.addTransaction(tr);
                }

                case 18 -> {
                    System.out.print("Enter new currency code(for example, USD for United States Dollar): ");
                    Currency currency = Currency.getInstance(sc.next().toUpperCase());
                    manager.setCurrencySymbol(currency.getCurrencyCode());
                    System.out.println("Changed Currency Symbol To: " + currency.getCurrencyCode());
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

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n|<=============== * " + header + " * ===============>|");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again");
            boolean hasExitOrGoBack = options[0].equalsIgnoreCase("Exit") || options[0].equalsIgnoreCase("Go Back");
            for (int i = 0; i < options.length; i++) {
                if (hasExitOrGoBack && i == 0) continue;
                printOptions(i, options[i]);
            }
            if (hasExitOrGoBack) printOptions(0, options[0]);
            System.out.print("Select an option to proceed:");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        System.out.println("=".repeat(("|<=============== * " + header + " * ===============>|").length()));
        sc.nextLine();
        return choice;
    }

    public static String formatAsTable(String[][] rows) {
        int columnCount = rows[0].length;

        int[] maxLengths = new int[columnCount];

        for (String[] strings : rows) {
            for (int col = 0; col < columnCount; col++) {
                maxLengths[col] = Math.max(maxLengths[col], strings[col].length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (String[] strings : rows) {
            result.append(String.format(format, (Object[]) strings)).append("\n");
        }

        return result.toString();
    }

    static void printOptions(int opNumber, String option) {
        String space = "  ";
        if (opNumber > 9) space = " ";
        System.out.println("[" + opNumber + "]" + space + option);
    }

    private static String[] getMainMenuOptions() {
        return new String[]{"Exit", "Add Income", "Add Expense", "Mark Transaction Important", "Unmark Transaction Important", "Delete Transaction by ID", "Delete Transactions by Description", "Edit Transaction", "View Total Income", "View Total Expenditure", "View Balance", "View Important Transactions", "View Optional Transactions", "View Transaction History", "View Transactions By Date", "Delete All Transaction Records", "Calculate Maturity Value Of Recurring Deposit", "Calculate Simple Interest", "Change Currency"};
    }

    public static Date parseDate(String input) {
        String date = input.split(" ")[0];
        String time = input.split(" ")[1];
        int years = Integer.parseInt(date.split("/")[0]);
        int months = Integer.parseInt(date.split("/")[1]);
        int days = Integer.parseInt(date.split("/")[2]);
        int hours = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);
        int seconds = Integer.parseInt(time.split(":")[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(years - 1900, months, days, hours, minutes, seconds);
        return calendar.getTime();
    }

    public static String parseDateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String str = "";
        str += calendar.get(Calendar.YEAR) + "/";
        str += calendar.get(Calendar.MONTH) + "/";
        str += calendar.get(Calendar.DAY_OF_MONTH) + " ";
        if (calendar.get(Calendar.HOUR_OF_DAY) <= 9) str += "0";
        str += calendar.get(Calendar.HOUR_OF_DAY) + ":";
        if (calendar.get(Calendar.MINUTE) <= 9) str += "0";
        str += calendar.get(Calendar.MINUTE) + ":";
        if (calendar.get(Calendar.SECOND) <= 9) str += "0";
        str += calendar.get(Calendar.SECOND);
        return str;
    }
}
