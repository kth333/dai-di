import java.util.*;
import java.io.*;

public class Instruction {
    
    public static void DisplayInstructions(){
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
        } catch(Exception e) {
            System.out.println("exception occured");
        }
    }

}
