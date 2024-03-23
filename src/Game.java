import java.util.*;
/**
 * Represents a game controller for managing the flow of the card game.
 */
public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;

     /**
     * Starts the game with the specified first player name and scanner object for input.
     *
     * @param firstPlayerName the name of the first player
     * @param scanner         the scanner object for input
     */

    public void startGame(String firstPlayerName, Scanner scanner) {
        List<Player> players = getPlayers(firstPlayerName, scanner);
        System.out.print("\nPlayers: ");
        for (int i = 0; i < NUM_PLAYERS - 1; i++) {
            System.out.print(players.get(i).getName() + ", ");
        }
        System.out.print(players.getLast().getName() + "\n");

        // Give 100 points to each player at the start of the game
        for (Player player : players) {
            player.addPoints(100);
        }

        for (int round = 0; round <= 5;) {
            if (round >= 1) {
                System.out.println("Continue next round? (y/n)");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("n")) {
                    // If the user types 'n', break out of the loop to end the game
                    quitGame();
                    return;
                } else if (!input.equalsIgnoreCase("y")) {
                    // If the user doesn't type 'y' or 'n', handle the invalid input accordingly
                    System.out.println("Invalid input. Please type 'y' or 'n'.");
                    continue;
                }
            }
            // Increment the round counter only when the user chooses to continue (otherwise it increases after an invalid input)
            round++;

            // Create and shuffle deck
            Deck deck = new Deck();
            deck.shuffle();

            // Distribute cards to players
            deck.distributeCards(players, CARDS_PER_PLAYER);

            // Determine the player with the 3 of diamonds to start the round
            List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);

            // show order of players
            displayPlayerOrder(playerOrder);

            Player roundWinner = null;
            Player currentPlayer = playerOrder.get(0);
            PlayedCards previousCards = null; // Initialize previous cards
            int turn = 1;
            int consecutivePasses = 0;
            PlayResult playResult = new PlayResult(previousCards, consecutivePasses);

            // Game loop
            while (roundWinner == null) {
                // quit game if choose q
                if (playResult.isQuit()) {
                    // System.out.println("Quit flag detected, quitting...");
                    quitGame();
                    return; // exit the startGame method, ending the game
                }

                System.out.println("\nRound: " + round + " Turn: " + turn);
                System.out.println(currentPlayer.getName() + "'s turn!");
                if (currentPlayer instanceof Bot) {
                    Bot bot = (Bot) currentPlayer;
                    playResult = bot.play(currentPlayer, previousCards, consecutivePasses);
                } else {
                    System.out.println("\n" + currentPlayer.getName() + "'s Hand: " + currentPlayer.getHand());
                    playResult = currentPlayer.play(currentPlayer, previousCards, consecutivePasses, scanner);
                }

                previousCards = playResult.getPreviousCards();
                // get number of times passed in round so far
                consecutivePasses = playResult.getConsecutivePasses();
                if (consecutivePasses >= 3) {
                    previousCards = null;
                }
                // Next turn
                turn++;

                // Look for a winner
                roundWinner = findRoundWinner(playerOrder);
                // Switch to the next player
                currentPlayer = getNextPlayer(currentPlayer, playerOrder);
            }

            // Display winner
            System.out.println("\n" + roundWinner.getName() + " won Round " + round + "!");

            // Point calculations
            Player.winGame(playerOrder, roundWinner, 1);

            // Show ranking of players
            Collections.sort(players, Player.sortByPoints());
            System.out.println("\nRank\tName\t\tTotal Points\tCards Left");

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                System.out.printf("%-6d\t%-15s\t%-13.1f\t%-5d\n", (i + 1), player.getName(), player.getPoints(),
                        player.getNumOfCards());
            }

            // Clear the hand of each player for the next round
            for (Player player : players) {
                player.getHand().clear();
            }
        }
        // Display winner of the game after all 5 rounds are completed
        System.out.println("\n" + players.get(0).getName() + " won the game! Good job!");
    }
    /**
     * Prompts the user to select the number of human players and enters their names.
     *
     * @param firstPlayerName the name of the first player
     * @param scanner         the scanner object for input
     * @return the list of players in the game
     */
    public List<Player> getPlayers(String firstPlayerName, Scanner scanner) {
        int players = 1;
        do {
            System.out.print("Select number of human players: ");
            try {
                players = Integer.parseInt(scanner.nextLine());
                if (players < 1 || players > NUM_PLAYERS) {
                    System.out.println("Invalid player number! Player number is only 1 to 4.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1, 2, 3 or 4.");
            }
        } while (true);

        List<Player> playerList = new ArrayList<Player>(Arrays.asList(new Player(firstPlayerName)));
        List<String> playerNames = new ArrayList<String>(Arrays.asList(firstPlayerName));
        // Create human players
        for (int i = 2; i <= players; i++) {
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

        if (players < NUM_PLAYERS) {
            for (int i = 0; i < 4 - players; i++) {
                playerList.add(new Bot(playerNames, Bot.usedNames));
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
                    int max = numPlayers;
                    int position = random.nextInt((max - 1)) + 1;
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
      /**
     * Displays a goodbye message when the game is quit prematurely.
     */
    private static void quitGame() {
        System.out.println("\nBye Bye!");
    }
}
