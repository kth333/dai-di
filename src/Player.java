import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Hand hand;
    private double points;
    private static final String[] botNames = { "Yeow Leong", "Zhi Yuan", "Lay Foo", "Fang Yuan", "Tony", "Lily Kong"}; // Array of bot names
    private static final Random random = new Random(); // Random object for selecting bot names

    // Constructor for human player with a custom name
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    // Constructor for bots with randomly assigned names
    public Player(String playerName, List<String> usedNames) {
        this.hand = new Hand();
        usedNames.add(playerName); // Add the player's name to the list of used names
        this.name = generateBotName(usedNames);
    }

    // Method to generate a random bot name
    private String generateBotName(List<String> usedNames) {
        String name;
        do {
            name = botNames[random.nextInt(botNames.length)];
        } while (usedNames.contains(name));
        usedNames.add(name);
        return name;
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    // public void playCard(Card card) {
    //     if (hand.contains(card)) {
    //         hand.removeCard(card);
    //         // might need to add the card to the area being played
    //     }
    // }

    public PlayedCards play(List<Integer> cardIndices) {
        if (cardIndices == null || cardIndices.isEmpty()) {
            return null;
        }
        PlayedCards playedCards = new PlayedCards(this, hand);
        for (int index : cardIndices) {
            if (index >= 0 && index < hand.getSize()) {
                Card card = hand.getCard(index);
                playedCards.addCard(card);
            }
        }
        hand.removeCards(playedCards);
        return playedCards;
    }

    public Hand getHand() {
        return hand;
    }
    
    public void setHand(List<Card> cards) {
        hand.clear(); // Clear any existing cards in the hand
        for (Card card : cards) {
            hand.addCard(card); // Add each card to the hand
        }
    }

    public String getName() {
        return this.name;
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
        double deduct = rate * getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }

    public void removeAllCards() {
        hand.clear();
    }

    public int getNumOfCards() {
        return hand.getSize();
    }

    public boolean has(Card card){
        return hand.contains(card);
    }

    //To be moved to CardGamePlayer
    public void winGame(List<Player> playerList, int winner, double rate){
        double winnings = 0;
        for (int i = 0; i < playerList.size(); i++){
            if (i != winner){
                Player player = playerList.get(i);
                winnings += player.loseGame(rate);
            }
        }
        playerList.get(winner).addPoints(winnings);
    }

    // public List<Player> playerOrder(List<Player> playerList, int numPlayers){
    //     List<Player> playerOrder = new ArrayList<>();

    //     //populate playerOrder list positions
    //     for (int i=0; i < numPlayers; i++){
    //         playerOrder.add(null);
    //     }

    //     //Set player Order
    //     for (int i = 0; i < numPlayers; i++) {
    //         //Get first player
    //         Player player = playerList.get(i);
    //         //Check if has startCard
    //         if (player.has(Card(DIAMONDS, THREE ))) {
    //             //If have startCard assign as first player
    //             //Assumes that all 52 cards are dealt out properly
    //             playerOrder.set(0, player);
    //         } else {
    //             //If not set their turn order as one of the other positions
    //             boolean turnOrderSet=false;
    //             //Set a do loop until turn order is successfully set
    //             do {
    //                 //nextInt(1,numPlayers) will generate random number inclusive 1 and exclusive numPlayers
    //                 int position = ThreadLocalRandom.current().nextInt(1, numPlayers);
    //                 //Set the player in the position if it is empty
    //                 if (playerOrder.get(position)==null) {
    //                     playerOrder.set(position,player);
    //                     turnOrderSet=true;
    //                 }
    //             } while (!turnOrderSet);
    //         }
    //     }
    //     return playerOrder;
    // }

    // public void displayPlayerOrder(List<Player> playerOrder){
    //     for (int i=0; i < playerOrder.size(); i++){
    //         Player player = playerOrder.get(i);
    //         String playerName = null;
    //         if (player instanceof Player){
    //             playerName = player.getName();
    //         } else {
    //             playerName = "Missing";
    //         }
    //         System.out.println("Player "+ i +" is "+ playerName);
    //     }
    // }


}