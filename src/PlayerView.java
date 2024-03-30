package src;

public class PlayerView {
    /**
     * Displays the commands the player can enter
     *@param PASS_COMMAND for the command to pass
     *@param SUIT_COMMAND for the command to sort hand by suit
     *@param RANK_COMMAND for the command to sort hand by rank
     *@param INSTRUCTIONS_COMMAND for the command to display instructions
     *@param QUIT_COMMAND for the command to quit game
     */
    public static void displayOptions(String PASS_COMMAND, String SUIT_COMMAND, String RANK_COMMAND,
            String INSTRUCTIONS_COMMAND, String QUIT_COMMAND) {
        System.out.print("\nOptions:\n" +
                " - Select cards to play (enter indices separated by spaces)\n" +
                " - Type '" + PASS_COMMAND + "' to pass\n" +
                " - Type '" + SUIT_COMMAND + "' to sort hand by suit\n" +
                " - Type '" + RANK_COMMAND + "' to sort hand by rank\n" +
                " - Type '" + INSTRUCTIONS_COMMAND + "' to display instructions\n" +
                " - Type '" + QUIT_COMMAND + "' to quit the game\n" +
                "Your choice: ");
    }

    /**
     * Displays a hand and message
     *@param hand hand to be printed
     *@param message message to print
     */
    public static void displayHand(Hand hand,String message){
        System.out.print("\n"+message+": "+hand);
    }


}
