<<<<<<< Updated upstream:src/Dai_Di/Cards/Deck.java
package Dai_Di.Cards;

=======
package src;
>>>>>>> Stashed changes:src/Deck.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Dai_Di.Players.Player;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        this.deck = buildDeck();
    }

    public ArrayList<Card> buildDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        // Create a deck of 52 cards, one for each combination of suit and rank
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    // Collections method shuffle randomly permutes specified list using specified
    // source of randomness
    public void shuffle() {
        Collections.shuffle(deck);
    }

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