import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class QuadCard extends PlayedCards {
    private string type;

    public QuadCard(Player player, CardList card){
        super(player,card);
        this.type = "QuadCard";
    }
    public boolean isQuadCard(){
        return.type.equals("QuadCard");
    }
    public static boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        boolean foundQuad = false;
        int[] rankCount = new int[Rank.values().length];
        for(int i = 0; i <hand.size();i++){
            rankCount[hand.getCard(i).getRank().ordinal()]++;
        }
        
        for(int count : rankCount){
            if(count == 4){
                foundQuad = true;
                break;
            }
        }
        return foundQuad;
    }
}
