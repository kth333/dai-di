package src;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //print ASCII
        Art.printArt();
        Game game = new Game();
        App app = new App(game);
        app.run();
    }
}
