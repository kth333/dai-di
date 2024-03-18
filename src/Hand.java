import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private List<Card> hand;
    private List<Card> cardsToRemove;

    public Hand(){
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card){
        hand.add(card);
    }
    
    public Card getCard(int i) {
        return hand.get(i);
    }

    // Removes cards that have been played from the hand
    public void removeCards(PlayedCards playedCards) {
        List<Card> cardsToRemove = playedCards.getCards();
        hand.removeAll(cardsToRemove);
    }

    // Clears the hand
    public void clear() { 
        hand.clear();
    }

    public boolean contains(Card card) {
        return hand.contains(card);
    }

    public void setCard(int index, Card card) {
        if (index >= 0 && index < hand.size()) {
            hand.set(index, card);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    public boolean isEmpty() {
        return hand.isEmpty();
    }
   
    public void sortByRank() {
        Collections.sort(hand, new RankComparator());
    }

    public void sortBySuit() {
        Collections.sort(hand, new SuitComparator());
    }
    
    public int getSize() {
        return hand.size();
    }

    public void print() {
        for (Card card : hand) {
            System.out.println(card.toString());
        }
    }

    @Override
    public String toString() {
        String result = "Hand: [";
        for (int i = 0; i < hand.size(); i++) {
            result += hand.get(i);
            if (i < hand.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}
