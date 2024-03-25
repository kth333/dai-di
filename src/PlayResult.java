/**
 * Represents the result of a play in a game.
 */
public class PlayResult {
    private PlayedCards previousCards; // The cards played in the previous turn
    private int consecutivePasses; // The number of consecutive passes
    private boolean quit; // Indicates whether the player chose to quit the game

    /**
     * Constructs a PlayResult object with the previous cards played and the number of consecutive passes.
     *
     * @param previousCards the cards played in the previous turn
     * @param consecutivePasses the number of consecutive passes
     */
    public PlayResult(PlayedCards previousCards, int consecutivePasses) {
        this.previousCards = previousCards;
        this.consecutivePasses = consecutivePasses;
        this.quit = false; // By default, the quit flag is set to false
    }

    /**
     * Retrieves the cards played in the previous turn.
     *
     * @return the previous cards played
     */
    public PlayedCards getPreviousCards() {
        return previousCards;
    }

    /**
     * Retrieves the number of consecutive passes.
     *
     * @return the number of consecutive passes
     */
    public int getConsecutivePasses() {
        return consecutivePasses;
    }

    /**
     * Checks whether the player chose to quit the game.
     *
     * @return true if the player quit, false otherwise
     */
    public boolean isQuit() {
        return quit;
    }

    /**
     * Sets the quit flag to indicate whether the player chose to quit the game.
     *
     * @param quit true if the player quit, false otherwise
     */
    public void setQuit(boolean quit) {
        this.quit = quit;
    }
}