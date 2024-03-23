import java.util.*;
/**
 * Represents a player in a card game.
 */

public class Player {
    private String name;
    private Hand hand;
    private double points;

    /**
     * Constructs a player with the given name.
     *
     * @param name the name of the player
     */

    // Constructor for player with a custom name
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }
     /**
     * Allows the player to play cards and returns the result of the play.
     *
     * @param currentPlayer the player whose turn it is to play
     * @param previousCards the cards played in the previous turn
     * @param consecutivePasses the number of consecutive passes made in the game
     * @param scanner the Scanner object to read player input
     * @return the result of the play
     */
    // Method for player to play cards
    public PlayResult play(Player currentPlayer, PlayedCards previousCards, int consecutivePasses, int round, int turn) {
        List<Card> hand = currentPlayer.getHand().getCardsInHand();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("\nSelect cards to play (enter indices separated by spaces) or type p to pass: ");
            String input = scanner.nextLine();

            if (input.toLowerCase().equals("p")) {
                System.out.println("\n" + currentPlayer.getName() + " passed their turn.");
                consecutivePasses++;
                return new PlayResult(previousCards, consecutivePasses); // Exit the method if the player chooses to pass
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
            if (round == 1 && turn == 1 && !playedCards.getCards().contains(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE))) {
                System.out.println("\nYou must include 3 of Diamonds on the first turn!");
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
    /**
     * Retrieves the player's hand.
     *
     * @return the player's hand
     */

    public Hand getHand() {
        return hand;
    }

      /**
     * Retrieves the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }
    /**
     * Retrieves the player's points.
     *
     * @return the player's points
     */

    public double getPoints() {
        return this.points;
    }

     /**
     * Adds points to the player's total.
     *
     * @param add the points to add
     */

    public void addPoints(double add) {
        if (add>0){
            points += add;
        }
    }

     /**
     * Deducts points from the player's total.
     *
     * @param deduct the points to deduct
     */

    public void deductPoints(double deduct) {
        if (deduct>0){
            points -= deduct;
        }
    }
    /**
     * Calculates and deducts points from the player's total based on a given rate.
     *
     * @param rate the rate at which points are deducted
     * @return the points deducted
     */

    public double loseGame(double rate) {
        double deduct = rate * getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }
    /**
     * Removes all cards from the player's hand.
     */
    public void removeAllCards() {
        hand.clear(); // Pass player to clear()
    }
     /**
     * Retrieves the number of cards in the player's hand.
     *
     * @return the number of cards in the player's hand
     */

    public int getNumOfCards() {
        return hand.getSize(); // Pass player to getSize()
    }
     /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void receiveCard(Card card){//Adds card to hand
        hand.addCard(card);
    }
     /**
     * Checks if the player has a specific card in their hand.
     *
     * @param card the card to check
     * @return true if the player has the card, false otherwise
     */

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



