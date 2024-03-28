package src;
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
    public PlayResult play(PlayedCards previousCards, int consecutivePasses, int turn, Scanner scanner) {
        List<Card> hand = getHand().getCardsInHand();

        while (true) {
            System.out.print("\nOptions:\n" +
                    " - Select cards to play (enter indices separated by spaces)\n" +
                    " - Type 'pass' to pass\n" +
                    " - Type 'suit' to sort hand by suit\n" +
                    " - Type 'rank' to sort hand by rank\n" +
                    " - Type 'quit' to quit the game\n" +
                    "Your choice: ");
            String input = scanner.nextLine();

            Card startCard = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
            if (input.toLowerCase().equals("pass")) {
                if (hasCard(startCard)) {
                    System.out.println("Need to play 3 of Diamonds cannot pass first turn!");
                    continue;
                }
                System.out.println("\n" + getName() + " passed their turn.");
                consecutivePasses++;
                return new PlayResult(this,previousCards, consecutivePasses,turn); // Exit the method if the player chooses to pass
            } else if (input.toLowerCase().equals("rank")) { // sort the hand by rank
                getHand().sortByRank();
                System.out.println("\nHand sorted by rank: " + getHand());
                continue;
            } else if (input.toLowerCase().equals("suit")) {// sort the hand by suit
                getHand().sortBySuit();
                System.out.println("\nHand sorted by suit: " + getHand());
                continue;
            } else if (input.toLowerCase().equals("quit")) {
                // System.out.println("Setting quit flag..."); // Debugging statement
                PlayResult result = new PlayResult(this,previousCards, consecutivePasses,turn);
                result.setQuit(true); // Set the quit flag when player chooses to quit
                return result;
            }else if (input.toLowerCase().equals("i")){
                    Instructions.displayInstructions();
                    continue;
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
            PlayedCards playedCards = new PlayedCards(selectedCards);

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
            } else if (!playedCards.winsAgainst(previousCards)) {
                System.out.println("\nInvalid selection! The selected cards do not beat previous cards.");
                continue; // Continue to prompt the player for valid input
            }

            // Display the played cards
            System.out.println("\n" + getName() + " played: " + playedCards.getCards());
            // Remove the played cards from the player's hand
            hand.removeAll(playedCards.getCards());
            previousCards = playedCards;
            consecutivePasses = 0;
            return new PlayResult(this,previousCards, consecutivePasses,turn);
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

    public void removeAllCards() {
        hand.clear(); // Pass player to clear()
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
