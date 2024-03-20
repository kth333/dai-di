import java.util.*;

public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;

    public void startGame(String firstPlayerName,Scanner scanner) {
        List<Player> players=getPlayers(firstPlayerName, scanner);

        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();

        // System.out.println("\nPlayers: " + players.getName() + ", " + bot1.getName()
        // + ", " + bot2.getName() + ", "
        // + bot3.getName());

        System.out.print("\nPlayers: ");
        for (int i = 0; i < NUM_PLAYERS - 1; i++) {
            System.out.print(players.get(i).getName() + ", ");
        }
        System.out.print(players.getLast().getName() + "\n");

        // Give 100 points to each player at the start of the game
        for (Player player : players) {
            player.addPoints(100);
        }

        // Distribute cards to players
        deck.distributeCards(players, CARDS_PER_PLAYER);

        // Determine the player with the 3 of diamonds to start the round
        List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);

        // show order of players
        displayPlayerOrder(playerOrder);

        System.out.println("\n" + playerOrder.get(0).getName() + " starts the round!");

        Player winner = null;
        Player currentPlayer = playerOrder.get(0);
        PlayedCards previousCards = null; // Initialize previous cards
        int turn = 1;
        int round = 1;
        int consecutivePasses = 0;
        PlayResult playResult = new PlayResult(previousCards, consecutivePasses);

        // Game loop
        while (winner == null) {
            //quit game if choose q
            if (playResult.isQuit()) {
                //System.out.println("Quit flag detected, quitting...");
                quitGame(); 
                return; // exit the startGame method, ending the game
            }
            
            System.out.println("\nRound: " + round + " Turn: " + turn);
            System.out.println(currentPlayer.getName() + "'s turn!");
            if (currentPlayer instanceof Bot) {
                Bot bot = (Bot)currentPlayer;
                playResult = bot.play(currentPlayer, previousCards, consecutivePasses);
            } else{
                System.out.println("\n"+currentPlayer.getName()+"'s Hand: " + currentPlayer.getHand());
                playResult = currentPlayer.play(currentPlayer, previousCards, consecutivePasses, scanner);
            }

            previousCards = playResult.getPreviousCards();
            // get number of times passed in round so far
            consecutivePasses = playResult.getConsecutivePasses();
            if (consecutivePasses >= 3) {
                previousCards = null;
            }
            turn++;
            round += (turn - 1) / NUM_PLAYERS;
            if ((turn - 1) % NUM_PLAYERS == 0) {
                // Reset the turn to 1
                turn = 1;
            }
            // Look for a winner
            winner = findWinner(playerOrder);
            // Switch to the next player
            currentPlayer = getNextPlayer(currentPlayer, playerOrder);
        }

        // Display winner
        System.out.println("\n" + winner.getName() + " wins!");

        // Point calculations
        Player.winGame(playerOrder, winner, 1);

        // Show ranking of players
        Collections.sort(players, Player.sortByPoints());
        System.out.println("Rank\tName\t\tPoints\t\tCards Left");

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%-6d\t%-15s\t%-5.1f\t\t%-5d\n", (i + 1), player.getName(), player.getPoints(),
                    player.getNumOfCards());
        }
    }

    public List<Player> getPlayers(String firstPlayerName,Scanner scanner) {
        int players = 1;
        do {
            System.out.print("Select number of human players: ");
            players = scanner.nextInt();
            scanner.nextLine();
            if (players < 1 || players > NUM_PLAYERS) {
                System.out.println("Invalid player number! Player number is only 1 to 4.");
            } else {
                break;
            }
        } while (true);

        List<Player> playerList = new ArrayList<Player>(Arrays.asList(new Player(firstPlayerName)));
        List<String> playerNames = new ArrayList<String>(Arrays.asList(firstPlayerName));
        // Create human players
        for (int i = 2; i <= players; i++) {
            String name = null;
            do {
                System.out.print("Enter player " + i + " name: ");
                name = scanner.nextLine();
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

    private static List<Player> playerOrder(List<Player> playerList, int numPlayers) {
        // Set turn order of players
        Random random = new Random();
        if (playerList == null || numPlayers < 1) {
            return null;
        }

        Player[] playerOrder = new Player[numPlayers];

        // Set player Order
        for (int i = 0; i < numPlayers; i++) {
            // Get first player
            Player player = playerList.get(i);
            // Check if has starting Card three of Diamonds
            if (player.hasCard(new Card(Card.Suit.DIAMONDS, Card.Rank.THREE))) {
                // If have startCard assign as first player
                // Assumes that all 52 cards are dealt out properly
                playerOrder[0] = player;
            } else {
                // If not set their turn order as one of the other positions
                boolean turnOrderSet = false;
                // Set a do loop until turn order is successfully set
                do {
                    // random.nextInt(max-1) will generate random number from 0 to max-2
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

    private static void displayPlayerOrder(List<Player> playerOrder) {
        // to display player order again
        System.out.println("Turn order is:");
        for (int i = 0; i < playerOrder.size() - 1; i++) {
            Player player = playerOrder.get(i);
            String playerName = player.getName();
            System.out.printf("%s then ", playerName);
        }
        System.out.printf("%s.", playerOrder.getLast().getName());
    }

    private static Player getNextPlayer(Player currentPlayer, List<Player> players) {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size(); // Wrap around to the beginning if at the end

        return players.get(nextIndex);
    }

    private static Player findWinner(List<Player> playerList) {
        for (Player player : playerList) {
            // search playerList for an empty hand
            if (player.getHand().isEmpty()) {
                return player;// Player with empty hand is winner
            }
        }
        return null; // No winner found
    }

    private static void quitGame() {
        System.out.println("\nBye Bye!");
    }
}
