import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FullHouseCard extends Hand {
    private String type;

    public FullHouseCard(Player player, CardList card){
        super(player,card);
        this.type = "FullHouseCard";
    }
    public boolean isFullHouseCard(){
        return this.type.equals("FullHouseCard");
    }
    public static boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        int[] rankCount = new int[Rank.values().length];
        for(int i = 0; i < hand.size(); i++){
            rankCount[hand.getCard(i).getRank().ordinal()]++;
        }
        boolean foundTriple = false;
        boolean foundPair = false;
        for(int count : rankCount){
            if(count == 3){
                foundTriple = true;
            }else if(count == 2){
                foundPair = true;
            }
        }
        return foundTriple && foundPair; //both must be true if the hand has a triple and a pair
    }
}