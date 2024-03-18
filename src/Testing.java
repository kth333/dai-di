import java.util.InputMismatchException;
import java.util.Scanner;

public class Testing {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Dai Di!");
        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("started game");
                        return; // Exit the loop and terminate the program
                    case 2:
                        Instructions.displayInstructions();
                        break; // Exit the loop and terminate the program
                    case 3:
                        System.out.println("bye bye");
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2 or 3.");
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
}
