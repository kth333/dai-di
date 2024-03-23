package Dai_Di.Cards;

import Dai_Di.Players.Player;
import java.util.List;

public class PlayResult {
    private Player player;
    private PlayedCards previousCards;
    private int consecutivePasses;
    private boolean quit;

    public PlayResult(Player player, PlayedCards previousCards, int consecutivePasses) {
        this.player = player;
        this.previousCards = previousCards;
        this.consecutivePasses = consecutivePasses;
        this.quit = false;
    }

    public PlayedCards getPreviousCards() {
        return previousCards;
    }

    public Player getPlayer() {
        return player;
    }

    public int getConsecutivePasses() {
        return consecutivePasses;
    }

    public boolean isQuit() { // Getter for the quit field
        return quit;
    }

    public void setQuit(boolean quit) { // Setter for the quit field
        this.quit = quit;
    }

    public static void playHistory(List<PlayResult> playHistory) {
        if (playHistory == null || playHistory.size() == 0) {
            System.out.println("No Play History.");
            return;
        }

        System.out.println("Play History:");
        for (PlayResult play : playHistory) {
            if (play.getConsecutivePasses() == 0) {
                System.out.println(play.getPlayer().getName() + " played " + play.getPreviousCards().toString());
            } else {
                System.out.println(play.getPlayer().getName() + " passes.");
            }
        }
    }
}
