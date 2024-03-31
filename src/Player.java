package src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Player {
    private static final int PLAY_COMMAND = 0;// Sets the play command
    private static final int PASS_COMMAND = 1;// Sets the pass command
    private static final int SUIT_COMMAND = 2;// Sets the sort hand by suit command
    private static final int RANK_COMMAND = 3;// Sets the sort hand by rank command
    private static final int INSTRUCTIONS_COMMAND = 4;// Sets the command to display instructions
    private static final int MUSIC_COMMAND = 5; // Sets the command to play or mute music
    private static final int QUIT_COMMAND = 6;// Sets the quit game command
    private static final String BACK_COMMAND = "back";

    private String name;
    private Hand hand;
    private double points;

    /**
     * Constructor that creates a Player object
     *
     * @param name String representing the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    /**
     * Prompts player for their command
     * accepts PLAY_COMMAND to play a hand
     * accepts PASS_COMMAND to return playResult that player pass turn increasing
     * consecutivePasses by 1
     * accepts QUIT_COMMAND to return a PlayResult that player is quitting with
     * isQuit=true
     * accepts RANK_COMMAND and SUIT_COMMAND to sort player hand by rank or suit
     * accepts INSTRUCTIONS_COMMAND to display the instructions
     * accepts MUSIC_COMMAND to stop or start the music
     * 
     * @param previousCards     The last played cards, is null at start or after 3
     *                          consecuetivePasses
     * @param consecutivePasses The number of times the previous players have passed
     * @param scanner           the scanner object for input
     * @return the PlayResult created that would store if player passes the command
     *         successfully
     */
    public PlayResult play(PlayedCards previousCards, int consecutivePasses, Scanner scanner) {
        while (true) {
            displayOptions();
            int input = GameView.getInt("Your choice: ", scanner);
            switch (input) {
                case PLAY_COMMAND:
                    if (previousCards == null) {
                        if (hasCard(Card.START_CARD)) {
                            System.out.println("\nLast Played: None. It's the first turn!"); // When it's the first
                                                                                             // turn
                        } else {
                            System.out.println("\nLast Played: 3 skips, free to play anything!"); // When 3
                                                                                                  // consecutive
                                                                                                  // passes
                        }
                    } else {
                        System.out.println("\nLast Played: \n" + previousCards.toString()); // Show player previously
                                                                                            // played cards
                    }
                    System.out.println("\nYour Hand: " + getHand()); // Show player their hand again
                    PlayResult result = playHand(previousCards, consecutivePasses, scanner);
                    if (result != null) {
                        return result;
                    }
                    break;
                case PASS_COMMAND:
                    PlayResult passResult = handlePass(previousCards, consecutivePasses);
                    if (passResult != null) {
                        return passResult;
                    }
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
                case MUSIC_COMMAND:
                    MusicPlayer.playOrStopMusic();
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
                    break;
            }

        }
    }

    /**
     * Prompts player for their selected cards
     * accepts the cards according to the hand list index from left to right
     * 
     * @param previousCards     The last played cards, is null at start or after 3
     *                          consecuetivePasses
     * @param consecutivePasses The number of times the previous players have passed
     * @param scanner           the scanner object for input
     * @return the PlayResult that would be created if the selection is validated or
     *         null if BACK_COMMAND is
     *         entered
     */
    private PlayResult playHand(PlayedCards previousCards, int consecutivePasses, Scanner scanner) {
        while (true) {
            String prompt = "\nSelect cards to play (enter indices separated by spaces) or enter '" + BACK_COMMAND
                    + "' to return to Options:";
            String input = GameView.getString(prompt, scanner);

            if (input.equalsIgnoreCase(BACK_COMMAND)) {
                return null;
            }
            PlayResult selectionResult = handleCardSelection(input, previousCards, consecutivePasses);
            if (selectionResult != null) {
                return selectionResult;
            } else {
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Displays the commands the player can enter at main menu
     *
     */
    private void displayOptions() {
        System.out.print("\nOptions:\n" +
                " - Type '" + PLAY_COMMAND + "' to play a hand\n" +
                " - Type '" + PASS_COMMAND + "' to pass\n" +
                " - Type '" + SUIT_COMMAND + "' to sort hand by suit\n" +
                " - Type '" + RANK_COMMAND + "' to sort hand by rank\n" +
                " - Type '" + INSTRUCTIONS_COMMAND + "' to display instructions\n" +
                " - Type '" + MUSIC_COMMAND + "' to play or stop the music\n" +
                " - Type '" + QUIT_COMMAND + "' to quit the game\n");
    }

    /**
     * Validates the player passing turn, player cannot pass turn if has Starting
     * card
     * If player has start card notify player and return null
     * If player does not have start card player can pass, so playResult will be
     * created with incremented
     * consecuetivePasses and the previousCards
     * 
     * @param previousCards
     * @param consecutivePasses
     * @return playResult containing the previousCards and an incremented
     *         consecutivePasses or null
     */
    private PlayResult handlePass(PlayedCards previousCards, int consecutivePasses) {
        if (hasCard(Card.START_CARD)) {
            System.out.println("Need to play 3 of Diamonds. Cannot pass first turn!");
            return null;
        }
        System.out.println("\n" + getName() + " passed their turn.");
        return new PlayResult(previousCards, ++consecutivePasses);
    }

    /**
     * Sorts and displays the Player's hand by rank
     * 
     */
    private void sortHandByRank() {
        Hand hand = getHand();
        hand.sortByRank();
        String asciiHand = Art.getAsciiRepresentation(hand.getCards()); // Assuming getCards() returns the List<Card> in
                                                                        // Hand
        System.out.println("\nHand sorted by rank: \n" + asciiHand);
        System.out.println(getHand());
    }

    /**
     * Sorts and displays the Player's hand by suit
     * 
     */
    private void sortHandBySuit() {
        Hand hand = getHand();
        hand.sortBySuit();
        String asciiHand = Art.getAsciiRepresentation(hand.getCards()); // Similarly for sorting by suit
        System.out.println("\nHand sorted by suit: \n" + asciiHand);
        System.out.println(getHand());
    }

    /**
     * Creates PlayResult with quit set to true to allow player to quit game
     * 
     * @param previousCards
     * @param consecutivePasses
     * @return PlayResult with quit set to true
     */
    private PlayResult handleQuit(PlayedCards previousCards, int consecutivePasses) {
        PlayResult result = new PlayResult(previousCards, consecutivePasses);
        result.setQuit(true);
        return result;
    }

    /**
     * Validates if player input can be used to play the cards, If not return null
     * If it is able and the card selection is valid return Playresult with the
     * valid card selection
     * 
     * @param input             What the player has input into the console in String
     * @param previousCards     The cards the last player who played put
     * @param consecutivePasses The number of passes since the last play
     * @return PlayResult null if invalid selection or Playresult containing the
     *         validated played cards
     *         and with consecutivePasses set to 0
     */
    private PlayResult handleCardSelection(String input, PlayedCards previousCards, int consecutivePasses) {
        List<Card> selectedCards = parseSelectedCards(input);
        if (selectedCards == null) {
            return null; // Invalid selection, continue prompting
        }

        PlayedCards playedCards = new PlayedCards(selectedCards);
        if (validatePlayedCards(playedCards, previousCards)) {
            System.out.println("\n" + getName() + " played: \n" + playedCards);
            getHand().removeCards(playedCards);
            return new PlayResult(playedCards, 0);
        }
        return null;
    }

    /**
     * Parses the player console input into the cards in hand using the assumption
     * that the cards
     * are played using their indexes in order from left to right
     * If the input is not an integer or index found is out of range of hand return
     * null
     * 
     * @param input What the player has input into the console in String
     * @return List of cards selected or null if selection is invalid
     */
    private List<Card> parseSelectedCards(String input) {
        List<Card> selectedCards = new ArrayList<>();
        String[] indices = input.split("\\s+");
        for (String idxStr : indices) {
            try {
                int idx = Integer.parseInt(idxStr.trim());
                if (idx < 0 || idx >= getHand().getCards().size()) {
                    System.out.println("\nInvalid index! Please select indices within range of your hand.");
                    return null;
                }
                selectedCards.add(getHand().getCards().get(idx));
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter indices seperately.");
                return null;
            }
        }
        return selectedCards;
    }

    /**
     * Checks if the cards being played are valid,
     * if previousCards is null check if player has start card and played start card
     * was played
     * Start card must be played if player has it on the first turn
     * If number of cards is invalid fail validation
     * If selected cards are not an allowed combination fail validation
     * If cards selected doesnt win against last played cards fail validation
     * 
     * @param playedCards   The cards the player has selected using the console
     * @param previousCards The last cards played
     * @return false if playedCards fails validation or true if playedCards passes
     */
    private boolean validatePlayedCards(PlayedCards playedCards, PlayedCards previousCards) {
        if (previousCards == null || previousCards.getCards().isEmpty()) {
            if (hasCard(Card.START_CARD) && !playedCards.getCards().contains(Card.START_CARD)) {
                System.out.println("You must play 3 of diamonds on the first turn!");
                return false;
            }
        }
        if (!playedCards.isValidSize()) {
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

    /**
     * Getter method for Player Hand object
     * 
     * @return Player's Hand object
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Getter method for Player name
     * 
     * @return Player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for Player's points
     * 
     * @return number of points player has as a double
     */
    public double getPoints() {
        return this.points;
    }

    /**
     * setter method for Player's points
     * Adds points to the player
     * 
     * @param add how many points to increase player points by, positive value
     */
    public void addPoints(double add) {
        if (add > 0) {
            points += add;
        }
    }

    /**
     * setter method for Player's points
     * deducts points from the player
     * 
     * @param deduct how many points to decrease player points by, positive value
     */
    private void deductPoints(double deduct) {
        if (deduct > 0) {
            points -= deduct;
        }
    }

    /**
     * Called when player loses a game to decide how many points to deduct
     * default is number of cards player has left times the rate
     * if number of cards left >= 10 multiply the deduction by 3
     * else if number of cards left >=7 and <10 multiply the deduction by 2
     * else keep the default deduction rate
     * deduct the points calcluated from the player
     * 
     * @param rate multiplier to decide how many points to deduct
     * @return points dedcucted from player as a double
     */
    private double loseGame(double rate) {
        // double deduct = rate * getNumOfCards();
        int numCards = getNumOfCards();
        double deduct = numCards * rate;
        if (numCards >= 10) {// If num of Cards greater then rate *3 loss
            deduct *= 3;
        } else if (numCards >= 7) {// If num of cards equal to rate then *2 loss
            deduct *= 2;
        }
        deductPoints(deduct);
        return deduct;
    }

    /**
     * Getter method to return cards in a Player's hand
     * 
     * @return number of cards in player's hand
     */
    public int getNumOfCards() {
        return hand.getSize(); // Pass player to getSize()
    }

    /**
     * Setter method to add a card to player's hand
     * 
     * @param card Card to add to hand
     */
    public void receiveCard(Card card) {// Adds card to hand
        hand.addCard(card);
    }

    /**
     * validator method to check if player's hand contains a card
     * 
     * @param card Card to check for
     * @return returns false if card is null or card not in hand
     *         returns true if card is in hand
     */
    protected boolean hasCard(Card card) {
        if (card == null) {
            return false;
        }

        return hand.getCards().contains(card);
    }

    /**
     * Static method to deduct points from players who didn't win and award it to
     * the winning
     * player
     * 
     * @param playerList List of all players
     * @param winner     the winner who will be given the points deducted from the
     *                   losers
     * @param rate       the default multiplier that the points will be deducted
     *                   using
     */
    public static void winRound(List<Player> playerList, Player winner, double rate) {
        double winnings = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).equals(winner)) {
                Player player = playerList.get(i);
                winnings += player.loseGame(rate);
            }
        }
        winner.addPoints(winnings);
    }

    /**
     * Comparator method to sort Players by points in descending order
     * 
     */
    public static Comparator<Player> sortByPoints() {
        return Comparator.comparingDouble(Player::getPoints).reversed();
    }
}
