import java.util.List;
import java.util.ArrayList;

public class PlayerOrderTest {

    public static void testIsValid() {
        // Create a playerlist and populate it with Player objects
        List<Player> playerlist = new ArrayList<>();
        playerlist.add(new Player("one"));
        playerlist.add(new Player("two"));
        playerlist.add(new Player("three", 90));
        playerlist.add(new Player("four", 80));

        // Create and shuffle deck
        Deck deck = new Deck();
        int deckSize=deck.size();
        deck.shuffle();
        int NUM_PLAYERS = playerlist.size();
        
        // Distribute cards to players
        for (int i = 0; i < NUM_PLAYERS; i++) {
            Player player = playerlist.get(i);
            for (int j = i * (deckSize / NUM_PLAYERS); j < (i + 1) * (deckSize / NUM_PLAYERS); j++) {
                // hand.add(deck.get(j));
                player.receiveCard(deck.dealCard());
            }
        }
        Card startCard = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
        System.out.println(playerlist.get(0).getNumOfCards());
        System.out.println(playerlist.get(1).getNumOfCards());
        System.out.println(playerlist.get(2).getNumOfCards());
        System.out.println(playerlist.get(3).getNumOfCards());
        //Player playerTest = new Player("Test");

        List<Player> order = PlayerGame.playerOrder(playerlist, startCard, NUM_PLAYERS);

        // Call winGame
        double rate = 0.1;
        int winner = 0;
        PlayerGame.winGame(playerlist, winner, rate);

        System.out.println(order.contains(playerlist.get(0))+" Player 1 is in");
        System.out.println(order.contains(playerlist.get(1))+" Player 2 is in");
        System.out.println(order.contains(playerlist.get(2))+" Player 3 is in");
        System.out.println(order.contains(playerlist.get(3))+" Player 4 is in");
        PlayerGame.displayPlayerOrder(order);
        System.out.println(playerlist.get(0).getPoints()+" player 1 Value");
        System.out.println(playerlist.get(1).getPoints()+" player 2 Value");
        System.out.println(playerlist.get(2).getPoints()+" player 3 Value");
        System.out.println(playerlist.get(3).getPoints()+" player 4 Value");
        System.out.println("Expected Value player 1="+(100+13*rate*3));
        System.out.println("Expected Value player 2="+(100-13*rate));
        System.out.println("Expected Value player 3="+(90-13*rate));
        System.out.println("Expected Value player 4="+(80-13*rate));
    }

    public static void main(String[] args) {
        testIsValid();
    }
}
