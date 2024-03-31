package Dai_Di.Cards;

import java.util.Comparator;

/**
 * Comparator for comparing cards based on their suits.
 */
public class SuitComparator implements Comparator<Card> {

    /**
     * Compares two cards based on their suits.
     *
     * @param c1 the first card to compare
     * @param c2 the second card to compare
     * @return a negative integer, zero, or a positive integer 
     * when the first card is less than, equal to, or greater than the second card based on their suits
     */
    @Override
    public int compare(Card c1, Card c2) {
        // Compare the ordinal values of the suits of the two cards
        return c1.getSuit().ordinal() - c2.getSuit().ordinal();
    }
}