import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Represents a set of cards played by a player in a game.
 */

public class PlayedCards {
    private Player player;
    private List<Card> cards;

     /**
     * Default constructor for PlayedCards.
     * Initializes the player as null and creates an empty list of cards.
     */
    public PlayedCards() {
        this.player = null;
        this.cards = new ArrayList<>();
    }
     /**
     * Constructs a PlayedCards object with the given player and list of cards.
     *
     * @param player the player who played the cards
     * @param cards the list of cards played
     */

    public PlayedCards(Player player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    /**
     * Adds a card to the set of played cards.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

     /**
     * Gets the highest ranked card in the set of played cards.
     *
     * @return the highest ranked card
     */
    public Card getHighestCard() {
        sortByRank();
        return this.cards.get(0);
    }
    
    /**
     * Retrieves the player who played the cards.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }
    /**
     * Gets the number of cards in the set of played cards.
     *
     * @return the number of cards
     */
    public int getNumOfCards() {
        return this.cards.size();
    }
    /**
     * Retrieves the list of cards in the set of played cards.
     *
     * @return the list of cards
     */
    public List<Card> getCards() {
        return this.cards;
    }
    /**
     * Checks if the size of the set of played cards is valid for the game rules.
     * Valid sizes are 1, 2, 3, or 5 cards.
     *
     * @return true if the size is valid, false otherwise
     */
    public boolean isValidSize() {
        int size = this.cards.size();
        return size == 1 || size == 2 || size == 3 || size == 5;
    }
    
    /**
     * Determines if the set of played cards wins against another set of played cards.
     * This method compares the sets of cards based on game rules, such as the combination rankings
     * and the values of the cards themselves.
     *
     * @param other the other set of played cards to compare against
     * @return true if this set of cards wins, false otherwise
     */

    public boolean winsAgainst(PlayedCards other) {
        // Always wins against null (pass)
        if (other == null) {
            return true;
        }

        int thisSize = this.getNumOfCards();
        int otherSize = other.getNumOfCards();

        // Compare the number of cards in each set
        if (thisSize != otherSize) {
            return false; // If the number of cards is different, they cannot win against each other
                          // (invalid)
        } else {
            if (thisSize == 5) {
                // Get the combination rankings for both hands
                HandValidator.CombinationRanking thisRanking = HandValidator.getCombinationRanking(this);
                HandValidator.CombinationRanking otherRanking = HandValidator.getCombinationRanking(other);

                // Compare the combination rankings
                int rankingComparison = thisRanking.compareTo(otherRanking);
                if (rankingComparison > 0) {
                    return true;
                } else if (rankingComparison == 0) {
                    // If the rankings are equal, compare the highest cards
                    int rankComparison = this.getHighestCard().compareTo(other.getHighestCard());
                    if (rankComparison > 0) {
                        return true;
                    } else if (rankComparison == 0) {
                        // If ranks are equal, compare the suits
                        int suitComparison = this.getHighestCard().getSuit()
                                .compareTo(other.getHighestCard().getSuit());
                        if (suitComparison > 0) {
                            return true;
                        }
                    }
                }
            } else {
                // If the number of cards is not five, compare based on the highest card
                int rankComparison = this.getHighestCard().compareTo(other.getHighestCard());
                if (rankComparison > 0) {
                    return true;
                } else if (rankComparison == 0) {
                    // If ranks are equal, compare the suits
                    int suitComparison = this.getHighestCard().getSuit().compareTo(other.getHighestCard().getSuit());
                    if (suitComparison > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Sorts the cards in the set of played cards by rank.
     */
    public void sortByRank() {
        Collections.sort(cards, new RankComparator());
    }
      /**
     * Determines the type of combination in the set of played cards.
     * This method identifies the combination of cards played, such as Single, Pair, Three of a Kind, etc.
     *
     * @return the type of combination as a string
     */
    public String getType() {
        int size = this.getNumOfCards();
        switch (size) {
            case 1:
                return "Single";
            case 2:
                if (HandValidator.isDouble(this)) {
                    return "Pair";
                } else {
                    return "Invalid";
                }
            case 3:
                if (HandValidator.isTriple(this)) {
                    return "Three of a Kind";
                } else {
                    return "Invalid";
                }
            case 5:
                if (HandValidator.isStraightFlush(this)) {
                    return "Straight Flush";
                } else if (HandValidator.isFourOfAKind(this)) {
                    return "Four of a Kind";
                } else if (HandValidator.isFullHouse(this)) {
                    return "Full House";
                } else if (HandValidator.isFlush(this)) {
                    return "Flush";
                } else if (HandValidator.isStraight(this)) {
                    return "Straight";
                }
                return "Invalid";
            default:
                return "Invalid";
        }
    }
    /**
     * Returns a string representation of the set of played cards.
     *
     * @return a string representation of the set of played cards
     */
    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "[]"; // Return empty brackets if the list is empty
        }

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            result.append(cards.get(i));
            if (i < cards.size() - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}
