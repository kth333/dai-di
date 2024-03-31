package src;

import java.util.Scanner;
import java.util.InputMismatchException;

public class GameView {
    private static final int MAX_NAME_LENGTH = 16;// Constant used to set max name length

    /**
     * Standardised prompt for player names in the application
     * validates for the length of name, if not prompts again
     * 
     * @param prompt          Message asking for player input
     * @param scanner         the scanner object for input
     * @return returns the validated name
     */
    public static String getName(String prompt, Scanner scanner) {
        String playerName;
        do {
            System.out.print(prompt);
            playerName = scanner.nextLine();

            if (playerName.length() > MAX_NAME_LENGTH) {
                System.out.println("Enter a shorter name! No names longer then 16 characters.");
            }
        } while (playerName.length() > MAX_NAME_LENGTH);
        return playerName;
    }

    /**
     * Standardised prompt for integers in the application
     * validates that the input is an integer if it is not prompts again
     * 
     * @param prompt          Message asking for player input
     * @param scanner         the scanner object for input
     * @return returns the integer
     */
    public static int getInt(String prompt, Scanner scanner) {
        while (true) {
            try {
                System.out.print(prompt);
                int choice = scanner.nextInt();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
            } finally {
                scanner.nextLine();
            }
        }
    }

    /**
     * Standardised prompt for yes or no in the application
     * validates that the input is 'y' or 'n', if not prompts again
     * 
     * @param prompt          Message asking for player input
     * @param scanner         the scanner object for input
     * @return returns the true if yes and false if no
     */
    public static boolean yesOrNo(String prompt, Scanner scanner) {
        while (true) {

            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                return false;
            } else if (input.equalsIgnoreCase("y")) {
                return true;
            }
            System.out.println("Invalid input. Please type 'y' or 'n'.");
        }
    }
}
