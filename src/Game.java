import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;

public class Game {

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

        // Create three bot players with unique random names
        List<String> usedNames = new ArrayList<>();
        Player bot1 = new Player(humanPlayer.getName(), usedNames);
        Player bot2 = new Player(humanPlayer.getName(), usedNames);
        Player bot3 = new Player(humanPlayer.getName(), usedNames);

        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        startGame(humanPlayer, bot1, bot2, bot3);
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

    private static void startGame(Player humanPlayer, Player bot1, Player bot2, Player bot3) {
        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println("Players: " + humanPlayer.getName() + ", " + bot1.getName() + ", " + bot2.getName() + ", " + bot3.getName());

        // Distribute cards to players
        List<Player> players = Arrays.asList(humanPlayer, bot1, bot2, bot3);
        Map<Player, List<Card>> playersHands = deck.distributeCards(players, startCardsPerPlayer);

        List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);
        displayPlayerOrder(playerOrder); // shit takes forever to run

        // Determine the player with the 3 of diamonds to start the round
        Player startingPlayer = findStartingPlayer(playersHands);
        System.out.println(startingPlayer.getName() + " starts the round!");
    }

        // Player winner = null;
        // // Game loop
        // Player currentPlayer = startingPlayer;
        // while (winner == null) {
        //     if (currentPlayer.equals(humanPlayer)) {
        //         // Human player's turn
        //         // playTurn(humanPlayer, playersHands);
        //         System.out.println("Your turn!");
        //     } else {
        //         // Bot player's turn
        //         // playTurn(botPlayer, playersHands);
        //         System.out.println("bots turn");
        //     }
        //     winner = findWinner(playersHands);
        // }

        // Determine winner
    //     System.out.println(winner.getName() + " wins!");
    // }

    private static Player findStartingPlayer(Map<Player, List<Card>> playersHands) {
        for (Player player : playersHands.keySet()) {
            List<Card> hand = playersHands.get(player);
            if (hand != null) { // Check if the hand is not null
                for (Card card : hand) {
                    if (card.getSuit() == Card.Suit.DIAMONDS && card.getRank() == Card.Rank.THREE) {
                        return player;
                    }
                }
            }
        }
        return null; // 3 of Diamonds not found
    }
    
    public static List<Player> playerOrder(List<Player> playerList, int numPlayers){
        List<Player> playerOrder = new ArrayList<>();

        //populate playerOrder list positions
        for (int i=0; i < numPlayers; i++){
            playerOrder.add(null);
        }

        //Set player Order
        for (int i = 0; i < numPlayers; i++) {
            //Get first player
            Player player = playerList.get(i);
            //Check if has startCard
            if (player.has(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE))) {
                //If have startCard assign as first player
                //Assumes that all 52 cards are dealt out properly
                playerOrder.set(0, player);
            } else {
                //If not set their turn order as one of the other positions
                boolean turnOrderSet=false;
                //Set a do loop until turn order is successfully set
                do {
                    //nextInt(1,numPlayers) will generate random number inclusive 1 and exclusive numPlayers
                    int position = ThreadLocalRandom.current().nextInt(1, numPlayers);
                    //Set the player in the position if it is empty
                    if (playerOrder.get(position)==null) {
                        playerOrder.set(position,player);
                        turnOrderSet=true;
                    }
                } while (!turnOrderSet);
            }
        }
        return playerOrder;
    }

    public static void displayPlayerOrder(List<Player> playerOrder){
        for (int i=0; i < playerOrder.size(); i++){
            Player player = playerOrder.get(i);
            String playerName = null;
            if (player instanceof Player){
                playerName = player.getName();
            } else {
                playerName = "Missing";
            }
            System.out.println("Player "+ i +" is "+ playerName);
        }
    }


    // private static void playTurn(Player player, Map<Player, List<Card>> playersHands) {
    //     System.out.println("Player " + player.getName() + "'s turn:");
    //     List<Card> hand = playersHands.get(player.getPlayerNumber());
    //     if (!hand.isEmpty()) {
    //         // Prompt the user to select cards to play
    //         List<Integer> selectedCardIndices = selectCards(player, hand);
    //         PlayedCards playedCards = player.play(selectedCardIndices);
    //         if (playedCards != null) {
    //             System.out.println("Player " + player.getName() + " plays: " + playedCards);
    //             playersHands.get(player.getPlayerNumber()).removeAll(playedCards.getCards());
    //         } else {
    //             System.out.println("Player " + player.getName() + " has no cards left!");
    //         }
    //     } else {
    //         System.out.println("Player " + player.getName() + " has no cards left!");
    //     }
    // }

    private static List<Integer> selectCards(Player player, List<Card> hand) {
        // Placeholder method for selecting cards to play
        // Here you can implement the logic to prompt the user to select cards
        // For now, let's select the first card
        List<Integer> selectedIndices = new ArrayList<>();
        selectedIndices.add(0);
        return selectedIndices;
    }

    private static Player findWinner(Map<Player, List<Card>> playersHands) {
        for (Map.Entry<Player, List<Card>> entry : playersHands.entrySet()) {
            if (entry.getValue().isEmpty()) {
                return entry.getKey();
            }
        }
        return null; // No winner found
    }
    

    private static void quitGame() {
        System.out.println("Bye Bye!");
    }
}