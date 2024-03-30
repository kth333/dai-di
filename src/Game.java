package src;

import java.util.*;

/**
 * Represents a game controller for managing the flow of the card game.
 */
public class Game {

    private static final int NUM_PLAYERS = 4; // Number of players is always 4
    private static final int CARDS_PER_PLAYER = 13; // Number of cards given to each player at the start is always 13
    private static final int NUM_ROUNDS = 5; // Number of rounds in a game is always 5

    private static Player currentPlayer = null; // Represents the current player taking their turn in the game
    private int round = 1; // Tracks the current round number in the game
    private boolean quitGame = false; // Flag to track whether player quit game

    /**
     * Starts the game with the specified first player name and scanner object for
     * input.
     *
     * @param firstPlayerName the name of the first player
     * @param scanner         the scanner object for input
     */
    public void startGame(String firstPlayerName, Scanner scanner) {
        List<Player> players = getPlayers(firstPlayerName, scanner); // Get the list of players for the game
        displayPlayerNames(players);     // Display the names of all players participating in the game
        giveInitialPoints(players);     // Give 100 points to each player before starting the game
        startRound(players, round, scanner); // Start round 1 of the game
        displayFinalWinner(players); // Display final winner after all 5 rounds are completed
    }

    /**
    * Starts the round and calls itself recursively to start the next round
    * after the current round is completed.
    *
    * @param players The list of player objects playing the game.
    * @param round Integer representing the current round.
    * @param scanner scanner object for input.
    */
    private void startRound(List<Player> players, int round, Scanner scanner) {
        this.round = round; // Updates round variable to the current round number

        // Ends the game if current round is greater than 5 or if player chose to stop playing
        if (round > NUM_ROUNDS || quitGame || !continueNextRound(round, scanner)) {
            System.out.println("Thank you for playing!"); 
            return;
        }

        Deck deck = new Deck(); // Create new deck object
        deck.shuffle(); // Shuffles the deck randomly
        deck.distributeCards(players, CARDS_PER_PLAYER); // Distributes 13 cards to each player
        List<Player> playerOrder = determinePlayerOrder(players, NUM_PLAYERS); // Determines order of the players
        displayPlayerOrder(playerOrder); // Displays order of the players
        startTurn(playerOrder, 1, null, 0, scanner); // Starts round from turn 1 until round is completed
        
        displayRanking(players); // Displays ranking of the players after round is completed
        clearHands(players); // Clears the hand of each player for the next round
        startRound(players, round + 1, scanner); // Starts the next round, round number is incremented by 1
    }
    
    /**
     * Prompts the user to select the number of human players and enters their
     * names.
     *
     * @param firstPlayerName String representing the name of the first player.
     * @param scanner         the scanner object for input.
     * @return the list of Player objects in the game.
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
    * Starts the turn and calls itself recursively to start the next turn
    * after the current turn is completed.
    *
    * @param playerOrder The list of player objects sorted according to their order in the current round.
    * @param previousCards The PlayedCards object representing the cards played in the previous turn
    * @param consecutivePasses Integer representing the number of consecutive passes made in the previous turns.
    * @param scanner scanner object for input.
    */
    private void startTurn(List<Player> playerOrder, int turn, PlayedCards previousCards, int consecutivePasses,
            Scanner scanner) {
        currentPlayer = playerOrder.get((turn - 1) % NUM_PLAYERS); // Find current player according to the player order
        System.out.println("\nRound: " + round + " Turn: " + turn); // Print the current round and turn number
        System.out.println(currentPlayer.getName() + "'s turn!"); // Print the current player's name to show whose turn it is

        // Player makes their move and playResult is determined accordingly
        PlayResult playResult = determinePlayerAction(currentPlayer, previousCards, consecutivePasses, scanner); 

        // Set quitGame flag to true and return if the player chose to quit the game
        if (playResult.isQuit()) {
            quitGame = true;
            return;
        }
        
        // Check if current player has won the round after making their play
        if (findRoundWinner(currentPlayer) != null) {
            // Display winner's name and congratulate the winner
            System.out.println("\n" + currentPlayer.getName() + " won Round " + round + "!");
            
            // Point calculations according to the number of cards left in each player's hand
            Player.winRound(playerOrder, currentPlayer, 1);
            return;
        }

        // newPreviousCards and newConsecutivePasses determined from the play result of the current turn
        PlayedCards newPreviousCards = playResult.getPreviousCards();
        int newConsecutivePasses = playResult.getConsecutivePasses();
        
        // Set newPreviousCards to null if 3 or more players passed consecutively
        // so that next player can play any valid combination
        if (newConsecutivePasses >= 3) {
            newPreviousCards = null;
        }
        
        // Starts the next turn passing in the new previous cards and consecutive passes, and incrementing turn number by 1
        startTurn(playerOrder, turn + 1, newPreviousCards, newConsecutivePasses, scanner);
    }
    
    /**
     * Prints the names of the players playing the game.
     * 
     * @param players The list of Player objects playing the game.
     */
    private void displayPlayerNames(List<Player> players) {
        System.out.print("\nPlayers: ");
        for (int i = 0; i < NUM_PLAYERS - 1; i++) {
            System.out.print(players.get(i).getName() + ", ");
        }
        System.out.print(players.getLast().getName() + "\n");
    }
    
    /**
     * Gives 100 points to each player at the start of a game.
     * 
     * @param players The list of Player objects playing the game.
     */
    private void giveInitialPoints(List<Player> players) {
        for (Player player : players) {
            player.addPoints(100);
        }
    }
    
    /**
     * After all 5 rounds are completed, displays the final winner of the game (player with highest points)
     * 
     * @param players List of Player objects playing the game.
     */
    private void displayFinalWinner(List<Player> players) {
        if (!quitGame) {
            Collections.sort(players, Player.sortByPoints());
            System.out.println("\n" + players.get(0).getName() + " won the game! Good job!");
        }
    }
    
    /**
     * From round 2 onwards, prompts the user if they wish to continue to the next round
     * or to stop playing after a round is completed.
     * 
     * @param round Integer representing the round that was just completed.
     * @param scanner the scanner object for input.
     * @return False if player inputs 'n', True if player inputs 'y'.
     */
    private boolean continueNextRound(int round, Scanner scanner) {
        if (round > 1) {
            System.out.println("Continue next round? ('y' to continue / 'n' to quit game)");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                return false;
            } else if (!input.equalsIgnoreCase("y")) {
                System.out.println("Invalid input. Please type 'y' or 'n'.");
                return continueNextRound(round, scanner);
            }
        }
        return true;
    }

    /**
     * Determines the order of players for the current round based on their
     * presence of the 3 of diamonds card and randomly assigns turn order for
     * other players.
     * 
     * @param playerList The list of Player objects playing the game.
     * @param numPlayers Integer representing the number of players in the game.
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
    * Displays the ranking of players and their number of remaining cards after a round is completed.
    * 
    * @param players The list of Player objects playing the game.
    */
    private void displayRanking(List<Player> players) {
        Collections.sort(players, Player.sortByPoints());
        System.out.println("\nRank\tName\t\tTotal Points\tCards Left");

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%-6d\t%-15s\t%-13.1f\t%-5d\n", (i + 1), player.getName(), player.getPoints(),
            player.getNumOfCards());
        }
    }
    
    /**
    * Clears the hand of each player after a round is completed, to prepare for the next round.
    * 
    * @param players The list of Player objects playing the game.
    */
    private void clearHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().clear();
        }
    }
    
    /**
    * Checks if the current player is an easy bot, hard bot or human player, then calls their play method.
    * 
    * @param currentPlayer The player who makes their play in the current turn.
    * @param previousCards The PlayedCards object representing cards played in the previous turn.
    * @param consecutivePasses An integer representing the number of consecutive passes made in the previous turns.
    * @return The PlayResult object representing the result of the play made by the current player.
    */
    private PlayResult determinePlayerAction(Player currentPlayer, PlayedCards previousCards, int consecutivePasses,
            Scanner scanner) {
        PlayResult playResult;

        if (currentPlayer instanceof EasyBot) {
            playResult = ((EasyBot) currentPlayer).play(previousCards, consecutivePasses);
        } else if (currentPlayer instanceof HardBot) {
            playResult = ((HardBot) currentPlayer).play(previousCards, consecutivePasses);
        } else {
            System.out.println("\n" + currentPlayer.getName() + "'s Hand: " + currentPlayer.getHand());
            playResult = currentPlayer.play(previousCards, consecutivePasses, scanner);
        }

        return playResult;
    }

    /**
    * Finds the winner of the current round based on 
    * whether the current player's hand is empty after playing their cards.
    * 
    * @param currentPlayer The Player object who made their play in the current turn.
    * @return The same Player object representing the winner of the round, or null if
    *         no winner is found.
    */
    private Player findRoundWinner(Player currentPlayer) {
        if (currentPlayer.getHand().isEmpty()) {
            return currentPlayer;
        }
        return null; // No winner found
    }
}
