package src;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppView {

    /**
     * Displays the options menu.
     */
    private static void displayOptions() {
        System.out.print(
                "\nOptions:" +
                        "\n1. Start game" +
                        "\n2. Read instructions" +
                        "\n3. Quit game" +
                        "\nChoose an option: ");
    }

    /**
     * Prompts the user to enter their name.
     *
     * @param MAX_NAME_LENGTH The maximum length allowed for the player's name.
     * @param scanner         The Scanner object used for input.
     * @return The player's name.
     */
    public static String getName(int MAX_NAME_LENGTH, Scanner scanner) {
        System.out.println("Welcome to Dai Di!");
        String playerName;
        do {
            System.out.print("Enter your name (up to 16 characters): ");
            playerName = scanner.nextLine();

            if (playerName.length() > MAX_NAME_LENGTH) {
                System.out.println("Enter a shorter name!");
            }
        } while (playerName.length() > MAX_NAME_LENGTH);

        return playerName;
    }
    /**
     * Prompts the user to choose an option.
     *
     * @param scanner The Scanner object used for input.
     * @return The user's choice.
     */

    public static int getChoice(Scanner scanner) {
        displayOptions();
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine();
        }
        return -1;
    }
}
