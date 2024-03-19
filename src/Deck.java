import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Player, List<Card>> distributeCards(List<Player> players, int startCardsPerPlayer) {
        Map<Player, List<Card>> playersHands = new HashMap<>();

        // Initialize hands for each player
        for (Player player : players) {
            playersHands.put(player, new ArrayList<>());
        }

        // Distribute cards to each player
        for (int i = 0; i < startCardsPerPlayer; i++) {
            for (Player player : players) {
                playersHands.get(player).add(deck.remove(0)); // Remove card from deck and add to player's hand
            }
        }

        return playersHands;
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