package src;
import java.util.List;

public class EasyBot extends Bot {

    public EasyBot(List<String> playerNames, List<String> usedNames) {
        super(playerNames, usedNames);
    }

    /** 
     * Plays a move based on the provided previous cards and the number of consecutive passes.
     * This method is overridden to implement the behavior of an easy-level bot.
     * 
     * @param previousCards The previously played cards.
     * @param consecutivePasses The number of consecutive passes made by players in the game.
     * @return A PlayResult object representing the result of the play.
     */
    @Override
    public PlayResult play(PlayedCards previousCards, int consecutivePasses) {
        //Pauses to allow player time to read by slowing down how fast the bots play
        pause();
        // Get the bot player's hand
        List<Card> botHand = getHand().getCards();

        // Check if there are any valid combinations
        List<PlayedCards> validCombinations = getAllValidCombinations(botHand, previousCards);
        if (validCombinations.isEmpty()) {
            System.out.println("\n" + getName() + " passed their turn.");
            consecutivePasses++;
            return new PlayResult(previousCards, consecutivePasses);
        }

        // Check if bot has the start card (3 of Diamonds)
        boolean hasStartCard = hasCard(Card.START_CARD);

        // Iterate through valid combinations
        for (PlayedCards combination : validCombinations) {
            if (combination.winsAgainst(previousCards)) {
                // If bot has the start card and the combination doesn't contain it, skip
                if (hasStartCard && !combination.getCards().contains(Card.START_CARD)) {
                    continue;
                }

                // Play the winning combination
                previousCards = combination; // Update the previous cards
                System.out.println("\n" + getName() + " played: \n" + combination);
                
                // Remove played cards from the bot's hand
                getHand().removeCards(combination);

                System.out.println(getHand());
                // Reset consecutive passes
                consecutivePasses = 0;
                
                // Return the play result
                return new PlayResult(previousCards, consecutivePasses);
            }
        }

        // If no winning combination found, pass the turn
        System.out.println("\n" + getName() + " passed their turn.");
        consecutivePasses++;
        return new PlayResult(previousCards, consecutivePasses);
    }
}