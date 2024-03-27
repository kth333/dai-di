import java.util.*;

public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;

    private static Player currentPlayer = null;

    private int round;

    public void startGame(String firstPlayerName, Scanner scanner) {
        List<Player> players = getPlayers(firstPlayerName, scanner);
        for (Player player : players) {
            player.addPoints(100);
        }
        playRounds(players, 1, scanner);
        displayFinalWinner(players);
    }

    private void playRounds(List<Player> players, int round, Scanner scanner) {
        if (round > 5 || (round > 1 && !continueGame(scanner))) {
            System.out.println("Bye Bye!");
            return;
        }
        this.round = round; // Update the class variable
        Deck deck = new Deck();
        deck.shuffle();
        deck.distributeCards(players, CARDS_PER_PLAYER);
        List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);
        displayPlayerOrder(playerOrder);
        playTurns(players, playerOrder, 1, null, 0, round, scanner);
        displayRanking(players);
        clearHands(players);
        playRounds(players, round + 1, scanner);
    }

    private void playTurns(List<Player> players, List<Player> playerOrder, int turn, PlayedCards previousCards, int consecutivePasses, int round, Scanner scanner) {
        currentPlayer = playerOrder.get((turn - 1) % 4);
        System.out.println("\nRound: " + round + " Turn: " + turn);
        System.out.println(currentPlayer.getName() + "'s turn!");
        
        PlayResult playResult = new PlayResult(currentPlayer, null, 0, 1);
        
        if (currentPlayer instanceof EasyBot) {
            playResult = ((EasyBot) currentPlayer).play(previousCards, consecutivePasses, turn);
        } else if (currentPlayer instanceof HardBot) {
            playResult = ((HardBot) currentPlayer).play(previousCards, consecutivePasses, turn);
        } else {
            System.out.println("\n" + currentPlayer.getName() + "'s Hand: " + currentPlayer.getHand());
            playResult = currentPlayer.play(previousCards, consecutivePasses,turn, scanner);
        }
        
        if (findRoundWinner(currentPlayer) != null) {
            // Display winner
            System.out.println("\n" + currentPlayer.getName() + " won Round " + round + "!");

            // Point calculations
            Player.winGame(playerOrder, currentPlayer, 1);
            return;
        }

        PlayedCards newPreviousCards = playResult.getPreviousCards();
        
        int newConsecutivePasses = playResult.getConsecutivePasses();
        
        if (newConsecutivePasses >= 3) {
            newPreviousCards = null;
        }

        playTurns(players, playerOrder, turn + 1, newPreviousCards, newConsecutivePasses, round, scanner);
    }
    
    private boolean continueGame(Scanner scanner) {
        System.out.println("Continue next round? (y/n)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("n")) {
            return false;
        } else if (!input.equalsIgnoreCase("y")) {
            System.out.println("Invalid input. Please type 'y' or 'n'.");
            return continueGame(scanner);
        }
        return true;
    }

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

    private List<Player> playerOrder(List<Player> playerList, int numPlayers) {
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

    private void displayPlayerOrder(List<Player> playerOrder) {
        // to display player order again
        System.out.println("\nTurn order is:");
        for (int i = 0; i < playerOrder.size() - 1; i++) {
            Player player = playerOrder.get(i);
            String playerName = player.getName();
            System.out.printf("%s -> ", playerName);
        }

        System.out.printf("%s.", playerOrder.getLast().getName());
    }

    private void displayFinalWinner(List<Player> players) {
        // System.out.println("\n" + players.get(0).getName() + " won the game! Good job!");
        Collections.sort(players, Player.sortByPoints());
        System.out.println("\n" + players.get(0).getName() + " won the game! Good job!");
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

    private void clearHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().clear();
        }
    }

    private Player findRoundWinner(Player currentPlayer) {
        if (currentPlayer.getHand().isEmpty()) {
            return currentPlayer;
        }
        return null; // No winner found
    }
}