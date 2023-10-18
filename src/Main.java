// Computer Project 2023-24; Personal Money Management Application
// By Ashutosh Mishra, XB, Roll 3

import java.io.*;
import java.util.Scanner;

/**
 * Contains the main class, which consists core backend functions and calls methods from other classes as
 * required
 * @author Ashutosh Mishra
 */
class Main {
    static Income income = new Income();
    static Scanner sc = new Scanner(System.in);
    static File fl = new File("userinfo");

    public static void main(String[] args) throws IOException {
        fl.mkdir();
        fl = new File(fl.getPath() + "/income.dat");
        mainMenu();
    }

    /**
     * Displays the main menu of the application, and handles the user's choice by calling corresponding functions
     * @throws IOException if the file could not be read
     * @author Ashutosh Mishra
     * @see Main#menu(String, String[])
     */
    static void mainMenu() throws IOException {
        int result = menu("Personal Money Management Application", new String[]{"Exit", "Set monthly income", "View monthly Income", "Edit Income Sources", "Delete user data"});
        switch (result) {
            case 0 -> {
                System.out.println("Gracefully Shutting Down");
                System.exit(0);
            }
            case 1 -> {
                income.setIncome();
                mainMenu();
            }
            case 2 -> {
                income.viewEntry();
                mainMenu();
            }
            case 3 -> {
                income.edit();
                mainMenu();
            }
            case 4 -> {
                System.out.print("Deleting user data");
                File folder = new File("userinfo");
                if (folder.listFiles() != null) {
                    for (File toDelete : folder.listFiles()) {
                        DataOutputStream dos = new DataOutputStream(new FileOutputStream(toDelete));
                        dos.close();
                        toDelete.delete();
                    }
                }
                folder.delete();
                mainMenu();
            }
        }
    }

    /**
     * Prints a menu using the given arguments, then takes input from user and checks if it is valid option and returns
     * it
     * @param header  the title of the menu
     * @param options the options in the menu
     * @return the choice input by the user
     * @author Ashutosh Mishra
     */
    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n================ " + header + " ================");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again or press Ctrl+D to exit");
            for (int i = 0; i < options.length; i++) System.out.println("[" + i + "] " + options[i]);
            System.out.print("Your choice:");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        String equals = "";
        for (int i = 0; i < ("================ " + header + " ================").length(); i++)
            equals += "=";
        System.out.println(equals);
        return choice;
    }

    /**
     * Replaces a given file with another
     * @param org File to replace with
     * @param dest File to be replaced
     */
    static void fileReplacer(File org, File dest) throws IOException {
        dest.createNewFile();
        org.createNewFile();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(dest));
        DataInputStream dis = new DataInputStream(new FileInputStream(org));
        try {
            while (true) {
                dos.writeUTF(dis.readUTF());
                dos.writeDouble(dis.readDouble());
            }
        } catch (EOFException ignored) {
        }
        dos.close();
        dis.close();
    }
}

/**
 * Handles Income related calls, writes to file located at ./userinfo/income.dat
 * @author Ashutosh Mishra
 */
class Income {
    String[] sources;
    Scanner sc = new Scanner(System.in);
    double[] income;

    void writeFile(boolean append) throws IOException {
        File fl = new File("userinfo/income.dat");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl, append));
        System.out.println("How many monthly income sources would you like to add?(for example, enter 1 if you want to add your business, 2 if you want to add your wage and stocks)");
        System.out.print("Your answer:");
        sources = new String[sc.nextInt()];
        income = new double[sources.length];
        System.out.println("Enter the sources of monthly income you would like to add:");
        for (int i = 0; i < sources.length; i++) sources[i] = new Scanner(System.in).nextLine();
        for (String source : sources) {
            dos.writeUTF(source);
            dos.writeDouble(0.0);
        }
        dos.close();
        readFile();
    }

    void readFile() throws IOException {
        File fl = new File("userinfo/income.dat");
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        int l = 0;
        try {
            while (true) {
                dis.readUTF();
                dis.readDouble();
                l++;
            }
        } catch (EOFException ignored) {
        }
        sources = new String[l];
        income = new double[l];
        dis.close();
        dis = new DataInputStream(new FileInputStream(fl));
        for (int i = 0; i < sources.length; i++) {
            sources[i] = dis.readUTF();
            income[i] = dis.readDouble();
        }
        dis.close();
    }

    void createReadFile() throws IOException {
        File fl = new File("userinfo/income.dat");
        try {
            fl.createNewFile();
        } catch (IOException ignored) {
        }
        boolean isEmpty = new BufferedReader(new FileReader(fl.getPath())).readLine() == null;
        if (isEmpty) {
            System.out.println("WARNING: No data found in file, creating a new file, ignore if this is first run");
            writeFile(false);
        } else {
            readFile();
        }
    }

    void setIncome() throws IOException {
        createReadFile();
        File fl = Main.fl;
        File tmp = new File("userinfo/income.tmp");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(tmp));
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        int sel = Main.menu("Select income source:", sources);
        System.out.println("Enter amount of income to add:");
        double amt = sc.nextDouble();
        income[sel] = amt;
        try {
            int record = 0;
            while (true) {
                dos.writeUTF(dis.readUTF());
                if (record == sel) {
                    dos.writeDouble(income[sel]);
                    dis.readDouble();
                } else dos.writeDouble(dis.readDouble());
                record++;
            }
        } catch (EOFException ignored) {
        }
        dos.close();
        dis.close();
        Main.fileReplacer(tmp, fl);
        tmp.delete();
    }

    void addEntry() throws IOException {
        writeFile(true);
    }

    void viewEntry() throws IOException {
        createReadFile();
        double sum = 0;
        for (double s : income) sum += s;
        System.out.println("Total Monthly Income = " + sum);
        System.out.print("Would you like to view monthly income gained from a specific source? (Y/N)");
        char ch;
        do {
            ch = sc.next().charAt(0);
        } while (ch != 'Y' && ch != 'z' && ch != 'Z' && ch != 'y');
        if (ch == 'Y' || ch == 'y') {
            int sel = Main.menu("Select income source to view", sources);
            System.out.println("Income gained per month by " + sources[sel] + " = " + income[sel]);
        }
    }

    void edit() throws IOException {
        createReadFile();
        int option = Main.menu("Select action", new String[]{"Exit", "Add Income Source", "List Income Sources", "Delete Income Source"});
        switch (option) {
            case 0 -> Main.mainMenu();
            case 1 -> {
                addEntry();
                edit();
            }
            case 2 -> {
                list();
                edit();
            }
            case 3 -> {
                int sel = Main.menu("Which entry do you want to delete?", sources);
                File tmp = new File("userinfo/income.tmp");
                File fl = new File("userinfo/income.dat");
                Main.fileReplacer(fl, tmp);
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
                DataInputStream dis = new DataInputStream(new FileInputStream(tmp));
                try {
                    for (int c = 0; true; c++) {
                        if (c != sel) {
                            dos.writeUTF(dis.readUTF());
                            dos.writeDouble(dis.readDouble());
                        } else {
                            dis.readUTF();
                            dis.readDouble();
                        }
                    }
                } catch (EOFException ignored) {
                }
                dos.close();
                dis.close();
                tmp.delete();
                readFile();
                edit();
            }
        }
    }

    void list() throws IOException {
        createReadFile();
        for (int i = 0; i < sources.length; i++) {
            System.out.println("[" + i + "]" + sources[i]);
        }
    }
}