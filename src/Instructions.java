package src;
import java.util.*;
import java.io.*;

/**
 * Utility class for displaying game instructions.
 */
public class Instructions {

    /**
     * Displays the game instructions stored in a text file named "Rules.txt".
     * If the file is not found, a message indicating that the rules were not found is printed.
     */

    public static void displayInstructions(){
        String RuleName = "src/Rules.txt";
        //text file which contains all the instructions

        String Current_Line;
        File file = new File(RuleName);
        try(Scanner document = new Scanner(file)){
            while (document.hasNextLine()){
                //print each line in the text file until theres no more lines
                Current_Line = document.nextLine();
                System.out.println(Current_Line);
            }

        //can log exceptions instead (?)
        } catch(FileNotFoundException e) {
            System.out.println("Rules not found");
        }
    }

}
