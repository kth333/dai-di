package Dai_Di;

import Dai_Di.App.App;
import Dai_Di.Art.Art;
import Dai_Di.Game.Game;

public class Main {

    public static void main(String[] args) {
        //print ASCII
        Art.printWelcomeArt();
        Game game = new Game();
        App app = new App(game);
        app.run();
    }
}
