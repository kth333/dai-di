import java.util.*;

/**
 * Represents a game controller for managing the flow of the card game.
 */
public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;
    private static final int NUM_ROUNDS = 5;

    private static Player currentPlayer = null;
    private int round = 1;


    /**
     * Starts the game with the specified first player name and scanner object for
     * input.
     *
     * @param firstPlayerName the name of the first player
     * @param scanner         the scanner object for input
     */

    public void startGame(String firstPlayerName, Scanner scanner) {
        List<Player> players = getPlayers(firstPlayerName, scanner);
        displayPlayerNames(players);
        giveInitialPoints(players);
        startRound(players, round, scanner);
        displayFinalWinner(players);
    }

    private void startRound(List<Player> players, int round, Scanner scanner) {
        if (round > NUM_ROUNDS || (round > 1 && !continueNextRound(scanner))) {
            System.out.println("Thank you for playing!");
            return;
        }
        this.round = round;
        Deck deck = new Deck();
        deck.shuffle();
        deck.distributeCards(players, CARDS_PER_PLAYER);
        List<Player> playerOrder = determinePlayerOrder(players, NUM_PLAYERS);
        displayPlayerOrder(playerOrder);
        startTurn(playerOrder, 1, null, 0, scanner);
        displayRanking(players);
        clearHands(players);
        startRound(players, round + 1, scanner);
    }

    private void startTurn(List<Player> playerOrder, int turn, PlayedCards previousCards, int consecutivePasses, Scanner scanner) {
        currentPlayer = playerOrder.get((turn - 1) % NUM_PLAYERS);
        System.out.println("\nRound: " + round + " Turn: " + turn);
        System.out.println(currentPlayer.getName() + "'s turn!");
        
        PlayResult playResult = new PlayResult(null, 0);
        
        if (currentPlayer instanceof EasyBot) {
            playResult = ((EasyBot) currentPlayer).play(currentPlayer, previousCards, consecutivePasses);
        } else if (currentPlayer instanceof HardBot) {
            playResult = ((HardBot) currentPlayer).play(currentPlayer, previousCards, consecutivePasses);
        } else {
            System.out.println("\n" + currentPlayer.getName() + "'s Hand: " + currentPlayer.getHand());
            playResult = currentPlayer.play(currentPlayer, previousCards, consecutivePasses, scanner);
        }
        
        if (findRoundWinner(currentPlayer) != null) {
            // Display winner
            System.out.println("\n" + currentPlayer.getName() + " won Round " + round + "!");

            // Point calculations
            Player.winRound(playerOrder, currentPlayer, 1);
            return;
        }

        PlayedCards newPreviousCards = playResult.getPreviousCards();
        
        int newConsecutivePasses = playResult.getConsecutivePasses();
        
        if (newConsecutivePasses >= 3) {
            newPreviousCards = null;
        }

        startTurn(playerOrder, turn + 1, newPreviousCards, newConsecutivePasses, scanner);
    }
    
    private void displayFinalWinner(List<Player> players) {
        Collections.sort(players, Player.sortByPoints());
        System.out.println("\n" + players.get(0).getName() + " won the game! Good job!");
    }

    private boolean continueNextRound(Scanner scanner) {
        System.out.println("Continue next round? (y/n)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("n")) {
            return false;
        } else if (!input.equalsIgnoreCase("y")) {
            System.out.println("Invalid input. Please type 'y' or 'n'.");
            return continueNextRound(scanner);
        }
        return true;
    }

    private Player findRoundWinner(Player currentPlayer) {
        if (currentPlayer.getHand().isEmpty()) {
            return currentPlayer;
        }
        return null; // No winner found
    }

    private void displayPlayerNames(List<Player> players) {
        System.out.print("\nPlayers: ");
        for (int i = 0; i < NUM_PLAYERS - 1; i++) {
            System.out.print(players.get(i).getName() + ", ");
        }
        System.out.print(players.getLast().getName() + "\n");
    }

    private void displayRanking(List<Player> players) {
        Collections.sort(players, Player.sortByPoints());
        System.out.println("\nRank\tName\t\tTotal Points\tCards Left");

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%-6d\t%-15s\t%-13.1f\t%-5d\n", (i + 1), player.getName(), player.getPoints(),
                    player.getNumOfCards());
        }
    }

    private void giveInitialPoints(List<Player> players) {
        for (Player player : players) {
            player.addPoints(100);
        }
    }
 
    private void clearHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().clear();
        }
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
    private static List<Player> determinePlayerOrder(List<Player> playerList, int numPlayers) {
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
     * Finds the winner of the current round based on the empty hands of players.
     * 
     * @param playerList The list of players.
     * @return The Player object representing the winner of the round, or null if
     *         no winner is found.
     */

}
