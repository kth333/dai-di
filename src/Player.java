import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private String name;
    private CardList hand;
    private double points;

    public Player(String name) {
        this.name = name;
        this.hand = new CardList();
        this.points = 100;
    }

    public Player(String name, double value) {
        this(name);
        this.points = value;
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.removeCard(card);
            // might need to add the card to the area being played
        }
    }

    public CardList getHand() {
        return hand;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return this.points;
    }

    public void addPoints(double add) {
        points += add;
    }

    public void deductPoints(double deduct) {
        points -= deduct;
    }

    public double loseGame(double rate){
        double deduct=rate*getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }

    public void removeAllCards() {
        hand.removeAllCards();
    }

    public int getNumOfCards() {
        return hand.size();
    }

    public void sortHand() {
        hand.sort();
    }

    public boolean has(Card card){
        return hand.contains(card);
    }

    //To be moved to CardGamePlayer
    public void winGame(List<Player> playerList, int winner, double rate){
        double winnings=0;
        for (int i=0;i<playerList.size();i++){
            if (i!=winner){
                Player player=playerList.get(i);
                winnings+=player.loseGame(rate);
            }
        }
        playerList.get(winner).addPoints(winnings);;
    }

    public List<Player> playerOrder(List<Player> playerList,Card startCard,int numPlayers){
        List<Player> playerOrder = new ArrayList<>();

        //populate playerOrder list positions
        for (int i=0;i<numPlayers;i++){
            playerOrder.add(null);
        }

        //Set player Order
        for (int i = 0; i < numPlayers; i++) {
            //Get first player
            Player player = playerList.get(i);
            //Check if has startCard
            if (player.has(startCard)) {
                //If have startCard assign as first player
                //Assumes that all 52 cards are dealt out properly
                playerOrder.set(0, player);
            } else {
                //If not set their turn order as one of the other positions
                boolean turnOrderSet=false;
                //Set a do loop until turn order is successfully set
                do {
                    //nextInt(1,numPlayers) will generate random number inclusive 1 and exclusive numPlayers
                    int position = ThreadLocalRandom.current().nextInt(1, numPlayers);
                    //Set the player in the position if it is empty
                    if (playerOrder.get(position)==null) {
                        playerOrder.set(position,player);
                        turnOrderSet=true;
                    }
                } while (!turnOrderSet);
            }
        }
        return playerOrder;
    }

    public void displayPlayerOrder(List<Player> playerOrder){
        for (int i=0;i<playerOrder.size();i++){
            Player player=playerOrder.get(i);
            String playerName=null;
            if (player instanceof Player){
                playerName=player.getName();
            } else{
                playerName="Missing";
            }
            System.out.println("Player "+i+" is "+playerName);
        }
    }

    public CardList play(Integer cardIndex) {
        if (cardIndex == null) {
            return null;
        }
        CardList playedCards = new CardList();
        for (int index : cardIndex) {
            if (index >= 0 && index < hand.size()) {
                Card card = hand.getCard(index);
                playedCards.addCard(card);
            }
        }
        hand.removeCards(playedCards);
        return playedCards;
    }

}

