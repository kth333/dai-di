package src;
import java.util.*;
/**
 * Represents a hand of cards for a player.
 */

public class Hand {
    private List<Card> hand;
    /**
     * Constructs an empty hand.
     */
    public Hand() {
        this.hand = new ArrayList<Card>();
    }
    /**
     * Adds a card to the hand.
     *
     * @param card the card to add to the hand
     */
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

     /**
     * Removes cards that have been played from the hand.
     *
     * @param playedCards the cards that have been played and need to be removed from the hand
     */
    public void removeCards(PlayedCards playedCards) {
        if (playedCards != null) {
            hand.removeAll(playedCards.getCards());
        }
    }

    /**
     * Clears the hand, removing all cards.
     */
    public void clear() {
        hand.clear();
    }

    /**
     * Checks if the hand is empty.
     *
     * @return true if the hand is empty, false otherwise
     */
    public boolean isEmpty() {
        return hand.isEmpty();
    }

    /**
     * Returns the size of the hand.
     *
     * @return the number of cards in the hand
     */
    public int getSize() {
        return hand.size();
    }

    /**
     * Returns a copy of the hand to avoid modification from outside.
     *
     * @return a copy of the hand
     */
    public List<Card> getCards() {
        List<Card> handCopy = new ArrayList<Card>();
        for (int i = 0; i < hand.size(); i++) {
            handCopy.add(hand.get(i));
        }
        return handCopy;
    }

    /**
     * Sorts the hand by rank.
     */

    public void sortByRank() {
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.getRank().compareTo(c2.getRank());
            }
        });
    }

     /**
     * Sorts the hand by suit and then by rank.
     */
    public void sortBySuit() {
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                int suitComparison = c1.getSuit().compareTo(c2.getSuit());
                if (suitComparison == 0) {
                    return c1.getRank().compareTo(c2.getRank());
                }
                return suitComparison;
            }
        });
    }

    /**
     * Returns a string representation of the player's hand.
     *
     * @return A string representing the player's hand in the format: [index. card1, index. card2, ..., index. cardN]
     *         where index is the position of the card in the hand.
     */
    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < hand.size(); i++) {
            result += i + ". " + hand.get(i);
            if (i < hand.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}
