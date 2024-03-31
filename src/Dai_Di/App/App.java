package Dai_Di.App;

import java.util.Scanner;
import Dai_Di.Game.Game;
import Dai_Di.Game.GameView;
import Dai_Di.Music.MusicPlayer;
import Dai_Di.Instructions.Instructions;

/**
 * The main application class for running the Dai Di game.
 */
public class App {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final Game GAME;

    /**
     * Constructs an instance of the App class with a specified Game object.
     * 
     * @param GAME the Game object to associate with this application
     */
    public App(Game GAME) {
        this.GAME = GAME;
    }

    /**
     * Runs the Dai Di game application.
     */
    public void run() {
        System.out.println("\nWelcome to Dai Di!");

        // Start playing music
        MusicPlayer.playMusic();

        String playerName = GameView.getName("Enter your name (up to 16 characters): ", SCANNER);

        while (true) {
            // Display the main menu options
            System.out.println("\nOptions:");
            System.out.println("1. Start game");
            System.out.println("2. Read instructions");
            System.out.println("3. Stop or Play music");
            System.out.println("4. Quit game");
            // Read the user's choice
            int choice = GameView.getInt("\nChoose an option: ",SCANNER);
            switch (choice) {
                case 1:
                    // Start the game with the entered player name
                    GAME.startGame(playerName, SCANNER);
                    return;
                case 2:
                    // Display game instructions
                    System.out.println("\nInstructions:");
                    Instructions.displayInstructions();
                    break;
                case 3:
                    // Stop music if music is playing, play music if music is stopped
                    MusicPlayer.playOrStopMusic();
                    break;
                case 4:
                    // Exit the game
                    System.out.println("Bye Bye!");
                    MusicPlayer.stopMusic();
                    return;
                default:
                    // Handle invalid input
                    System.out.println("Invalid choice! Please enter 1, 2, 3 or 4.");
                    break;
            }
        }
    }
}