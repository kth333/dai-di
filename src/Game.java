import java.util.*;
import java.util.List;

public class Game {

    private static final int NUM_PLAYERS = 4;
    private static final int CARDS_PER_PLAYER = 13;
    private static final Scanner scanner = new Scanner(System.in);
    private static int passCount = 0;
    private static PlayedCards previousCards = null;

    public static void main(String[] args) {
        // Give the user a warm welcome :)
        System.out.println("Welcome to Dai Di!");

        // Prompt the user to enter their name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        // Create the human player with the entered name
        Player humanPlayer = new Player(playerName);
        humanPlayer.addPoints(100);

        // Create three bot players with unique random names
        List<String> usedNames = new ArrayList<>();
        Player bot1 = new Player(humanPlayer.getName(), usedNames);
        Player bot2 = new Player(humanPlayer.getName(), usedNames);
        Player bot3 = new Player(humanPlayer.getName(), usedNames);
        bot1.addPoints(100);
        bot2.addPoints(100);
        bot3.addPoints(100);

        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        startGame(humanPlayer, bot1, bot2, bot3);
                        return; // Exit the loop and terminate the program
                    case 2:
                        Instructions.displayInstructions();
                        break; // Exit the loop and terminate the program
                    case 3:
                        quitGame();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice! Please enter 1, 2 or 3.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice! Please enter 1, 2 or 3.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Start game");
        System.out.println("2. Read instructions");
        System.out.println("3. Quit game");
        System.out.print("Choose an option: ");
    }

    private static void startGame(Player humanPlayer, Player bot1, Player bot2, Player bot3) {
        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println("Players: " + humanPlayer.getName() + ", " + bot1.getName() + ", " + bot2.getName() + ", "
                + bot3.getName());

        // Distribute cards to players
        List<Player> players = Arrays.asList(humanPlayer, bot1, bot2, bot3);
        deck.distributeCards(players, CARDS_PER_PLAYER);

        // List<Player> playerOrder = playerOrder(players, NUM_PLAYERS);
        // displayPlayerOrder(playerOrder); // shit takes forever to run

        // Determine the player with the 3 of diamonds to start the round
        List<Player> playerOrder = Player.playerOrder(players, NUM_PLAYERS);
        System.out.println(playerOrder.get(0).getName() + " starts the round!");

        Player winner = null;
        // Game loop
        Player currentPlayer = playerOrder.get(0);
        //PlayedCards previousCards = null; // Initialize previous cards

        while (winner == null) {
            if (currentPlayer.equals(humanPlayer)) {
                System.out.println("Your turn!");
                System.out.println("Your Hand: " + currentPlayer.getHand().getCardsInHand());
                humanTurn(currentPlayer);
            } else {
                System.out.println(currentPlayer.getName() + "'s turn!");
                botTurn(currentPlayer);
            }
            winner = findWinner(playerOrder);
            // Switch to the next player
            currentPlayer = getNextPlayer(currentPlayer, playerOrder);
        }

        // Display winner
        System.out.println(winner.getName() + " wins!");

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

    private static void humanTurn(Player currentPlayer) {
        List<Card> hand = currentPlayer.getHand().getCardsInHand();

        while (true) {
            System.out.print("Select cards to play (enter indices separated by spaces) or type p to pass: ");
            String input = scanner.nextLine();

            if (input.toLowerCase().equals("p")) {
                System.out.println(currentPlayer.getName() + " passed their turn.");
                passCount++;
                // previousCards = null; // Reset previous cards if the player passes
                return; // Exit the method if the player chooses to pass
            }

            String[] indices = input.split("\\s+");
            List<Integer> selectedIndices = new ArrayList<>();
            for (String index : indices) {
                try {
                    selectedIndices.add(Integer.parseInt(index));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter indices separated by spaces.");
                    break; // Continue to prompt the player for valid input
                }
            }

            // Validate selected indices
            boolean validSelection = true;
            List<Card> selectedCards = new ArrayList<>();
            for (int index : selectedIndices) {
                if (index < 0 || index >= hand.size()) {
                    System.out.println("Invalid index! Please select indices within the range of your hand.");
                    validSelection = false;
                    break; // Continue to prompt the player for valid input
                }
                selectedCards.add(hand.get(index));
            }

            if (!validSelection) {
                continue; // Continue to prompt the player for valid input
            }

            // Create an instance of PlayedCards to store the selected cards
            PlayedCards playedCards = new PlayedCards(currentPlayer, selectedCards);

            // Check if the selected cards win against the previous cards
            if (previousCards != null && !playedCards.winsAgainst(previousCards)) {
                System.out.println("Invalid selection! The selected cards do not win against the previous cards.");
                continue; // Continue to prompt the player for valid input
            }

            // Display the played cards
            System.out.println(currentPlayer.getName() + " played: " + playedCards.getCards());

            // Remove the played cards from the player's hand
            hand.removeAll(playedCards.getCards());
            previousCards = playedCards;
            break; // Exit the loop after a valid selection is made
        }
    }

    public static void botTurn(Player botPlayer) {
        // Get the bot player's hand
        List<Card> botHand = botPlayer.getHand().getCardsInHand();

        // Check if the bot player should pass

        // Get all valid combinations in the bot's hand
        List<PlayedCards> validCombinations = HandValidator.getAllValidCombinations(botHand, previousCards);
        if (previousCards != null && (validCombinations == null || validCombinations.size()==0)) {
            System.out.println(botPlayer.getName() + " passed their turn.");
            passCount++;
            // previousCards = null; // Reset previous cards if the player passes
            return;
        }

        // Check each valid combination against the previous cards
        for (PlayedCards combination : validCombinations) {
            if (previousCards == null || combination.winsAgainst(previousCards)) {
                // If the combination wins or there are no previous cards, play it
                System.out.println(botPlayer.getName() + " played: " + combination);
                previousCards = combination; // Update the previous cards
                botHand.removeAll(combination.getCards()); // Remove the played cards from the bot's hand
                return;
            }
        }
    }

    private static Player getNextPlayer(Player currentPlayer, List<Player> players) {
        int currentIndex = players.indexOf(currentPlayer);
        if (currentIndex + 1 == players.size()) {
            if (passCount == players.size()) {
                previousCards = null;
            }
            passCount = 0;
        }
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

    private static void quitGame() {
        System.out.println("Bye Bye!");
    }
}
