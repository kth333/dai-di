public class WinsAgainstTest {

    public static void main(String[] args) {
        PlayedCards singleCards = new PlayedCards();
        PlayedCards weakerSingle = new PlayedCards();
        PlayedCards doubleCards = new PlayedCards();
        PlayedCards weakerDouble = new PlayedCards();
        singleCards.addCard(new Card(Card.Suit.SPADES, Card.Rank.TWO));
        weakerSingle.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE));

        System.out.println(singleCards);
        System.out.println(weakerSingle);
        System.out.println("2 of SPADES beats 3 of DIAMONDS: " + singleCards.winsAgainst(weakerSingle));
        System.out.println("3 of DIAMONDS beats 2 of SPADES: " + weakerSingle.winsAgainst(singleCards));
        System.out.println();

        doubleCards.addCard(new Card(Card.Suit.SPADES, Card.Rank.TWO));
        doubleCards.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.TWO));
        weakerDouble.addCard(new Card(Card.Suit.SPADES, Card.Rank.THREE));
        weakerDouble.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE));
        System.out.println(doubleCards);
        System.out.println(weakerDouble);
        System.out.println("double beats weaker double: " + doubleCards.winsAgainst(weakerDouble));
        System.out.println();

        System.out.println("Size of singleCards: " + singleCards.getCards().size());
        System.out.println("Size of weakerSingle: " + weakerSingle.getCards().size());
        System.out.println("Size of doubleCards: " + doubleCards.getCards().size());
        System.out.println("Size of weakerDouble: " + weakerDouble.getCards().size());
        System.out.println();
        
        PlayedCards straightFlush = new PlayedCards();
        PlayedCards straight = new PlayedCards();
        straightFlush.addCard(new Card(Card.Suit.SPADES, Card.Rank.TWO));
        straightFlush.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE));
        straightFlush.addCard(new Card(Card.Suit.SPADES, Card.Rank.JACK));
        straightFlush.addCard(new Card(Card.Suit.SPADES, Card.Rank.KING));
        straightFlush.addCard(new Card(Card.Suit.SPADES, Card.Rank.QUEEN));

        System.out.println(straightFlush);
        System.out.println("straight flush? " + HandValidator.isStraightFlush(straightFlush));
        System.out.println();
        straight.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TWO));
        straight.addCard(new Card(Card.Suit.SPADES, Card.Rank.JACK));
        straight.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN));
        straight.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.KING));
        straight.addCard(new Card(Card.Suit.HEARTS, Card.Rank.ACE));
        System.out.println(straight);
        System.out.println("straight? " + HandValidator.isStraight(straight));
        System.out.println();

        System.out.println("straight flush beats straight: " + straightFlush.winsAgainst(straight));
        System.out.println("straight beats straight flush: " + straight.winsAgainst(straightFlush));
        System.out.println("Size of straight: " + straight.getCards().size());
        System.out.println("Size of straightFlush: " + straightFlush.getCards().size());
        System.out.println();

        System.out.println("straight beats single: " + straight.winsAgainst(singleCards));
        System.out.println("single beats straight: " + singleCards.winsAgainst(straight));
        System.out.println("straight beats null: " + straight.winsAgainst(null));
        System.out.println("single beats null: " + singleCards.winsAgainst(null));

    }
}
