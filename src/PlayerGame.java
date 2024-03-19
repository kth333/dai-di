import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerGame {
    // To be moved to CardGamePlayer
    public static void winGame(List<Player> playerList, int winner, double rate) {
        double winnings = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (i != winner) {
                Player player = playerList.get(i);
                winnings += player.loseGame(rate);
            }
        }
        playerList.get(winner).addPoints(winnings);
        ;
    }

    public static List<Player> playerOrder(List<Player> playerList, Card startCard, int numPlayers) {
        if (playerList==null || startCard==null || numPlayers<1){
            //may throw exception here
            return null;
        }
        List<Player> playerOrder = new ArrayList<>();

        // populate playerOrder list positions
        for (int i = 0; i < numPlayers; i++) {
            playerOrder.add(null);
        }

        // Set player Order
        for (int i = 0; i < numPlayers; i++) {
            // Get first player
            Player player = playerList.get(i);
            // Check if has startCard
            if (player.has(startCard)) {
                // If have startCard assign as first player
                // Assumes that all 52 cards are dealt out properly
                playerOrder.set(0, player);
            } else {
                // If not set their turn order as one of the other positions
                boolean turnOrderSet = false;
                // Set a do loop until turn order is successfully set
                do {
                    // nextInt(1,numPlayers) will generate random number inclusive 1 and exclusive
                    // numPlayers
                    int position = ThreadLocalRandom.current().nextInt(1, numPlayers);
                    // Set the player in the position if it is empty
                    if (playerOrder.get(position) == null) {
                        playerOrder.set(position, player);
                        turnOrderSet = true;
                    }
                } while (!turnOrderSet);
            }
        }
        return playerOrder;
    }

    public static void displayPlayerOrder(List<Player> playerOrder) {
        if (playerOrder==null){
            return;
        }
        for (int i = 0; i < playerOrder.size(); i++) {
            Player player = playerOrder.get(i);
            String playerName = null;
            if (player!=null) {
                playerName = player.getName();
            } else {
                playerName = "Missing";
            }
            System.out.println("Player " + i + " is " + playerName);
        }
    }
}
