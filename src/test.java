import java.io.*;
import java.util.Scanner;

class Alumnus {
    private String name;
    private int graduationYear;
    private String contactInfo;
    private boolean isVIP;
    private double donations;
    private String[] awards;

    //Parameterized Constructor to take Alumnus info when adding one
    public Alumnus(String name, int graduationYear, String contactInfo) {
        this.name = name;
        this.graduationYear = graduationYear;
        this.contactInfo = contactInfo;
        this.isVIP = false;
        this.donations = 0;
        this.awards = new String[0];
    }

    // Method to mark an alumnus as VIP
    public void markAsVIP() {
        this.isVIP = true;
    }


    // Method to display all awards for an alumnus
    public void displayAwards() {
        System.out.println("Awards for " + name + ":");
        for (String award : awards) {
            System.out.println(award);
        }
    }


    // Getter method for Alumnus Name
    public String getName() {
        return name;
    }

    // Setter method for Alumnus Name
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for Alumnus Graduation Year
    public int getGraduationYear() {
        return graduationYear;
    }

    // Setter method for Alumnus Graduation Year
    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    // Getter method for Alumnus Contact Information
    public String getContactInfo() {
        return contactInfo;
    }

    // Setter method for Alumnus Contact Information
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // Checks if Alumnus is VIP and returns the value as a boolean
    public boolean isVIP() {
        return isVIP;
    }

    // Setter method for Alumnus VIP status
    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    // Getter method for Alumnus donation amount
    public double getDonations() {
        return donations;
    }

    // Setter method for Alumnus donation amount
    public void setDonations(double donations) {
        this.donations = donations;
    }

    // Getter method for Alumnus' list of awards
    public String[] getAwards() {
        return awards;
    }

    // Additive Setter method for Alumnus' awards
    public void addAward(String award) {
        String[] tempList = awards;
        awards = new String[tempList.length + 1];
        System.arraycopy(tempList, 0, awards, 0, tempList.length);
        awards[awards.length - 1] = award;
    }

    // Additive Setter method for Alumnus donation amount
    public void addDonation(double donation) {
        this.donations += donation;
    }
}

class AlumniManagementSystem {
    private Alumnus[] alumniList;

    // Constructor to initialise the list of alumni
    public AlumniManagementSystem() {
        alumniList = new Alumnus[0];
    }

    // Method to rename a specific Alumnus
    public void renameAlumnus(String currentName, String newName) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(currentName)) {
                alumnus.setName(newName);
            }
        }
    }

    // Method to add an alumnus to the list
    public void addAlumnus(Alumnus alumnus) {
        add(alumnus);
    }

    // Method to delete an alumnus by name
    public void deleteAlumnus(String name) {
        for (int i = 0; i < alumniList.length; i++) {
            Alumnus alumnus = alumniList[i];
            if (alumnus.getName().equalsIgnoreCase(name)) {
                remove(i);
                break;
            }
        }
    }

    // Method to update contact information of an alumnus
    public void updateContactInfo(String name, String newContactInfo) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(name)) {
                alumnus.setContactInfo(newContactInfo);
                break;
            }
        }
    }

    // Method to display the list of all alumni
    public void displayAllAlumni() {
        System.out.println("List of all alumni:");
        for (Alumnus alumnus : alumniList) {
            System.out.println("Name: " + alumnus.getName());
            System.out.println("Graduation Year: " + alumnus.getGraduationYear());
            System.out.println("VIP: " + (alumnus.isVIP() ? "Yes" : "No"));
            System.out.println("Donations: " + alumnus.getDonations());
            System.out.println("Awards: ");
            for (String award : alumnus.getAwards()) System.out.println(" - " + award);
            System.out.println();
        }
    }

    // Method to add donations to a specific alumnus
    public void addDonation(String nameToDonate, double donationAmount) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(nameToDonate)) {
                alumnus.addDonation(donationAmount);
                break;
            }
        }
    }

    // Method to set donation to a specific alumnus
    public void updateDonation(String nameToDonate, double donationAmount) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(nameToDonate)) {
                alumnus.setDonations(donationAmount);
                break;
            }
        }
    }

    // Method to set Graduation Year of a specific alumnus
    public void updateGradYear(String nameToUpdate, int newGradYear) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(nameToUpdate)) {
                alumnus.setGraduationYear(newGradYear);
                break;
            }
        }
    }

    // Method to get a list of Alumni as return
    public Alumnus[] getAlumniList() {
        return alumniList;
    }

    // Method to get sum of all donations received by all alumni
    public double totalDonations() {
        double sum = 0;
        for (Alumnus alumnus : alumniList) sum += alumnus.getDonations();
        return sum;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Alumnus alumni : alumniList) {
            dos.writeUTF("Alumnus:");
            dos.writeUTF(alumni.getName());
            dos.writeInt(alumni.getGraduationYear());
            dos.writeBoolean(alumni.isVIP());
            dos.writeDouble(alumni.getDonations());
            dos.writeUTF(alumni.getContactInfo());
            dos.writeInt(alumni.getAwards().length);
            for (String award : alumni.getAwards()) dos.writeUTF(award);
        }
        dos.close();
    }

    public void add(Alumnus alumni) {
        Alumnus[] tempList = alumniList;
        alumniList = new Alumnus[tempList.length + 1];
        System.arraycopy(tempList, 0, alumniList, 0, tempList.length);
        alumniList[alumniList.length - 1] = alumni;
    }

    public void remove(int index) {
        Alumnus[] bufferArray = new Alumnus[alumniList.length - 1];
        int bufferIndex = 0;
        for (int i = 0; i < alumniList.length; i++) {
            if (i != index) {
                bufferArray[bufferIndex] = alumniList[i];
                bufferIndex++;
            }
        }
        alumniList = bufferArray;
    }

    public void read(File fl) throws IOException {
        fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        alumniList = new Alumnus[0];
        try {
            while (true) {
                if (dis.readUTF().equals("Alumnus:")) {
                    String name = dis.readUTF();
                    int gradYear = dis.readInt();
                    boolean VIP = dis.readBoolean();
                    double donations = dis.readDouble();
                    String contact = dis.readUTF();
                    int awardCount = dis.readInt();
                    Alumnus alumni = new Alumnus(name, gradYear, contact);
                    alumni.setVIP(VIP);
                    alumni.setDonations(donations);
                    for (int i = 0; i < awardCount; i++)
                        alumni.addAward(dis.readUTF());
                    add(alumni);
                }
            }
        } catch (EOFException ignored) {
        }
        dis.close();
    }
}

class test {
    static Scanner scanner = new Scanner(System.in);
    static AlumniManagementSystem alumniSystem = new AlumniManagementSystem();
    static File fl = new File("alumni.db");

    static void printOption(int selNumber, String option) {
        System.out.println("[" + selNumber + "] " + option);
    }

    static void failedAction() {
        System.out.println("The action you selected failed, there are no Alumni present in the data base, please add atleast one Alumnus before trying again");
        main(new String[0]);
    }

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n================ " + header + " ================");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again or press Ctrl+D to exit");
            boolean hasExitFirst = options[0].equalsIgnoreCase("Exit");
            for (int i = 0; i < options.length; i++) {
                if (hasExitFirst && i == 0) continue;
                printOption(i, options[i]);
            }
            if (hasExitFirst) printOption(0, options[0]);
            System.out.print("Select an option to proceed:");
            choice = scanner.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        for (int i = 0; i < ("================ " + header + " ================").length(); i++) System.out.print("=");
        System.out.println();
        return choice;
    }

    public static void main(String[] args) {
        String[] optionsList = getOptions();
        try {
            alumniSystem.read(fl);
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't read file");
            System.exit(0);
        }
        int choice = menu("Alumni Management System Menu", optionsList);
        scanner.nextLine();
        try {
            switch (choice) {
                case 1: {
                    System.out.print("Enter Alumnus Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Graduation Year: ");
                    int graduationYear = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Contact Info: ");
                    String contactInfo = scanner.nextLine();
                    Alumnus alumnus = new Alumnus(name, graduationYear, contactInfo);
                    alumniSystem.addAlumnus(alumnus);
                    System.out.println("Alumnus added successfully.");
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 2: {
                    System.out.print("Enter Alumnus current name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new name for Alumnus: ");
                    alumniSystem.renameAlumnus(name, scanner.nextLine());
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 3: {
                    System.out.print("Enter Alumnus Name to Delete: ");
                    String nameToDelete = scanner.nextLine();
                    alumniSystem.deleteAlumnus(nameToDelete);
                    System.out.println("Alumnus deleted successfully.");
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 4: {
                    System.out.print("Enter Alumnus Name to Update Contact Info: ");
                    String nameToUpdate = scanner.nextLine();
                    System.out.print("Enter New Contact Info: ");
                    String newContactInfo = scanner.nextLine();
                    alumniSystem.updateContactInfo(nameToUpdate, newContactInfo);
                    System.out.println("Contact Info updated successfully.");
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 5: {
                    System.out.println("Enter Alumnus name to update donations: ");
                    String nameToUpdate = scanner.nextLine();
                    System.out.println("Enter new donation amount:");
                    double donationAmount = scanner.nextDouble();
                    scanner.nextLine();
                    alumniSystem.updateDonation(nameToUpdate, donationAmount);
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 6: {
                    System.out.println("Enter Alumnus name to update graduation year: ");
                    String nameToUpdate = scanner.nextLine();
                    System.out.println("Enter new graduation year: ");
                    alumniSystem.updateGradYear(nameToUpdate, scanner.nextInt());
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 7: {
                    System.out.print("Enter Alumnus Name to Add Donation: ");
                    String nameToDonate = scanner.nextLine();
                    System.out.print("Enter Donation Amount: ");
                    double donationAmount = scanner.nextDouble();
                    scanner.nextLine();
                    alumniSystem.addDonation(nameToDonate, donationAmount);
                    System.out.println("Donation added successfully.");
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 8: {
                    double totalDonations = alumniSystem.totalDonations();
                    System.out.println("Total Donations: " + totalDonations);
                    main(new String[0]);
                    break;
                }
                case 9: {
                    System.out.print("Enter Alumnus Name to Mark as VIP: ");
                    String nameToMarkVIP = scanner.nextLine();
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getName().equalsIgnoreCase(nameToMarkVIP)) {
                            alumni.markAsVIP();
                            System.out.println(nameToMarkVIP + " marked as VIP.");
                            break;
                        }
                    }
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 10: {
                    System.out.print("Enter Alumnus Name to Unmark as VIP: ");
                    String nameToUnMarkVIP = scanner.nextLine();
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getName().equalsIgnoreCase(nameToUnMarkVIP)) {
                            alumni.setVIP(false);
                            System.out.println(nameToUnMarkVIP + " unmarked as VIP.");
                            break;
                        }
                    }
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 11: {
                    System.out.print("Enter Alumnus Name to Add an Award: ");
                    String nameToAddAward = scanner.nextLine();
                    System.out.print("Enter Award: ");
                    String award = scanner.nextLine();
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getName().equalsIgnoreCase(nameToAddAward)) {
                            alumni.addAward(award);
                            System.out.println("Award added for " + nameToAddAward);
                            break;
                        }
                    }
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    main(new String[0]);
                    break;
                }
                case 12: {
                    System.out.println("Enter Alumnus Name to Display Contact Info: ");
                    String nameToDisplay = scanner.nextLine();
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getName().equalsIgnoreCase(nameToDisplay)) {
                            System.out.println("Contact info for " + alumni.getName() + ": " + alumni.getContactInfo());
                        }
                    }
                    main(new String[0]);
                    break;
                }
                case 13: {
                    System.out.print("Enter Alumnus Name to Display Awards: ");
                    String nameToDisplayAwards = scanner.nextLine();
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getName().equalsIgnoreCase(nameToDisplayAwards)) {
                            alumni.displayAwards();
                            break;
                        }
                    }
                    main(new String[0]);
                    break;
                }
                case 14: {
                    System.out.print("Enter Graduation Year to Display Alumni: ");
                    int graduationYearToDisplay = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Alumni for Graduation Year " + graduationYearToDisplay + ":");
                    for (Alumnus alumni : alumniSystem.getAlumniList()) {
                        if (alumni.getGraduationYear() == graduationYearToDisplay) {
                            System.out.println(alumni.getName());
                        }
                    }
                    main(new String[0]);
                    break;
                }
                case 15: {
                    alumniSystem.displayAllAlumni();
                    main(new String[0]);
                    break;
                }
                case 0: {
                    System.out.println("Exiting Alumni Management System. Goodbye!");
                    try {
                        alumniSystem.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes to file, try again later");
                    }
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (ClassCastException e) {
            failedAction();
        }
    }

    private static String[] getOptions() {
        return new String[]{"Exit", "Add Alumnus", "Rename Alumnus", "Delete Alumnus", "Update Contact Info", "Update Donation", "Update Graduation Year", "Add Donation", "Total Donations", "Mark as VIP", "Unmark as VIP", "Add award", "Display Contact Info for Alumnus", "Display Awards for Alumnus", "Display Alumnus by Graduation Year", "Display All Alumni"};
    }
}
