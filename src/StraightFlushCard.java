import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StraightFlushCard extends Hand {
    private string type;

    public StraightFlushCard(Player player, CardList card){
        super(player,card);
        this.type = "StraightFlushCard";
    }
    public boolean isQuadCard(){
        return.type.equals("StraightFlushCard");
    }
    public static boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        //sort the card by rank first
        hand.sortByRank();
        boolean CheckIfFlush = true;
        Suit firstCardSuit = hand.getCard(0).getSuit();
        //check for every card whether same suit
        for(int i = 1; i < hand.size(); i++){
            if(hand.getCard(i).getSuit() != firstCardSuit){
                CheckIfFlush = false;
                break;
            }
        }
        boolean CheckIfStraight = true;
        for(int i = 0; i < hand.size() - 1; i++){
            if(hand.getCard(i+1).getRank().ordinal() - hand.getCard(i).getRank().ordinal() != 1){
                CheckIfStraight = false;
                break;
            }
        }
        //same i havent implement whether A to 5 consider straight
        return CheckIfFlush && CheckIfStraight; // satisfy both conditon 
    }
}
