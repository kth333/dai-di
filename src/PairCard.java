import java.util.ArrayList;


public class PairCard extends PlayedCards {
    private String type;

    public Pair(Player player, Hand cards){
        super(player, cards);
        this.type = "PairCard";
    }
    //this method will be used to check for later use in the game
    public boolean isPairCard(){
        return this.type.equals("PairCard");
    }

    public static boolean isValid(Hand hand){
        if(hand.size() == 2){
            Card card = hand.getCard(0);
            Suit suit = card.getSuit();
            Rank rank = card.getRank();

            return true;
        }
        return false;
    }
    public String getType(){
        return this.type;
    }
}
