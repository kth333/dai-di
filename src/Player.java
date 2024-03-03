public class Player {
    private String name;
    private CardList hand;

    public Player(String name){
        this.name = name;
        this.hand = new CardList();
    }
    public void receiveCard(Card card){
        hand.addCard(card);
    }
    public void playCard(Card card){
        if(hand.contains(card)){
            hand.removeCard(card);
            //might need to add the card to the area being played
        }
    }
    public CardList getHand(){
        return hand;
    }
    public String getName(){
        return this.name;
    }
    public void setName(){
        this.name = name;
    }
    public void removeAllCards(){
        hand.removeAllCards();
    }
    public int getNumOfCards(){
        hand.size();
    }
    public void sortHand(){
        hand.sort();
    }

    public CardList play(Integer cardIndex){
        if(cardIndex == null){
            return null;
        }
        CardList playedCards = new CardList();
        if(cardIndex >= 0 && cardIndex < hand.size()){
            Card card = hand.getCard(cardIndex);
            playedCards.addCard(card);
            hand.removeCard(card);
        }
        return playedCards;
    }
    
}
