package src;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * The main application class for running the Dai Di game.
 */
class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_NAME_LENGTH = 16;
    private final Game game;

    /**
     * Constructs an instance of the App class with a specified Game object.
     * 
     * @param game the Game object to associate with this application
     */
    public App(Game game) {
        this.game = game;
    }

    /**
     * Runs the Dai Di game application.
     */
    public void run() {
        System.out.println("Welcome to Dai Di!");

        // Start playing music
        MusicPlayer.playMusic();

        String playerName;
        do {
            // Prompt the user to enter their name
            System.out.print("Enter your name (up to 16 characters): ");
            playerName = scanner.nextLine();

            // Check if the entered name exceeds the maximum length
            if (playerName.length() > MAX_NAME_LENGTH) {
                System.out.println("Enter a shorter name!");
            }
        } while (playerName.length() > MAX_NAME_LENGTH);

        while (true) {
            // Display the main menu options
            System.out.println("\nOptions:");
            System.out.println("1. Start game");
            System.out.println("2. Read instructions");
            System.out.println("3. Quit game");
            System.out.print("\nChoose an option: ");

            try {
                // Read the user's choice
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        // Start the game with the entered player name
                        game.startGame(playerName, scanner);
                        return;
                    case 2:
                        // Display game instructions
                        System.out.println("\nInstructions:");
                        Instructions.displayInstructions();
                        break;
                    case 3:
                        // Exit the game
                        System.out.println("Bye Bye!");
                        MusicPlayer.stopMusic();
                        return;
                    default:
                        // Handle invalid input
                        System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                        break;
                }
            } catch (InputMismatchException e) {
                // Handle non-integer input
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}