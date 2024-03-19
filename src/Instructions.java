import java.util.*;
import java.io.*;

public class Instructions {

    public static void displayInstructions(){
        String RuleName = "Rules.txt";
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