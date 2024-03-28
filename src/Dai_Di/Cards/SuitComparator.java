<<<<<<< Updated upstream:src/Dai_Di/Cards/SuitComparator.java
package Dai_Di.Cards;
=======
package src;
>>>>>>> Stashed changes:src/SuitComparator.java
import java.util.Comparator;

public class SuitComparator implements Comparator<Card> {
    @Override
    public int compare(Card c1, Card c2) {
        return c1.getSuit().ordinal() - c2.getSuit().ordinal();
    }
}
