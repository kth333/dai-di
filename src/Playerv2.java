import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Player {
    private static final String PASS_COMMAND = "pass";
    private static final String QUIT_COMMAND = "quit";
    private static final String RANK_COMMAND = "rank";
    private static final String SUIT_COMMAND = "suit";
    private static final String INSTRUCTIONS_COMMAND = "i";
    private static final Card START_CARD = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);

    private String name;
    private Hand hand;
    private double points;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public PlayResult play(PlayedCards previousCards, int consecutivePasses, Scanner scanner) {
        while (true) {
            displayOptions();
            String input = scanner.nextLine().toLowerCase();
            switch (input) {
                case PASS_COMMAND:
                    PlayResult passResult = handlePass(previousCards, consecutivePasses);
                    if (passResult != null) return passResult;
                    break; // If passing is not allowed, it will break and prompt again.
                case RANK_COMMAND:
                    sortHandByRank();
                    break;
                case SUIT_COMMAND:
                    sortHandBySuit();
                    break;
                case QUIT_COMMAND:
                    return handleQuit(previousCards, consecutivePasses);
                case INSTRUCTIONS_COMMAND:
                    Instructions.displayInstructions();
                    break;
                default:
                    PlayResult selectionResult = handleCardSelection(input, previousCards, consecutivePasses);
                    if (selectionResult != null) return selectionResult;
                    // If handleCardSelection returns null, indicating invalid selection, print an error message
                    // and continue in the loop to prompt again.
                    System.out.println("Invalid selection, please try again.");
                    break;
            }
        }
    }

    private void displayOptions() {
        System.out.print("\nOptions:\n" +
                " - Select cards to play (enter indices separated by spaces)\n" +
                " - Type 'pass' to pass\n" +
                " - Type 'suit' to sort hand by suit\n" +
                " - Type 'rank' to sort hand by rank\n" +
                " - Type 'quit' to quit the game\n" +
                "Your choice: ");
    }

    private PlayResult handlePass(PlayedCards previousCards, int consecutivePasses) {
        if (hasCard(START_CARD)) {
            System.out.println("Need to play 3 of Diamonds. Cannot pass first turn!");
            return null;
        }
        System.out.println("\n" + getName() + " passed their turn.");
        return new PlayResult(this, previousCards, ++consecutivePasses);
    }

    private void sortHandByRank() {
        getHand().sortByRank();
        System.out.println("\nHand sorted by rank: " + getHand());
    }

    private void sortHandBySuit() {
        getHand().sortBySuit();
        System.out.println("\nHand sorted by suit: " + getHand());
    }

    private PlayResult handleQuit(PlayedCards previousCards, int consecutivePasses) {
        PlayResult result = new PlayResult(this, previousCards, consecutivePasses);
        result.setQuit(true);
        return result;
    }

    private PlayResult handleCardSelection(String input, PlayedCards previousCards, int consecutivePasses) {
        List<Card> selectedCards = parseSelectedCards(input);
        if (selectedCards == null) {
            return null; // Invalid selection, continue prompting
        }

        PlayedCards playedCards = new PlayedCards(selectedCards);
        if (validatePlayedCards(playedCards, previousCards)) {
            System.out.println("\n" + getName() + " played: " + playedCards);
            getHand().removeAllCards(playedCards.getCards());
            return new PlayResult(this, playedCards, 0);
        }
        return null;
    }

    private List<Card> parseSelectedCards(String input){
        List<Card> selectedCards = new ArrayList<>();
        String[] indices = input.split("\\s+");
        for(String idxStr: indices){
            try{
                int idx = Integer.parseInt(idxStr.trim());
                if(idx < 0 || idx >= getHand().getCardsInHand().size()){
                    System.out.println("\nInvalid index! Please select indices within range of your hand.");
                    return null;
                }
                selectedCards.add(getHand().getCardsInHand().get(idx));
            }catch(NumberFormatException e){
                System.out.println("\nInvalid input! Please enter indices seperately.");
                return null;
            }
        }
        return selectedCards;
    }

    private boolean validatePlayedCards(PlayedCards playedCards, PlayedCards previousCards){
        if(previousCards == null || previousCards.getCards().isEmpty()){
            // Ensure "3 of Diamonds" is part of the played cards if it has not been played yet.
            if (hasCard(START_CARD) && !playedCards.contains(START_CARD)) {
                System.out.println("You must play the 3 of Diamonds on the first turn!");
                return false;// Block the play as invalid since "3 of Diamonds" is not included.
            }
        }
        if(!playedCards.isValidSize()){
            System.out.println("\nInvalid Selection! Please select one, two, three or five cards.");
            return false;
        }
        if (playedCards.getType().equals("Invalid")) {
            System.out.println("\nInvalid selection! Please select a valid combination.");
            return false;
        }
    
        // Check if the played cards beat the previous cards (if any).
        if (previousCards != null && !playedCards.getCards().isEmpty() && !playedCards.winsAgainst(previousCards)) {
            System.out.println("\nInvalid selection! The selected cards do not beat the previously played cards.");
            return false;
        }
    
        return true;
    }



    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return this.name;
    }

    public double getPoints() {
        return this.points;
    }

    public void addPoints(double add) {
        if (add > 0) {
            points += add;
        }
    }

    public void deductPoints(double deduct) {
        if (deduct > 0) {
            points -= deduct;
        }
    }

    public double loseGame(double rate) {
        // double deduct = rate * getNumOfCards();
        int numCards=getNumOfCards();
        double deduct = numCards * rate;
        if (numCards >= 10) {//If num of Cards greater then rate *3 loss
            deduct *= 3;
        } else if (numCards >=7) {//If num of cards equal to rate then *2 loss
            deduct *= 2;
        }
        deductPoints(deduct);
        return deduct;
    }

    

    public int getNumOfCards() {
        return hand.getSize(); // Pass player to getSize()
    }

    public void receiveCard(Card card) {// Adds card to hand
        hand.addCard(card);
    }

    public boolean hasCard(Card card) {
        if (card == null) {
            return false;
        }

        return hand.getCardsInHand().contains(card);
    }

    public static void winGame(List<Player> playerList, Player winner, double rate) {
        double winnings = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).equals(winner)) {
                Player player = playerList.get(i);
                winnings += player.loseGame(rate);
            }
        }
        winner.addPoints(winnings);
    }

    public static Comparator<Player> sortByPoints() {
        return Comparator.comparingDouble(Player::getPoints).reversed();
    }
}
