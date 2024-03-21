import java.util.*;

public class Hand {
    private List<Card> hand;

    public Hand() {
        this.hand = new ArrayList<Card>();
    }

    // Add a card to the hand of a specific player
    public void addCard(Card card) {
        if (card!=null){
            hand.add(card);
        }
    }

    // Removes cards that have been played from the hand of a specific player
    public void removeCards(PlayedCards playedCards) {
        if (playedCards != null) {
            hand.removeAll(playedCards.getCards());
        }
    }

    // Clears the hand of a specific player
    public void clear() {
        hand.clear();
    }

    // Returns the hand of a specific player
    public List<Card> getCardsInHand() {
        return hand;
    }

    // Checks if the hand of a specific player is empty
    public boolean isEmpty() {
        return hand.isEmpty();
    }

    // Returns the size of the hand of the player
    public int getSize() {
        return hand.size();
    }

    // Returns a copy of the hand of a specific player to avoid modification from outside
    public List<Card> getCards() {
        List<Card> handCopy=new ArrayList<Card>();
        for (int i=0;i<hand.size();i++){
            handCopy.add(hand.get(i));
        }
        return handCopy;
    }

     public void sortByRank() {
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.getRank().compareTo(c2.getRank());
            }
        });
    }
    
      //sort it by suit by getting the list of hand and compare
    public void sortBySuit() {
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                int suitComparison = c1.getSuit().compareTo(c2.getSuit());
                if (suitComparison == 0) {
                    return c1.getRank().compareTo(c2.getRank());
                }
                return suitComparison;
            }
        });
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < hand.size(); i++) {
            result += i + ". " + hand.get(i);
            if (i < hand.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}
