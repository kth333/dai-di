import java.util.List;

public class EasyBot extends Bot {

    public EasyBot(List<String> playerNames, List<String> usedNames) {
        super(playerNames, usedNames);
    }

    @Override
    public PlayResult play(PlayedCards previousCards, int consecutivePasses, int turn) {
        // Get the bot player's hand
        List<Card> botHand = getHand().getCardsInHand();

        // Get all valid combinations in the bot's hand
        List<PlayedCards> validCombinations = getAllValidCombinations(botHand, previousCards);

        if (validCombinations != null) {
            // Check each valid combination against the previous cards
            for (PlayedCards combination : validCombinations) {

                if (combination.winsAgainst(previousCards)) {
                    Card startCard=new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
                    if (hasCard(startCard) && !combination.getCards().contains(startCard)) {
                        // If bot has 3 of Diamonds and the combination doesn't contain 3 of Diamonds, continue searching
                        continue;
                    }
                    // If the combination wins or there are no previous cards, play it
                    previousCards = combination; // Update the previous cards
                    System.out.println("\n" + getName() + " played: " + combination);
                    botHand.removeAll(combination.getCards()); // Remove the played cards from the bot's hand
                    if (previousCards != null) {
                        consecutivePasses = 0;
                        return new PlayResult(this,previousCards, consecutivePasses,turn);
                    }
                }
            }
        }
        System.out.println("\n" + getName() + " passed their turn.");
        consecutivePasses++;
        return new PlayResult(this,previousCards, consecutivePasses,turn);
    }
}