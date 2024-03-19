import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
        //create a deck of 52 cards one for each combination of suit and rank
        for(Card.Suit suit: Card.Suit.values()){
            for(Card.Rank rank : Card.Rank.values()){
                cards.add(new Card(suit, rank));
            }
        }
    }
    //collections method shuffle randomly permute specified list using specified source of randomness
    public void shuffle(){
        Collections.shuffle(cards);
    }

    public int size(){
        return cards.size();
    }

    public Card dealCard(){
        //remove a card from deck to deal if not empty
        if(!cards.isEmpty()){
            return cards.remove(cards.size() - 1 );
        }
        return null;
    }
}
