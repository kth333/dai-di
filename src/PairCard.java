import java.util.ArrayList;


public class PairCard extends Hand{
    private String type;

    public Pair(Player player, CardList cards){
        super(player, cards);
        this.type = "PairCard";
    }
    //this method will be used to check for later use in the game
    public boolean isPairCard(){
        return this.type.equals("PairCard");
    }

    public static boolean isValid(CardList hand){
        if(hand.size() == 2){
            Card card1 = hand.getCard(0);
            Card card2 = hand.getCard(1);
            return card1.getRank().equals(card2.getRank());
        }
        return false;
    }
    public String getType(){
        return this.type;
    }
}
