import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class Game {

    private static final int NUM_PLAYERS = 4;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Dai Di!");
        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        startGame();
                        return; // Exit the loop and terminate the program
                    case 2:
                        Instructions.displayInstructions();
                        break; // Exit the loop and terminate the program
                    case 3:
                        quitGame();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice! Please enter 1, 2 or 3.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Start game");
        System.out.println("2. Read instructions");
        System.out.println("3. Quit game");
        System.out.print("Choose an option: ");
    } 

    private static void startGame() {
        // Create and shuffle deck
        ArrayList<String> deck = buildDeck();
        Collections.shuffle(deck);

        // Distribute cards to players
        List<List<String>> playersHands = new ArrayList<>();
        int cardsPerPlayer = deck.size() / NUM_PLAYERS;
        for (int i = 0; i < NUM_PLAYERS; i++) {
            List<String> hand = new ArrayList<>();
            for (int j = i * cardsPerPlayer; j < (i + 1) * cardsPerPlayer; j++) {
                hand.add(deck.get(j % deck.size()));
            }
            playersHands.add(hand);
        }

        System.out.println("Your Hand: " + player.getHand());

        // Determine the player with the 3 of diamonds to start the round
        int startingPlayer = findStartingPlayer(playersHands);
        System.out.println("Player " + (startingPlayer + 1) + " starts the round!");

        // Game loop
        int currentPlayer = startingPlayer;
        while (!allPlayersEmpty(playersHands)) {
            playTurn(currentPlayer, playersHands);
            currentPlayer = (currentPlayer + 1) % NUM_PLAYERS;
        }

        // Determine winner
        int winner = findWinner(playersHands);
        System.out.println("Player " + (winner + 1) + " wins!");
    }

    public static ArrayList<String> buildDeck() {
        ArrayList<String> deck = new ArrayList<>();
        String[] values = { "a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] types = { "c", "d", "h", "s" };

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                deck.add(values[j] + types[i]);
            }
        }
        return deck;
    }

    // Function to find the player with the 3 of diamonds to start the round
    private static int findStartingPlayer(List<List<String>> playersHands) {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (playersHands.get(i).contains("3d")) {
                return i;
            }
        }
        return -1; // 3 of Diamonds not found
    }

    // Function to check if all players have empty hands
    private static boolean allPlayersEmpty(List<List<String>> playersHands) {
        for (List<String> hand : playersHands) {
            if (!hand.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Function to play a turn for a player
    private static void playTurn(int player, List<List<String>> playersHands) {
        System.out.println("Player " + (player + 1) + "'s turn:");
        List<String> hand = playersHands.get(player);
        if (!hand.isEmpty()) {
            String card = hand.remove(0);
            System.out.println("Player " + (player + 1) + " plays: " + card);
        } else {
            System.out.println("Player " + (player + 1) + " has no cards left!");
        }
    }

    // Function to find the winner (player with empty hand)
    private static int findWinner(List<List<String>> playersHands) {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (playersHands.get(i).isEmpty()) {
                return i;
            }
        }
        return -1; // No winner found
    }

    private static void quitGame() {
        System.out.println("Bye Bye!");
    }
}
