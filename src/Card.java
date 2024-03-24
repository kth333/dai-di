import java.util.Objects;

 /**
 * The Card class represents a playing card with a suit and a rank.
 */
public class Card implements Comparable<Card> {
    
    /**
     * The Suit enum represents the four suits of a standard deck of cards.
     */
    public enum Suit {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }
    /**
     * The Rank enum represents the ranks of cards from three to two and ace.
     */

    public enum Rank {
        THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE, TWO
    }
    private final Suit suit;
    private final Rank rank;
    /**
     * Gets the suit of the card.
     * 
     * @return The suit of the card.
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
    /**
     * Gets the suit of the card.
     * 
     * @return The suit of the card.
     */

    public Suit getSuit() {
        return suit;
    }
    /**
     * Gets the rank of the card.
     * 
     * @return The rank of the card.
     */
    public Rank getRank() {
        return rank;
    }
    /**
     * Returns a string representation of the card, including its rank and suit.
     * 
     * @return A string representing the card.
     */
    public String toString() {
        return rank + " of " + suit;
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param obj The reference object with which to compare.
     * @return True if this card is equal to the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // check if object is same as this object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;// check if object comparison is null or not instance of card class
        }
        Card card = (Card) obj;// cast obj to card
        return suit == card.suit && rank == card.rank;
    }
    
     /* Returns a hash code value for the card.
     * 
     * @return A hash code value for this card.
     */

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
    /**
     * Compares this card with the specified card for order.
     * 
     * @param other The card to be compared.
     * @return A negative integer, zero, or a positive integer as this card is less than, equal to, or greater than
     *         the specified card.
     */

    public int compareTo(Card other) {
        int rankComparison = this.rank.compareTo(other.rank);
        if (rankComparison != 0) {
            return rankComparison;
        }
        return this.suit.compareTo(other.suit);
    }
}
