import java.util.*;

public class DaiDiGame {

    private static final int NUM_PLAYERS = 4;
    
    public static void main(String[] args) {
        // Create and shuffle deck
        List<String> deck = createDeck();
        Collections.shuffle(deck);
        
        // Distribute cards to players
        List<List<String>> playersHands = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYERS; i++) {
            List<String> hand = new ArrayList<>();
            for (int j = i * (deck.size() / NUM_PLAYERS); j < (i + 1) * (deck.size() / NUM_PLAYERS); j++) {
                hand.add(deck.get(j));
            }
            playersHands.add(hand);
        }
        
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
    
    // Function to create a shuffled deck of cards
    private static List<String> createDeck() {
        List<String> deck = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(rank + " of " + suit);
            }
        }
        return deck;
    }
    
    // Function to find the player with the 3 of diamonds to start the round
    private static int findStartingPlayer(List<List<String>> playersHands) {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (playersHands.get(i).contains("3 of Diamonds")) {
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
        // Implement turn logic here
        // For simplicity, let's assume players just play single cards for now
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
}
