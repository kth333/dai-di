package src;
import java.util.Comparator;

public class RankComparator implements Comparator<Card> {
    @Override
    public int compare(Card c1, Card c2) {
        return c1.getRank().ordinal() - c2.getRank().ordinal();
    }
}