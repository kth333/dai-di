import java.util.*;

public class Player {
    private String name;
    private Hand hand;
    private double points;

    // Constructor for player with a custom name
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }
    
    // Method for player to play cards
    public PlayResult play(Player currentPlayer, PlayedCards previousCards, int consecutivePasses,Scanner scanner) {
        List<Card> hand = currentPlayer.getHand().getCardsInHand();
        
        while (true) {
            System.out.print("\nSelect cards to play (enter indices separated by spaces) or type p to pass: ");
            String input = scanner.nextLine();

            Card startCard=new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
            if (input.toLowerCase().equals("p")) {
                if (hasCard(startCard)){
                    System.out.println("Need to play 3 of Diamonds cannot pass first turn!");
                    continue;
                }
                System.out.println("\n" + currentPlayer.getName() + " passed their turn.");
                consecutivePasses++;
                return new PlayResult(previousCards, consecutivePasses); // Exit the method if the player chooses to pass
            }else if(input.toLowerCase().equals("r")){ // sort the hand by rank
                    currentPlayer.getHand().sortByRank();
                    System.out.println("Hand sorted by rank: "+ currentPlayer.getHand().getCardsInHand());
                    continue;
            }else if(input.toLowerCase().equals("s")){// sort the hand by suit
                    currentPlayer.getHand().sortBySuit();
                    System.out.println("Hand sorted by suit: "+ currentPlayer.getHand().getCardsInHand());
                    continue;
            } else if (input.toLowerCase().equals("q")) {
                    //System.out.println("Setting quit flag..."); // Debugging statement
                    PlayResult result = new PlayResult(previousCards, consecutivePasses);
                    result.setQuit(true); // Set the quit flag when player chooses to quit
                    return result;
            }

            boolean validSelection = true;
            List<Card> selectedCards = new ArrayList<>();
            String[] indices = input.split("\\s+");
            for (String indexStr : indices) {
                try {
                    int index = Integer.parseInt(indexStr);
                    if (index < 0 || index >= hand.size()) {
                        System.out.println("\nInvalid index! Please select indices within the range of your hand.");
                        validSelection = false;
                        break; // Continue to prompt the player for valid input
                    }
                    selectedCards.add(hand.get(index));
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input! Please enter indices separated by spaces.");
                    validSelection = false;
                    break; // Continue to prompt the player for valid input
                }
            }
            
            if (!validSelection) {
                continue; // Continue to prompt the player for valid input
            }
            
            // Create an instance of PlayedCards to store the selected cards
            PlayedCards playedCards = new PlayedCards(currentPlayer, selectedCards);
            
            // Check if the selected cards win against the previous cards
            if (hasCard(startCard) && !playedCards.getCards().contains(startCard)) {
                System.out.println("\nYou must play 3 of Diamonds on the first turn!");
                continue;
            }
            if (!playedCards.isValidSize()) {
                System.out.println("\nInvalid selection! Please select one, two, three or five cards.");
                continue;
            } else if (playedCards.getType().equals("Invalid")) {
                System.out.println("\nInvalid selection! Please select a valid combination.");
                continue;
            } else if (previousCards != null && !playedCards.winsAgainst(previousCards)) {
                System.out.println("\nInvalid selection! The selected cards do not beat previous cards.");
                continue; // Continue to prompt the player for valid input
            }
    
            // Display the played cards
            System.out.println("\n" + currentPlayer.getName() + " played: " + playedCards.getCards());
            // Remove the played cards from the player's hand
            hand.removeAll(playedCards.getCards());
            previousCards = playedCards;
            consecutivePasses = 0;
            return new PlayResult(previousCards, consecutivePasses);
    }
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
        if (add>0){
            points += add;
        }
    }

    public void deductPoints(double deduct) {
        if (deduct>0){
            points -= deduct;
        }
    }

    public double loseGame(double rate) {
        double deduct = rate * getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }

    public void removeAllCards() {
        hand.clear(); // Pass player to clear()
    }

    public int getNumOfCards() {
        return hand.getSize(); // Pass player to getSize()
    }

    public void receiveCard(Card card){//Adds card to hand
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
