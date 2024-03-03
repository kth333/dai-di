import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StraightCard extends Hand{
    public String type;

    public StraightCard(Player player, CardList cards){
        super(player,cards);
        this.type = "StraightCard";
    }

    @Override
    public String getType() { // needs to override abstract method getType
        return "Straight";
    }

    //this method is used to check for later in the game
    public boolean isStraightCard(){
        return this.type.equals("StraightCard");
    }

    public boolean isValid(CardList hand){
        if(hand.size() != 5){
            return false;
        }
        hand.sortByRank();


        if (hand.getCard(0).getRank().ordinal() == 0) { // check if lowest rank is 0
            for (int i = 1; i < hand.size(); i++) {
                Card currentCard = hand.getCard(i);
                if (currentCard.getRank().ordinal() >= 9) { // check if rank 9 or above exist
                    Rank newRank = currentCard.getRank().adjustRank(-13);
                    currentCard.setRank(newRank);
                    hand.setCard(i, currentCard); // Update the card in the hand list with the new rank
            }
        }

        hand.sortByRank(); // sort again

        for(int i = 0; i< hand.size() - 1; i++){
            Card currentCard = hand.getCard(i);
            Card nextCard = hand.getCard(i+1);
            if(nextCard.getRank().ordinal() - currentCard.getRank().ordinal() != 1){
                //the difference can only be 1 because of straights the con-current flow
                return false;
            }
        }
        return true;
        
    }
}
}
//https://www.educative.io/answers/what-is-the-ordinal-method-in-enum-in-java
//how ordinal is used.