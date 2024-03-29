import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StraightCardTest {

    @Test
    public void testIsValid() {
        // Create a CardList and populate it with Card objects
        Hand hand = new Hand();
        hand.add(new Card(Rank.ACE, Suit.HEARTS));
        hand.add(new Card(Rank.TWO, Suit.HEARTS));
        hand.add(new Card(Rank.THREE, Suit.HEARTS));
        hand.add(new Card(Rank.FOUR, Suit.HEARTS));
        hand.add(new Card(Rank.FIVE, Suit.HEARTS));

        // Instantiate StraightCard with the CardList
        StraightCard straightCard = new StraightCard(new Player(), hand);

        // Call isValid and assert the expected result
        assertTrue(straightCard.isValid(hand), "The hand should be a valid straight");
    }

    // Add more test methods for different cases, such as invalid straights
}