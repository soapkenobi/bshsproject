import java.io.*;
import java.util.Scanner;

public class quizsandbox {
    static File fl = new File("quiz.dat");
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int ch;
        do {
            try {
                new DataInputStream(new FileInputStream(fl)).close();
                System.out.println("To play, enter 1");
                System.out.println("To create quiz, enter 2");
                System.out.println("To exit, press 0");
                ch = sc.nextInt();
            } catch (IOException e) {
                System.out.println("Quiz not found, entering quiz creation");
                ch = 2;
            }
        } while (ch != 2 && ch != 1 && ch != 0);
        try {
            switch (ch) {
                case 0 -> System.exit(0);
                case 1 -> play();
                case 2 -> createQuiz();
            }
        } catch(IOException e) {
            System.out.println("An error occured");
            System.exit(0);
        }
    }

    public static void play() throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        int score = 0;
        try {
            while (true) {
                String q = dis.readUTF();
                String[] op = new String[4];
                for (int i = 0; i < op.length; i++) op[i] = dis.readUTF();
                int correct = dis.readInt();
                System.out.println("Q: " + q);
                for (int i = 0; i < op.length; i++) System.out.println(i + 1 + ". " + op[i]);
                System.out.print("Enter your answer:");
                int answer = sc.nextInt();
                if (answer == correct) score += 10;
            }
        } catch (EOFException e) {
            System.out.println("Your score:" + score);
        }
    }

    public static void createQuiz() throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        boolean continueLoop;
        do {
            System.out.print("Enter question:");
            dos.writeUTF(sc.nextLine());
            System.out.println("Enter 4 options:");
            for (int i = 0; i < 4; i++) dos.writeUTF(sc.nextLine());
            System.out.print("\nEnter serial of correct option:");
            dos.writeInt(sc.nextInt());
            System.out.println("Would you like to add another one?[Y\\N]");
            char chL;
            continueLoop = (chL = sc.next().charAt(0)) == 'y' || chL == 'Y';
            sc.nextLine();
        } while (continueLoop);
    }
}
