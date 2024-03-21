import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Give the user a warm welcome :)
        System.out.println("Welcome to Dai Di!");

        // Prompt the user to enter their name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Start game");
            System.out.println("2. Read instructions");
            System.out.println("3. Quit game");
            System.out.print("\nChoose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        Game game = new Game();
                        Player humanPlayer = new Player(playerName);
                        Player bot1 = new Bot(humanPlayer.getName(), Bot.usedNames);
                        Player bot2 = new HardBot(humanPlayer.getName(), Bot.usedNames);
                        Player bot3 = new Bot(humanPlayer.getName(), Bot.usedNames);
                        game.startGame(humanPlayer, bot1, bot2, bot3);
                        return; // Exit the loop and terminate the program
                    case 2:
                        System.out.println("\nInstructions:");
                        Instructions.displayInstructions();
                        break; // Continue showing menu
                    case 3:
                        System.out.println("Bye Bye!");
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }
}
