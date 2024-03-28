package src;
/**
 * Represents the result of a play in a game.
 */
public class PlayResult {
    private Player player;//the player who performed the turn
    private PlayedCards previousCards; // The cards played in the previous turn
    private int consecutivePasses; // The number of consecutive passes, if >0 means player passes turn
    private int turn;//tracks the turn the play was made
    private boolean quit; // Indicates whether the player chose to quit the game

    /**
     * Constructs a PlayResult object with the previous cards played and the number of consecutive passes.
     *
     * @param player Player that made the move
     * @param previousCards the cards played in the previous turn
     * @param consecutivePasses the number of consecutive passes
     */
    public PlayResult(Player player,PlayedCards previousCards, int consecutivePasses,int turn) {
        this.player=player;
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

    /**
     * Retrieves the player who played the cards.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }
}
