import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private String name;
    private Hand hand;
    private double points;
    private static final String[] botNames = { "Yeow Leong", "Zhi Yuan", "Lay Foo", "Fang Yuan", "Tony", "Lily Kong" }; // Array
                                                                                                                        // of
                                                                                                                        // bot
                                                                                                                        // names
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

    // public void playCard(Card card) {
    // if (hand.contains(card)) {
    // hand.removeCard(card);
    // // might need to add the card to the area being played
    // }
    // }

    public void play(List<Integer> selectedIndices, List<Card> hand, PlayedCards previousCards) {
        // Validate selected indices
        boolean validSelection = true;
        List<Card> selectedCards = new ArrayList<>();
        for (int index : selectedIndices) {
            if (index < 0 || index >= hand.size()) {
                System.out.println("Invalid index! Please select indices within the range of your hand.");
                validSelection = false;
                break; // Continue to prompt the player for valid input
            }
            selectedCards.add(hand.get(index));
        }

        if (!validSelection) {
            return; // Continue to prompt the player for valid input
        }

        // Create an instance of PlayedCards to store the selected cards
        PlayedCards playedCards = new PlayedCards(this, selectedCards);

        // Check if the selected cards win against the previous cards
        if (previousCards != null && !playedCards.winsAgainst(previousCards)) {
            System.out.println("Invalid selection! The selected cards do not win against the previous cards.");
            return; // Continue to prompt the player for valid input
        }

        // Display the played cards
        System.out.println(this.getName() + " played: " + playedCards.getCards());

        // Remove the played cards from the player's hand
        hand.removeAll(playedCards.getCards());
        previousCards = playedCards;
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
        points += add;
    }

    public void deductPoints(double deduct) {
        points -= deduct;
    }

    public double loseGame(double rate) {
        double deduct = rate * getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }

    public void removeAllCards() {
        hand.clear(this); // Pass player to clear()
    }

    public int getNumOfCards() {
        return hand.getSize(this); // Pass player to getSize()
    }

    public boolean hasCard(Card card) {
        if (card == null) {
            return false;
        }
        List<Card> cardList = hand.getCards(this);

        if (cardList != null) {
            return cardList.contains(card);
        }

        return false;
    }

    // To be moved to CardGamePlayer
    public void winGame(List<Player> playerList, int winner, double rate) {
        double winnings = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (i != winner) {
                Player player = playerList.get(i);
                winnings += player.loseGame(rate);
            }
        }
        playerList.get(winner).addPoints(winnings);
    }

    public static Comparator<Player> sortByPoints() {
        return Comparator.comparingDouble(Player::getPoints).reversed();
    }

    public List<Player> playerOrder(List<Player> playerList, int numPlayers) {
        if (playerList == null || numPlayers > 1) {
            return null;
        }

        Player[] playerOrder = new Player[numPlayers];

        // Set player Order
        for (int i = 0; i < numPlayers; i++) {
            // Get first player
            Player player = playerList.get(i);
            // Check if has startCard
            if (player.hasCard(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE))) {
                // If have startCard assign as first player
                // Assumes that all 52 cards are dealt out properly
                playerOrder[0] = player;
            } else {
                // If not set their turn order as one of the other positions
                boolean turnOrderSet = false;
                // Set a do loop until turn order is successfully set
                do {
                    // nextInt(1,numPlayers) will generate random number inclusive 1 and exclusive
                    // numPlayers
                    int position = ThreadLocalRandom.current().nextInt(1, numPlayers);
                    // Set the player in the position if it is empty
                    if (playerOrder[position] == null) {
                        playerOrder[position] = player;
                        turnOrderSet = true;
                    }
                } while (!turnOrderSet);
            }
        }
        return Arrays.asList(playerOrder);
    }

    public void displayPlayerOrder(List<Player> playerOrder) {
        for (int i = 0; i < playerOrder.size(); i++) {
            Player player = playerOrder.get(i);
            String playerName = null;
            if (player instanceof Player) {
                playerName = player.getName();
            } else {
                playerName = "Missing";
            }
            System.out.println("Player " + i + " is " + playerName);
        }
    }

}
