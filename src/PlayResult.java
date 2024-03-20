public class PlayResult {
    private PlayedCards previousCards;
    private int consecutivePasses;

    public PlayResult(PlayedCards previousCards, int consecutivePasses) {
        this.previousCards = previousCards;
        this.consecutivePasses = consecutivePasses;
    }

    public PlayedCards getPreviousCards() {
        return previousCards;
    }

    public int getConsecutivePasses() {
        return consecutivePasses;
    }
}