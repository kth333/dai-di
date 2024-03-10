import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;


public class CardList {
    private List<Card> cards;

    public CardList(){
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card){
        cards.add(card);
    }
    
    public Card getCard(int i) {
        return cards.get(i);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public void removeAllCards() {
        cards.clear();
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public void setCard(int index, Card card) {
        if (index >= 0 && index < cards.size()) {
            cards.set(index, card);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void sort() {
        Collections.sort(cards);
    }

   
    public void sortByRank(){
        Collections.sort(cards,new Comparator<Card>(){
            public int compare(Card c1, Card c2){
                return c1.getRank().ordinal() - c2.getRank().ordinal();
            }
        });
    }

    public void sortBySuit(){
        Collections.sort(cards, new Comparator<Card>(){
            public int compare(Card c1, Card c2){
                return c1.getSuit().ordinal() - c2.getSuit().ordinal();
            }
        });
    }
    
    public int size() {
        return cards.size();
    }


    public void print() {
        for (Card card : cards) {
            System.out.println(card.toString());
        }
    }
}
