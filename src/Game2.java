import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a game controller for managing the flow of the card game.
 */

public class Game2 {
    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;
    private static final String CONTINUE_PROMPT = "Continue next round? (y/n): ";
    private static final String BYE_MESSAGE = "Bye Bye!";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please type 'y' or 'n'.";
    private static final String PLAYER_PROMPT = "Select number of human players (1-4): ";
    private static final String DIFFICULTY_PROMPT = "Select difficulty (easy or hard): ";
    private static final String NAME_PROMPT = "Enter player %d name (up to 16 characters): ";
    private static final String INVALID_NAME_MESSAGE = "Invalid name try again!";
    private static final String PLAYER_NAME_TOO_LONG = "Enter a shorter name!";

    private List<Player> players;
    private Scanner scanner;

    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.players = new ArrayList<>();
    }

    /*
     * Starts the game with the specified first player name and scanner object for
     * input.
     *
     * @param firstPlayerName the name of the first player
     * 
     * @param scanner the scanner object for input
     */
    public void startGame(String firstPlayerName, Scanner scanner) {
        List<Player> players = initializePlayers(firstPlayerName, scanner);
        displayPlayers(players);

        // Initial points allocation
        allocateInitialPoints(players);

        // Game rounds
        for (int round = 1; round <= 5; round++) {
            if (!promptForNextRound(scanner, round))
                return;

            // Setup and play round
            if (!setupAndPlayRound(players, scanner, round))
                return;

            // End of round processing
            processRoundEnd(players, round);
        }
        // End of game
        displayGameWinner(players);
    }
}

private void initializePlayers(String firstPlayerName) {

}

private void displayPlayers(){
    System.out.println("\nPlayers: " + players.stream().map(Player::getName().collect(Collectors.joining(","))));
}

private void allocateInitialPoints() {
    players.forEach(player -> player.addPoints(100));
}

private boolean promptNextRound(int round) {
    if (round > 1) {
        System.out.print(CONTINUE_PROMPT);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("n")) {
            System.out.println(BYE_MESSAGE);
            return false;
        }
    } else if (!input.equalsIgnoreCase("y")) {
        System.out.println(INVALID_INPUT_MESSAGE);
        return false;
    }
    return true;
}

private void setupAndPlayRound(int round) {

}

private void displayGameWinner() {

}

/**
 * gets the player whose turn it is if it is not a bot
 *
 * @return the Player instance of current player
 */
public Player getCurrentPlayer() {
    if (currentPlayer instanceof Bot) {
        return null;
    }
    return currentPlayer;
}

/**
 * Prompts the user to select the number of human players and enters their
 * names.
 *
 * @param firstPlayerName the name of the first player
 * @param scanner         the scanner object for input
 * @return the list of players in the game
 */
public List<Player> getPlayers(String firstPlayerName, Scanner scanner) {
    int numHumanPlayers = 1;
    do {
        System.out.print("Select number of human players: ");
        try {
            numHumanPlayers = Integer.parseInt(scanner.nextLine());
            if (numHumanPlayers < 1 || numHumanPlayers > NUM_PLAYERS) {
                System.out.println("Invalid player number! Player number is only 1 to 4.");
            } else {
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter 1, 2, 3 or 4.");
        }
    } while (true);

    boolean isEasyMode = true;
    if (numHumanPlayers != 4) {
        while (true) {
            System.out.print("Select difficulty (easy or hard): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("easy")) {
                isEasyMode = true;
                break;
            } else if (input.equalsIgnoreCase("hard")) {
                isEasyMode = false;
                break;
            } else {
                System.out.println("Invalid input! Please enter 'easy' or 'hard'.");
            }
        }
    }

    List<Player> playerList = new ArrayList<Player>(Arrays.asList(new Player(firstPlayerName)));
    List<String> playerNames = new ArrayList<String>(Arrays.asList(firstPlayerName));
    // Create human players
    for (int i = 2; i <= numHumanPlayers; i++) {
        String name = null;
        do {
            System.out.print("Enter player " + i + " name (up to 16 characters): ");
            name = scanner.nextLine();

            if (name.length() > 16) {
                System.out.println("Enter a shorter name!");
                continue; // Prompt again
            }

            if (name != null && name.length() > 0 && !playerNames.contains(name)) {
                playerNames.add(name);
                playerList.add(new Player(name));
                break;
            } else {
                System.out.println("Invalid name try again!");
            }
        } while (true);
    }

    if (numHumanPlayers < NUM_PLAYERS) {
        for (int i = 0; i < 4 - numHumanPlayers; i++) {
            if (isEasyMode) {
                playerList.add(new EasyBot(playerNames, Bot.usedNames));
            } else {
                playerList.add(new HardBot(playerNames, Bot.usedNames));
            }
        }
    }
    return playerList;
}

/**
 * Determines the order of players for the current round based on their
 * presence of the 3 of diamonds card and randomly assigns turn order for
 * other players.
 * 
 * @param playerList The list of players.
 * @param numPlayers The number of players in the game.
 * @return A list of Player objects representing the turn order for the
 *         current round.
 */
private static List<Player> playerOrder(List<Player> playerList, int numPlayers) {
    Random random = new Random();
    if (playerList == null || numPlayers < 1) {
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
                // nextInt(max-min) will generate random number from 0 to max-2
                // max - min sets max-2 to be highest random int generated
                // +1 will ensure that it will never be 0
                int position = random.nextInt((numPlayers - 1)) + 1;
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
/*
 * Displays the turn order of players for the current round.
 * 
 * @param playerOrder The list of Player objects representing the turn order.
 */

/**
 * Displays the turn order of players for the current round.
 * 
 * @param playerOrder The list of Player objects representing the turn order.
 */

private static void displayPlayerOrder(List<Player> playerOrder) {
    // to display player order again
    System.out.println("\nTurn order is:");
    for (int i = 0; i < playerOrder.size() - 1; i++) {
        Player player = playerOrder.get(i);
        String playerName = player.getName();
        System.out.printf("%s -> ", playerName);
    }
    System.out.printf("%s.", playerOrder.getLast().getName());

}

/**
 * Retrieves the next player in the turn order after the current player.
 * 
 * @param currentPlayer The current player.
 * @param players       The list of Player objects representing the turn order.
 * @return The next player in the turn order.
 */

private static Player getNextPlayer(Player currentPlayer, List<Player> players) {
    int currentIndex = players.indexOf(currentPlayer);
    int nextIndex = (currentIndex + 1) % players.size(); // Wrap around to the beginning if at the end
    return players.get(nextIndex);
}

/**
 * Finds the winner of the current round based on the empty hands of players.
 * 
 * @param playerList The list of players.
 * @return The Player object representing the winner of the round, or null if
 *         no winner is found.
 */

private static Player findRoundWinner(List<Player> playerList) {
    for (Player player : playerList) {
        if (player.getHand().isEmpty()) {
            return player;
        }
    }
    return null; // No winner found
}
