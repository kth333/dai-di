import java.util.ArrayList;

public abstract class Hand extends CardList {
    private Player player;
    private int numOfCards;

    // store the type of hand against this hand
    public ArrayList<String> againstArrayList;

    public Hand(Player player, CardList cards) {
        this.player = player;
        this.againstArrayList = new ArrayList<String>();
        // any hand against a pass will win
        this.againstArrayList.add("Pass");

        if (cards != null) {
            this.numOfCards = cards.size();
            for (int i = 0; i < cards.size(); i++) {
                this.addCard(cards.getCard(i));
            }
        } else {
            // if handis empty then the card will be null
            this.numOfCards = 0;
        }

    }

    // get the highest card of the hand
    public Card getHighestCard() {
        this.sort();
        return this.getCard(0);
    }

    // get player for this hand
    public Player getPlayer() {
        return this.player;
    }

    public int getNumOfCards() {
        return this.numOfCards;
    }

    public boolean isValid(CardList hand) {
        // valid hand must be at least 1 card e
        return this.getNumOfCards() > 0;
    }

    public boolean beats(Hand hand) {
        if (hand == null) {
            return true;
        }
        // check the number of cards as only the same number of cards can go against
        // each other
        if (this.getNumOfCards() != hand.getNumOfCards()) {
            return false;
        } else if (this.getType() == hand.getType()) {
            // check if the hand against are of the same type then continue comparison
            if (this.getHighestCard().compareTo(hand.getHighestCard()) > 0) {
                return true;
            } else {
                return false;
            }
        } else if (this.againstArrayList.contains(hand.getType())) {
            return true;
        }
        return false;
    }

    // so that other classes can use make this abstract
    public abstract String getType();

}