package src;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game();
        App app = new App(game);
        app.run();
    }
}