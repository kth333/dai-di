<<<<<<< Updated upstream:src/Dai_Di/Game/Instructions.java
package Dai_Di.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
=======
package src;
import java.util.*;
import java.io.*;
>>>>>>> Stashed changes:src/Instructions.java

public class Instructions {

    public static void displayInstructions(){
<<<<<<< Updated upstream:src/Dai_Di/Game/Instructions.java
        //getAbsolutePath will get the root directory of the program
        String RuleName = new File("").getAbsolutePath()+"/data/Rules.txt";
=======
        String RuleName = "src/Rules.txt";
>>>>>>> Stashed changes:src/Instructions.java
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