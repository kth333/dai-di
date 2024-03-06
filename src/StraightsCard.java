import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StraightsCard extends Hand{
    public String type;

    public Straight(Player player, CardList cards){
        super(player,cards);
        this.type = "StraightCards";
    }
    //this method is used to hceck for later in the game
    public boolean isStraightsCard(){
        return this.type.equals("StraightsCard");
    }

    public static boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        hand.sortByRank();
        //check if the cards form a straight
        //-1 cause checking for next and do not want to go out of bounds
        for(int i = 0; i< hand.size() - 1; i++){
            Card currentCard = hand.getCard(i);
            Card nextCard = hand.getCard(i+1);
            if(nextCard.getRank().ordinal() - currentCard.getRank().ordinal() != 1){
                //the difference can only be 1 because of straights the con-current flow
                //i dont think i have implemented the instance where it can start from ace
                return false;
            }
        }
        return true;
        
    }
}
//https://www.educative.io/answers/what-is-the-ordinal-method-in-enum-in-java
//how ordinal is used.