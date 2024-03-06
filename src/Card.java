import java.util.Objects;

public class Card implements Comparable <Card> {
    public enum Suit{
        DIAMONDS, CLUBS, HEARTS, SPADES
    }
    public enum Rank{
        THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE, TWO
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit(){
        return suit;
    }
    public Rank getRank(){
        return rank;
    }
    public String toString(){
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true; //check if object is same as this object
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;//check if object comparison is null or not instance of card class
        }
        Card card = (Card) obj;// cast obj to card
        return suit == card.suit && rank == card.rank;
    }
    @Override
    public int hashCode(){
        return Objects.hash(suit,rank);
    }

    public int compareTo(Card other){
        int rankComparison = this.rank.compareTo(other.rank);
        if(rankComparison != 0){
            return rankComparison;
        }
        return this.suit.compareTo(other.suit);
    }
}   
