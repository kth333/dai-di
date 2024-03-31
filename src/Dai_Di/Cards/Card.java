package Dai_Di.Cards;

import java.util.Objects;

/**
 * The Card class represents a playing card with a suit and a rank.
 */
public class Card implements Comparable<Card> {
    
    /**
     * The Rank enum represents the ranks of cards from three to two and ace.
     */
    public enum Rank {
        THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE, TWO
    }

    /**
     * The Suit enum represents the four suits of a standard deck of cards.
     */
    public enum Suit {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }


    private final Rank RANK;
    private final Suit SUIT;
    public static final Card START_CARD = new Card(Rank.THREE, Suit.DIAMONDS); // Start card is always three of diamonds

    /**
     * Constructs a new Card object with the specified suit and rank.
     *
     * @param suit the suit of the card
     * @param rank the rank of the card
     */
    public Card(Rank rank, Suit suit) {
        this.RANK = rank;
        this.SUIT = suit;
    }

    /**
     * Gets the suit of the card.
     * 
     * @return The suit of the card.
     */
    public Suit getSuit() {
        return SUIT;
    }

    /**
     * Gets the rank of the card.
     * 
     * @return The rank of the card.
     */
    public Rank getRank() {
        return RANK;
    }

    /**
     * Returns a string representation of the card, including its rank and suit.
     * 
     * @return A string representing the card.
     */
    public String toString() {
        return RANK + " of " + SUIT;
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
            return false; // check if object comparison is null or not instance of card class
        }
        Card card = (Card) obj;// cast obj to card
        return SUIT == card.SUIT && RANK == card.RANK;
    }

    /*
     * Returns a hash code value for the card.
     * 
     * @return A hash code value for this card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(SUIT, RANK);
    }

    /**
     * Compares this card with the specified card for order.
     * 
     * @param other The card to be compared.
     * @return A negative integer, zero, or a positive integer as this card is less
     *         than, equal to, or greater than
     *         the specified card.
     */

    public int compareTo(Card other) {
        int rankComparison = this.RANK.compareTo(other.RANK);
        if (rankComparison != 0) {
            return rankComparison;
        }
        return this.SUIT.compareTo(other.SUIT);
    }
}