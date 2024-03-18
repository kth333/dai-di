public class TripleCard extends PlayedCards {
    private String type;

    public TripleCard(Player player, CardList card){
        super(player, card);
        this.type = "TripleCard";
    }
    public isTripleCard(){
        return.type.equals("TripleCard");
    }

    public static booelean isValid(CardList hand){
        if(hand.size()!=3){
            return false;
        }
        //get the first card
        Rank firstCardRank() = hand.getCard(0).getRank();
        //check for consecutive cards whether they are the same
        for(int i = 1; i < hand.size; i++){
            if(hands.getCard(i).getRank() != firstCardRank){
                return false;
            }
            
        }
        return true;
    }
}
