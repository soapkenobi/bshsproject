import java.io.*;
import java.util.*;

class Player {
    private String name;
    private int roundsPlayed;
    private int score;

    public Player(String name) {
        this.name = name;
        roundsPlayed = 0;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public int getRoundCount() {
        return roundsPlayed;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoundCount(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void addRound() {
        roundsPlayed++;
    }
}

class PlayerManager {
    private Player[] players;

    public PlayerManager() {
        players = new Player[0];
    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        add(player);
    }

    public void deletePlayer(String name) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getName().equalsIgnoreCase(name)) {
                remove(i);
                break;
            }
        }
    }

    public void add(Player player) {
        Player[] tempList = players;
        players = new Player[tempList.length + 1];
        System.arraycopy(tempList, 0, players, 0, tempList.length);
        players[players.length - 1] = player;
    }

    public void remove(int index) {
        Player[] bufferArray = new Player[players.length - 1];
        int bufferIndex = 0;
        for (int i = 0; i < players.length; i++) {
            if (i != index) {
                bufferArray[bufferIndex] = players[i];
                bufferIndex++;
            }
        }
        players = bufferArray;
    }

    public void write(File fl) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        for (Player player : players) {
            dos.writeUTF(player.getName());
            dos.writeInt(player.getRoundCount());
            dos.writeInt(player.getScore());
        }
        dos.close();
    }

    public void read(File fl) throws IOException {
        fl.createNewFile();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        players = new Player[0];
        while (true) {
            try {
                String name = dis.readUTF();
                int roundCount = dis.readInt();
                int score = dis.readInt();
                Player player = new Player(name);
                player.setRoundCount(roundCount);
                player.setScore(score);
                add(player);
            } catch (EOFException e){
                break;
            }
        }
        dis.close();
    }

    public boolean isPlayer(String name) {
        for (Player player : players) if (player.getName().equalsIgnoreCase(name)) return true;
        return true;
    }

    public int getPlayerIndex(String name) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getName().equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void displayAllPlayers() {
        for (int i = 0; i < players.length; i++)
            System.out.println("<" + i + "> " + players[i].getName());
        if (players.length == 0)
            System.out.println("No players found...");
    }

    public void displayPlayerCard(String name) {
        for (Player player : players)
            if (player.getName().equalsIgnoreCase(name)) {
                System.out.println("Name: " + player.getName());
                System.out.println("Score: " + player.getScore());
                System.out.println("Rounds Played: " + player.getRoundCount());
                System.out.println("Rounds Won: " + (player.getScore() / 100));
                System.out.println();
            }
    }
}

class Game {
    private String[] wordList;
    private PlayerManager manager;
    private Scanner sc;

    public Game(String[] wordList, PlayerManager manager, Scanner sc) {
        this.sc = sc;
        this.wordList = wordList;
        this.manager = manager;
    }

    public void play() {
        int player = -1;
        int c = 0;
        do {
            if (c > 0) System.out.println("No such player found, try again...");
            System.out.println("Who's playing?");
            String name = sc.nextLine();
            for (int i = 0; i < manager.getPlayers().length; i++)
                if (manager.getPlayers()[i].getName().equalsIgnoreCase(name)) player = i;
            c++;
        } while (player == -1);
        manager.getPlayers()[player].addRound();
        char[] word = wordList[(int) (Math.random() * wordList.length)].toCharArray();
        char[] guessedWord = new char[word.length];
        for (int i = 0; i < guessedWord.length; i++) guessedWord[i] = '_';
        int lives = 0;
        do {
            if (arrayEqual(word, guessedWord)) break;
            System.out.print("Guessed word:");
            for (char ch : guessedWord) System.out.print(ch + " ");
            System.out.println("\nGuess a letter: ");
            char guess = sc.next().charAt(0);
            if (arrayContains(guessedWord, guess)) {
                System.out.println("You've already guessed this letter");
                continue;
            }
            for (int i = 0; i < word.length; i++) {
                if (word[i] == guess) {
                    guessedWord[i] = guess;
                    lives++;
                }
            }
        } while (lives <= 5);
        if (arrayEqual(word, guessedWord)) {
            manager.getPlayers()[player].addScore(100);
            System.out.println("Congrats! You've been awards 100 score points");
        } else System.out.println("Oops! You ran out of lives.");
    }

    boolean arrayEqual(char[] array1, char[] array2) {
        if (array1.length == array2.length) {
            boolean equal = true;
            for (int i = 0; i < array1.length; i++) {
                if (array1[i] != array2[i]) {
                    equal = false;
                    break;
                }
            }
            return equal;
        } else return false;
    }

    boolean arrayContains(char[] array, char letter) {
        boolean contains = false;
        for (char letters : array) {
            if (letters == letter) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}

public class test2 {
    static File fl;
    static Scanner sc;
    static PlayerManager playerManager;
    static Game game;

    public static void main(String[] args) throws IOException {
        fl = new File("hangman.dat");
        fl.createNewFile();
        sc = new Scanner(System.in);
        playerManager = new PlayerManager();
        game = new Game(new String[]{"soap", "pc"}, playerManager, sc);
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            try {
                playerManager.read(fl);
            } catch (IOException ignored) {
            }
            int ch = menu("Hangman", getMainMenuOptions());
            switch (ch) {
                case 1 -> {
                    System.out.print("Enter player name: ");
                    String name = sc.nextLine();
                    if (playerManager.isPlayer(name)) {
                        System.out.println("Player with name " + name + " already exists");
                        break;
                    }
                    playerManager.addPlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                }
                case 2 -> {
                    System.out.println("Enter player current name: ");
                    String oldName = sc.nextLine();
                    if (playerManager.isPlayer(oldName)) {
                        System.out.println("Enter new name for player");
                        String newName = sc.nextLine();
                        if (playerManager.isPlayer(newName)) {
                            System.out.println("Player with name " + newName + " already exists");
                        } else {
                            playerManager.getPlayers()[playerManager.getPlayerIndex(newName)].setName(newName);
                        }
                    } else {
                        System.out.println("No such player found");
                    }
                }
                case 3 -> {
                    System.out.println("Enter player name: ");
                    String name = sc.nextLine();
                    playerManager.deletePlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                }
                case 4 -> {
                    game.play();
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save game data");
                    }
                }
                case 5 -> {
                    System.out.print("Enter player name to reset: ");
                    String name = sc.nextLine();
                    playerManager.deletePlayer(name);
                    playerManager.addPlayer(name);
                    try {
                        playerManager.write(fl);
                    } catch (IOException e) {
                        System.out.println("Couldn't save changes");
                    }
                }
                case 6 -> playerManager.displayAllPlayers();
                case 7 -> {
                    System.out.print("Enter player name to view card: ");
                    String name = sc.nextLine();
                    playerManager.displayPlayerCard(name);
                }
                default -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        }
    }

    static int menu(String header, String[] options) {
        int choice;
        int c = 0;
        System.out.println("\n================= * " + header + " * =================");
        do {
            if (c > 0) System.out.println("\nInvalid choice, try again");
            boolean hasExitOrGoBack = options[0].equalsIgnoreCase("Exit") || options[0].equalsIgnoreCase("Go Back");
            for (int i = 0; i < options.length; i++) {
                if (hasExitOrGoBack && i == 0) continue;
                printOptions(("================= * " + header + " * =================").length(), i, options[i]);
            }
            System.out.println();
            if (hasExitOrGoBack)
                printOptions(("================= * " + header + " * =================").length(), 0, options[0]);
            System.out.println();
            System.out.print("Select an option to proceed: ");
            choice = sc.nextInt();
            c++;
        } while (choice < 0 || choice >= options.length);
        System.out.println("=".repeat(("================= * " + header + " * =================").length()));
        sc.nextLine();
        return choice;
    }

    static void printOptions(int heaaderLength, int opNumber, String option) {
        String space = "  ";
        if (opNumber > 9) space = " ";
        System.out.println(" ".repeat(heaaderLength / 6) + opNumber + "." + space + option);
    }

    static String[] getMainMenuOptions() {
        String[] op = new String[8];
        op[0] = "Exit";
        op[1] = "Add Player";
        op[2] = "Rename Player";
        op[3] = "Remove Player";
        op[4] = "Play";
        op[5] = "Reset Player Statistics";
        op[6] = "View All Players";
        op[7] = "View Player Card";
        return op;
    }
}
