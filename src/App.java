import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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

                        int players=1;
                        do{
                            System.out.print("Select number of human players: ");
                            players = scanner.nextInt();
                            scanner.nextLine();
                            if (players<1 || players>4){
                                System.out.println("Invalid player number! Player number is only 1 to 4.");
                            } else{
                                break;
                            }
                        } while(true);

                        List<Player> playerList = new ArrayList<Player>(Arrays.asList(new Player(playerName)));
                        List<String> playerNames = new ArrayList<String>(Arrays.asList(playerName));
                        //Create human players
                        for (int i=2;i<=players;i++){
                            String name=null;
                            do{
                                System.out.print("Enter player "+i+" name: ");
                                name=scanner.nextLine();
                                if (name!=null && name.length()>0 && !playerNames.contains(name)){
                                    playerNames.add(name);
                                    playerList.add(new Player(name));
                                    break;
                                } else{
                                    System.out.println("Invalid name try again!");
                                }
                            } while(true);
                        }

                        if (players<4){
                            for (int i=0;i<4-players;i++){
                                playerList.add(new Bot(playerNames,Bot.usedNames));
                            }
                        }

                        game.startGame(playerList);
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
