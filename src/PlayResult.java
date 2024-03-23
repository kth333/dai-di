public class PlayResult {
    private PlayedCards previousCards;
    private int consecutivePasses;
    private boolean quit;

    public PlayResult(PlayedCards previousCards, int consecutivePasses) {
        this.previousCards = previousCards;
        this.consecutivePasses = consecutivePasses;
        this.quit = false;
    }

    public PlayedCards getPreviousCards() {
        return previousCards;
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
}