import java.util.Comparator;
/**
 * Comparator for comparing cards based on their suits.
 */

public class SuitComparator implements Comparator<Card> {
     /**
     * Compares two cards based on their ranks.
     *
     * @param c1 the first card to be compared
     * @param c2 the second card to be compared
     * @return a negative integer, zero, or a positive integer as the first card 
     *         is less than, equal to, or greater than the second card based on suit
     */
    @Override
    public int compare(Card c1, Card c2) {
        return c1.getSuit().ordinal() - c2.getSuit().ordinal();
    }
}
