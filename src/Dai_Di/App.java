<<<<<<< Updated upstream:src/Dai_Di/App.java
package Dai_Di;

import Dai_Di.Game.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class App {
=======
package src;
import java.util.Scanner;
import java.util.InputMismatchException;

class App {
>>>>>>> Stashed changes:src/App.java
    private static final Scanner scanner = new Scanner(System.in);
    private final Game game;
    private final MusicPlayer musicPlayer;

    public App(Game game, MusicPlayer musicPlayer) {
        this.game = game;
        this.musicPlayer = musicPlayer;
    }

    public void run() {
        System.out.println("Welcome to Dai Di!");

<<<<<<< Updated upstream:src/Dai_Di/App.java
        // Prompt the user to enter their name (up to 16 characters)
=======
        musicPlayer.playMusic("Music/music.wav");

>>>>>>> Stashed changes:src/App.java
        String playerName;
        do {
            System.out.print("Enter your name (up to 16 characters): ");
            playerName = scanner.nextLine();

            if (playerName.length() > 16) {
                System.out.println("Enter a shorter name!");
            }
        } while (playerName.length() > 16);

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Start game");
            System.out.println("2. Read instructions");
            System.out.println("3. Quit game");
            System.out.print("\nChoose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        game.startGame(playerName, scanner);
                        return;
                    case 2:
                        System.out.println("\nInstructions:");
                        Instructions.displayInstructions();
                        break;
                    case 3:
                        System.out.println("Bye Bye!");
<<<<<<< Updated upstream:src/Dai_Di/App.java
                        return; // Exit the program
=======
                        musicPlayer.stopMusic();
                        return;
>>>>>>> Stashed changes:src/App.java
                    default:
                        System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}
