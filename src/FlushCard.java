import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FlushCard extends Hand{
    private String type;

    public FlushCard(Player player, CardList card){
        super(player,card);
        this.type = "FlushCard";
    }
    public boolean isFlushCard(){
        return this.type.equals("FlushCard");
    }

    public static boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        //make the first card suit and check consecutive suitedcards
        Suit firstCardSuit = hand.getCard(0).getSuit();

        //check if all the cards have the same suit as the first hand
        //if one is not same suit return false

        for(int i = 1; i< hand.size();i++){
            if(hand.getCard(i).getSuit() != firstCardSuit){
                return false;
            }
        }
        return true;
        //have not decided whether to check if the highest card of the suit is what
        //maybe will implement elsewhere

    }
}

