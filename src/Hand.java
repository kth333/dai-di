import java.util.*;

public class Hand {
    private List<Card> hand;

    public Hand() {
        this.hand = new ArrayList();
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
        List<Card> handCopy=new ArrayList<>();
        for (int i=0;i<hand.size();i++){
            handCopy.add(hand.get(i));
        }
        return handCopy;
    }
}