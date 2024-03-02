import java.util.ArrayList;

import Card.Suit;

public class SingleCard extends Hand{
    
    private String type;


    //check player SingleCard and their List of cards
    public SingleCard(Player player, CardList cards){
        super(player,cards);
        this.type = "SingleCard";
    }

    //this method is used to check for later use in the game
    public boolean isSingleCard(){
        return this.type.equals("SingleCard");
    }
    
    //check for handsize first then get its suit and rank return true if all correct
    public boolean static isValid(CardList hand){
        if(hand.size() == 1){
            Card card = hand.getCard(0);
            Suit suit = card.getSuit();
            Rank rank = card.getRank();
            return true
        }
        return false;
    }

    public String getType(){
        return this.type;
    }
}
