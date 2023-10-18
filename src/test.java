import java.util.ArrayList;
import java.util.Scanner;

class Alumnus {
    private String name;
    private int graduationYear;
    private String contactInfo;
    private boolean isVIP;
    private double donations;
    private ArrayList<String> awards;

    public Alumnus(String name, int graduationYear, String contactInfo) {
        this.name = name;
        this.graduationYear = graduationYear;
        this.contactInfo = contactInfo;
        this.isVIP = false;
        this.donations = 0;
        this.awards = new ArrayList<>();
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


    // Getter and Setter methods for Alumnus properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public double getDonations() {
        return donations;
    }

    public void setDonations(double donations) {
        this.donations = donations;
    }

    public ArrayList<String> getAwards() {
        return awards;
    }

    public void addAward(String award) {
        awards.add(award);
    }
    public void addDonation(double donation){
        this.donations += donation;
    }
}

class AlumniManagementSystem {
    private ArrayList<Alumnus> alumniList;

    public AlumniManagementSystem() {
        alumniList = new ArrayList<>();
    }

    // Method to add an alumnus to the list
    public void addAlumnus(Alumnus alumnus) {
        alumniList.add(alumnus);
    }

    // Method to delete an alumnus by name
    public void deleteAlumnus(String name) {
        for (Alumnus alumnus : alumniList) {
            if (alumnus.getName().equalsIgnoreCase(name)) {
                alumniList.remove(alumnus);
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
            System.out.println("Awards: " + alumnus.getAwards());
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        AlumniManagementSystem alumniSystem = new AlumniManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Alumni Management System Menu:");
            System.out.println("1. Add Alumnus");
            System.out.println("2. Delete Alumnus");
            System.out.println("3. Update Contact Info");
            System.out.println("4. Add Donation");
            System.out.println("5. Total Donations");
            System.out.println("6. Mark as VIP");
            System.out.println("7. Add Award");
            System.out.println("8. Display Awards for Alumnus");
            System.out.println("9. Display Alumni by Graduation Year");
            System.out.println("10. Display All Alumni");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
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
                    break;
                case 2:
                    System.out.print("Enter Alumnus Name to Delete: ");
                    String nameToDelete = scanner.nextLine();
                    alumniSystem.deleteAlumnus(nameToDelete);
                    System.out.println("Alumnus deleted successfully.");
                    break;
                case 3:
                    System.out.print("Enter Alumnus Name to Update Contact Info: ");
                    String nameToUpdate = scanner.nextLine();
                    System.out.print("Enter New Contact Info: ");
                    String newContactInfo = scanner.nextLine();
                    alumniSystem.updateContactInfo(nameToUpdate, newContactInfo);
                    System.out.println("Contact Info updated successfully.");
                    break;
                case 4:
                    System.out.print("Enter Alumnus Name to Add Donation: ");
                    String nameToDonate = scanner.nextLine();
                    System.out.print("Enter Donation Amount: ");
                    double donationAmount = scanner.nextDouble();
                    scanner.nextLine();
                    alumniSystem.addDonation(nameToDonate, donationAmount);
                    System.out.println("Donation added successfully.");
                    break;
                case 5:
                    double totalDonations = alumniSystem.totalDonations();
                    System.out.println("Total Donations: " + totalDonations);
                    break;
                case 6:
                    System.out.print("Enter Alumnus Name to Mark as VIP: ");
                    String nameToMarkVIP = scanner.nextLine();
                    for (Alumnus alumnus : alumniSystem.getAlumniList()) {
                        if (alumnus.getName().equalsIgnoreCase(nameToMarkVIP)) {
                            alumnus.markAsVIP();
                            System.out.println(nameToMarkVIP + " marked as VIP.");
                            break;
                        }
                    }
                    break;

                case 7:
                    System.out.print("Enter Alumnus Name to Add an Award: ");
                    String nameToAddAward = scanner.nextLine();
                    System.out.print("Enter Award: ");
                    String award = scanner.nextLine();
                    for (Alumnus alumnus : alumniSystem.getAlumniList()) {
                        if (alumnus.getName().equalsIgnoreCase(nameToAddAward)) {
                            alumnus.addAward(award);
                            System.out.println("Award added for " + nameToAddAward);
                            break;
                        }
                    }
                    break;
                case 8:
                    System.out.print("Enter Alumnus Name to Display Awards: ");
                    String nameToDisplayAwards = scanner.nextLine();
                    for (Alumnus alumnus : alumniSystem.getAlumniList()) {
                        if (alumnus.getName().equalsIgnoreCase(nameToDisplayAwards)) {
                            alumnus.displayAwards();
                            break;
                        }
                    }
                    break;
                case 9:
                    System.out.print("Enter Graduation Year to Display Alumni: ");
                    int graduationYearToDisplay = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Alumni for Graduation Year " + graduationYearToDisplay + ":");
                    for (Alumnus alumnus : alumniSystem.getAlumniList()) {
                        if (alumnus.getGraduationYear() == graduationYearToDisplay) {
                            System.out.println(alumnus.getName());
                        }
                    }
                    break;

                case 10:
                    alumniSystem.displayAllAlumni();
                    break;
                case 0:
                    System.out.println("Exiting Alumni Management System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}