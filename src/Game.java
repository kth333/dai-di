import java.util.*;

public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;
    private static final Scanner scanner = new Scanner(System.in);

    public void startGame(Player humanPlayer, Player bot1, Player bot2, Player bot3) {
        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();

        System.out.println("\nPlayers: " + humanPlayer.getName() + ", " + bot1.getName() + ", " + bot2.getName() + ", "
                + bot3.getName());

        // Give 100 points to each player at the start of the game
        List<Player> players = Arrays.asList(humanPlayer, bot1, bot2, bot3);
        for (Player player : players) {
            player.addPoints(100);
        }

        // Distribute cards to players
        deck.distributeCards(players, CARDS_PER_PLAYER);

        // Determine the player with the 3 of diamonds to start the round
        List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);
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
            System.out.println("\nRound: " + round + " Turn: " + turn);
            if (currentPlayer.equals(humanPlayer)) {
                System.out.println("Your turn!");
                System.out.println("\nYour Hand: " + currentPlayer.getHand());
            } else {
                System.out.println(currentPlayer.getName() + "'s turn!");
            }
            playResult = currentPlayer.play(currentPlayer, previousCards, consecutivePasses, turn);
            previousCards = playResult.getPreviousCards();
            consecutivePasses = playResult.getConsecutivePasses();
            // Check if the player passed
            if (consecutivePasses >= 3) {
                previousCards = null;
            }
            turn++;
            round += (turn - 1) / playerOrder.size();
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

    private static void displayPlayerOrder(List<Player> playerOrder) {
        for (int i = 0; i < playerOrder.size(); i++) {
            Player player = playerOrder.get(i);
            String playerName = null;
            if (player != null) {
                playerName = player.getName();
            } else {
                playerName = "Missing";
            }
            System.out.println("Player " + i + " is " + playerName);
        }
    }

    private static Player getNextPlayer(Player currentPlayer, List<Player> players) {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size(); // Wrap around to the beginning if at the end
        return players.get(nextIndex);
    }

    private static Player findWinner(List<Player> playerList) {
        for (Player player : playerList) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }
        return null; // No winner found
    }
}