package Dai_Di.Cards;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Dai_Di.Players.Player;

/**
 * The Deck class represents a standard deck of playing cards.
 */

public class Deck {
    private ArrayList<Card> deck;

     /**
     * Constructs a new deck by building it from scratch.
     */
    public Deck() {
        this.deck = buildDeck();
    }

    /**
     * Builds a new deck of 52 cards, one for each combination of suit and rank.
     * 
     * @return The newly constructed deck.
     */
    public ArrayList<Card> buildDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        // Create a deck of 52 cards, one for each combination of suit and rank
        for (Card.Rank rank : Card.Rank.values()) {
                for (Card.Suit suit : Card.Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }
    

   /**
     * Shuffles the cards in the deck, randomly permuting their order.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Distributes cards from the deck to the specified list of players, ensuring
     * each player receives the same number of cards.
     * 
     * @param players          The list of players to distribute cards to.
     * @param startCardsPerPlayer The number of cards each player should receive.
     * @return True if card distribution was successful, false otherwise.
     */

    public boolean distributeCards(List<Player> players, int startCardsPerPlayer) {
        if (players==null || startCardsPerPlayer<1){
            return false;
        }

        // Distribute cards to each player
        for (int i = 0; i < startCardsPerPlayer; i++) {
            for (Player player : players) {
                player.receiveCard((deck.remove(0))); // Remove card from deck and add to player's hand
            }
        }

        return true;
    }

     /**
     * Returns a string representation of the deck.
     * 
     * @return A string containing all the cards in the deck.
     */
    @Override
    public String toString() {
        String result = "Deck: [";
        for (int i = 0; i < deck.size(); i++) {
            result += deck.get(i);
            if (i < deck.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}
