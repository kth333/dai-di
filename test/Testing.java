import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;

public class Testing {

    private static final int NUM_PLAYERS = 4;
    private static final int startCardsPerPlayer = 13;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Give the user a warm welcome :)
        System.out.println("Welcome to Dai Di!");

        // Prompt the user to enter their name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        // Create the human player with the entered name
        Player humanPlayer = new Player(playerName);

        // Create three bot players with predefined names
        Player bot1 = new Player("Bot 1");
        Player bot2 = new Player("Bot 2");
        Player bot3 = new Player("Bot 3");

        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        startGame(humanPlayer, bot1, bot2, bot3);
                        return; // Exit the loop and terminate the program
                    case 2:
                        // Instructions.displayInstructions();
                        break; // Exit the loop and terminate the program
                    case 3:
                        // quitGame();
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

    private static void startGame(Player humanPlayer, Player bot1, Player bot2, Player bot3) {
        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println(deck);
        System.out.println(bot1.getName());
    }
}
// // Distribute cards to players
// List<Player> players = Arrays.asList(humanPlayer, bot1, bot2, bot3);
// Map<Player, List<Card>> playersHands = deck.distributeCards(players,
// startCardsPerPlayer);

// // Determine the player with the 3 of diamonds to start the round
// Player startingPlayer = findStartingPlayer(players);
// System.out.println(startingPlayer.getName() + " starts the round!");

// Player winner = null;
// // Game loop
// Player currentPlayer = startingPlayer;
// while (winner == null) {
// if (currentPlayer.getPlayerNumber() == 0) {
// // Human player's turn
// playTurn(humanPlayer, playersHands);
// } else {
// // Bot player's turn
// Player botPlayer = getPlayer(currentPlayer.getPlayerNumber(), bot1, bot2,
// bot3);
// playTurn(botPlayer, playersHands);
// }
// winner = findWinner(playersHands);
// int nextPlayerIndex = (currentPlayer.getPlayerNumber() + 1) % NUM_PLAYERS;
// currentPlayer = players.get(nextPlayerIndex);
// }

// // Determine winner
// System.out.println(winner.getName() + " wins!");
// }

// private static Player getPlayer(int playerNumber, Player bot1, Player bot2,
// Player bot3) {
// switch (playerNumber) {
// case 1:
// return bot1;
// case 2:
// return bot2;
// case 3:
// return bot3;
// default:
// return null;
// }
// }

// private static Player findStartingPlayer(List<Player> players) {
// for (Player player : players) {
// Hand hand = player.getHand();
// if (hand != null) { // Check if the hand is not null
// for (int i = 0; i < hand.getSize(); i++) {
// Card card = hand.getCard(i);
// if (card.getSuit() == Card.Suit.DIAMONDS && card.getRank() ==
// Card.Rank.THREE) {
// return player;
// }
// }
// }
// }
// return null; // 3 of Diamonds not found
// }
// }

private static void humanTurn(Player currentPlayer, Map<Player, List<Card>> playersHands, PlayedCards previousCards) {
    List<Card> hand = playersHands.get(currentPlayer);

    while (true) {
        System.out.print("Select cards to play (enter indices separated by spaces) or type p to pass: ");
        String input = scanner.nextLine();

        if (input.toLowerCase().equals("p")) {
            System.out.println(currentPlayer.getName() + " passed their turn.");
            previousCards = null; // Reset previous cards if the player passes
            return; // Exit the method if the player chooses to pass
        }

        String[] indices = input.split("\\s+");
        List<Integer> selectedIndices = new ArrayList<>();
        for (String index : indices) {
            try {
                selectedIndices.add(Integer.parseInt(index));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter indices separated by spaces.");
                break; // Continue to prompt the player for valid input
            }
        }

        currentPlayer.play(selectedIndices, hand, previousCards);
    }
}

public void play(List<Integer> selectedIndices, List<Card> hand, PlayedCards previousCards) {
    // Validate selected indices
    boolean validSelection = true;
    List<Card> selectedCards = new ArrayList<>();
    for (int index : selectedIndices) {
        if (index < 0 || index >= hand.size()) {
            System.out.println("Invalid index! Please select indices within the range of your hand.");
            validSelection = false;
            break; // Continue to prompt the player for valid input
        }
        selectedCards.add(hand.get(index));
    }

    if (!validSelection) {
        return; // Continue to prompt the player for valid input
    }

    // Create an instance of PlayedCards to store the selected cards
    PlayedCards playedCards = new PlayedCards(this, selectedCards);

    // Check if the selected cards win against the previous cards
    if (previousCards != null && !playedCards.winsAgainst(previousCards)) {
        System.out.println("Invalid selection! The selected cards do not win against the previous cards.");
        return; // Continue to prompt the player for valid input
    }

    // Display the played cards
    System.out.println(this.getName() + " played: " + playedCards.getCards());

    // Remove the played cards from the player's hand
    hand.removeAll(playedCards.getCards());
    previousCards = playedCards;
}