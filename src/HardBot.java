package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HardBot extends Bot {
    /**
     * Constructs a new HardBot instance.
     * 
     * @param playerNames A list of names for all players in the game.
     * @param usedNames A list of names already used by other bots or players.
     */
    public HardBot(List<String> playerNames, List<String> usedNames) {
        super(playerNames, usedNames);
    }
     /**
     * Attempts to play a combination of cards that beats the previous combination played.
     * If no such combination exists, the bot will pass its turn.
     * 
     * @param previousCards The cards played in the previous turn.
     * @param consecutivePasses The current number of consecutive passes.
     * @return The result of the bot's play, including the combination played and the updated number of consecutive passes.
     */

    @Override
    public PlayResult play(PlayedCards previousCards, int consecutivePasses) {
        pause();

        List<Card> botHand = getHand().getCards();
        List<PlayedCards> validCombinations = getAllValidCombinations(botHand, previousCards);
    
        if (validCombinations != null) {
            for (PlayedCards combination : validCombinations) {
                if (canPlayCombination(previousCards, combination)) {
                    previousCards = playCombination(botHand, combination);
                    consecutivePasses = 0;
                    return new PlayResult(previousCards, consecutivePasses);
                }
            }
        }
    
        //if got here, means no validCombinations and bot will just pass
        System.out.println("\n" + getName() + " passed their turn.");
        consecutivePasses++;
        return new PlayResult(previousCards, consecutivePasses);
    }

    /**
     * Checks if the bot can play the given combination based on the current game state.
     * 
     * @param previousCards The cards played in the previous turn.
     * @param combination The combination the bot is considering to play.
     * @return true if the combination can be legally played, false otherwise.
     */
    
    /** 
     * Checks if the bot can play the given combination based on the previous cards played.
     * 
     * @param previousCards The previously played cards.
     * @param combination The combination of cards to check for playability.
     * @return true if the combination can be played, false otherwise.
     */
    private boolean canPlayCombination(PlayedCards previousCards, PlayedCards combination) {

        // Check if everyone has passed or if the current combination can win against previous cards
        if (previousCards == null || combination.winsAgainst(previousCards)) {

            // If the bot has the 3 of Diamonds and the combination doesn't contain it, return false
            return !hasCard(Card.START_CARD) || combination.getCards().contains(Card.START_CARD);
        }
        return false; // If none of the conditions are met, return false
    }
    /**
     * Removes the played combination from the bot's hand and logs the play.
     * 
     * @param botHand The current hand of the bot.
     * @param combination The combination of cards being played.
     * @return The played combination.
     */
    
    /**
     * Plays the given combination from the bot's hand and removes played cards from bots hand
     * 
     * @param botHand The bot's current hand of cards.
     * @param combination The combination of cards to play.
     * @return The combination of cards that was played.
     */
    private PlayedCards playCombination(List<Card> botHand, PlayedCards combination) {
        System.out.println("\n" + getName() + " played: " + combination);
        botHand.removeAll(combination.getCards());
        return combination;
    }

    /**
     * Evaluates the cards to determine if they contain any high-value cards above the specified minimum strength.
     * 
     * @param Cards The set of cards to evaluate.
     * @param minimumStrength The minimum strength card to compare against.
     * @return The original set of cards if no card exceeds the minimum strength, null otherwise.
     */
    

    /**
     * Bot strategy for determining what to play.
     * 
     * @param cards The set of cards to evaluate.
     * @param minimumStrength The minimum strength card.
     * @return The set of cards to play, or null if none are suitable.
     */
    public static PlayedCards botStrategy(PlayedCards Cards, Card minimumStrength){
        PlayedCards weakestSingle = new PlayedCards();
        weakestSingle.addCard(minimumStrength);

        //get the individual cards within each card combinations
        List<Card> card_list = Cards.getCards();
        List<PlayedCards> singles = findSingles(card_list);

        //if any cards is bigger than the minimum strength card, return null
        for (PlayedCards individual : singles){
            if (individual.winsAgainst(weakestSingle)){
                return null;
            }
        }

        //if we reach here, means that the set of cards did not contain any cards stronger
        //than the minimum strength card
        return Cards;

    }   

    /**
     * Retrieves all valid combinations of cards that can be played.
     * 
     * @param hand The bot's current hand of cards.
     * @param previousCards The previously played cards.
     * @return A list of valid combinations that can be played.
     */
    public static List<PlayedCards> getAllValidCombinations(List<Card> hand, PlayedCards previousCards) {
        List<PlayedCards> validCombinations = new ArrayList<>();
    
        if (previousCards == null) {
            validCombinations = getValidCombinationsWhenAllPassed(hand);
        } else {
            String previousType = previousCards.getType();
    
            switch (previousType) {
                case "Single":
                    validCombinations.addAll(findEfficientCombinations(hand, 1, false));
                    break;
                case "Pair":
                    validCombinations.addAll(findEfficientCombinations(hand, 2, true));
                    break;
                case "Three of a Kind":
                    validCombinations.addAll(findTriples(hand));
                    break;
                default:
                    validCombinations.addAll(findFiveCombination(hand));
                    break;
            }
        }
    
        // Filter valid combinations that win against previousCards
        List<PlayedCards> winningCombinations = new ArrayList<>();
        for (PlayedCards combination : validCombinations) {
            if (combination.winsAgainst(previousCards)) {
                winningCombinations.add(combination);
            }
        }
    
        return winningCombinations;
    }
    
    /** 
     * Bot logic for what to play when everyone else has passed
     * 
     * @param hand The cards in the bots hand
     * @return List of PlayedCards which represents the bot's options on what to play
     */
    private static List<PlayedCards> getValidCombinationsWhenAllPassed(List<Card> hand) {
        List<PlayedCards> validCombinations = new ArrayList<>();
    
        // Try to find valid five card combinations that does not contain an ACE
        validCombinations.addAll(findValidCombinations(hand, 5, Card.Rank.ACE));
    
        // If no valid five card combinations, try to find valid three card combinations that does not contain an ACE
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findValidCombinations(hand, 3, Card.Rank.ACE));
        }
    
        // If no valid three card combinations, try to find valid two card combinations that does not contain TWO's
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findValidCombinations(hand, 2, Card.Rank.TWO));
        }
    
        // If no valid two card combinations, add all single card combinations
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findSingles(hand));
        }
    
        return validCombinations;
    }
    
    /**
     * Finds valid combinations of cards based on the given parameters.
     * 
     * @param hand The list of cards in the hand.
     * @param combinationSize The size of combinations to search for.
     * @param rankToAvoid The rank of cards to avoid.
     * @return A list of valid combinations.
     */
    private static List<PlayedCards> findValidCombinations(List<Card> hand, int combinationSize, Card.Rank rankToAvoid) {
        List<PlayedCards> validCombinations = new ArrayList<>();
        List<PlayedCards> potentialCombinations;
    
        switch (combinationSize) {
            case 5:
                potentialCombinations = findFiveCombination(hand);
                break;
            case 3:
                potentialCombinations = findTriples(hand);
                break;
            case 2:
                potentialCombinations = findDoubles(hand);
                break;
            default:
                return validCombinations;
        }
    
        for (PlayedCards p : potentialCombinations) {
            PlayedCards worthPlaying = botStrategy(p, new Card(rankToAvoid, Card.Suit.DIAMONDS));
            if (worthPlaying != null) {
                validCombinations.add(worthPlaying);
            }
        }
    
        return validCombinations;
    }
    
    /**
     * Finds efficient combinations of cards based on the given parameters.
     * 
     * @param hand The list of cards in the hand.
     * @param combinationSize The size of combinations to search for.
     * @param avoidTwos Whether to avoid combinations containing twos.
     * @return A list of efficient combinations.
     */
    private static List<PlayedCards> findEfficientCombinations(List<Card> hand, int combinationSize, boolean avoidTwos) {
        List<PlayedCards> efficientCombinations = new ArrayList<>();
        List<PlayedCards> potentialCombinations;
    
        //find potential singles/doubles. If no valid combinations, return empty list
        switch (combinationSize) {
            case 1:
                potentialCombinations = findSingles(hand);
                break;
            case 2:
                potentialCombinations = findDoubles(hand);
                break;
            default:
                return efficientCombinations;
        }
    
        HashMap<Card, String> uniqueCards = getUniqueCardsToAvoid(hand, avoidTwos);
    
        //if any combinations contains unique cards to avoid, dont add it to efficient combinations
        for (PlayedCards p : potentialCombinations) {
            if (isWorthPlaying(p, uniqueCards)) {
                efficientCombinations.add(p);
            }
        }
    
        // If no efficient combinations found, add all potential combinations
        if (efficientCombinations.isEmpty()) {

            efficientCombinations.addAll(potentialCombinations);
        }
    
        return efficientCombinations;
    }
    
    /**
     * Retrieves unique cards to avoid based on the given parameters.
     * 
     * @param hand The list of cards in the hand.
     * @param avoidTwos Whether to avoid twos.
     * @return A map of unique cards to avoid.
     */
    private static HashMap<Card, String> getUniqueCardsToAvoid(List<Card> hand, boolean avoidTwos) {
        HashMap<Card, String> uniqueCards = new HashMap<>();
    
        List<PlayedCards> potentialFiveCombinations = findFiveCombination(hand);
        List<PlayedCards> potentialThreeCombinations = findTriples(hand);
    
        addCardsToUniqueCards(potentialFiveCombinations, uniqueCards);
        addCardsToUniqueCards(potentialThreeCombinations, uniqueCards);
    
        if (avoidTwos) {
            uniqueCards.put(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS), "");
            uniqueCards.put(new Card(Card.Rank.TWO, Card.Suit.CLUBS), "");
            uniqueCards.put(new Card(Card.Rank.TWO, Card.Suit.HEARTS), "");
            uniqueCards.put(new Card(Card.Rank.TWO, Card.Suit.SPADES), "");
        }
    
        return uniqueCards;
    }
    
    /**
     * Adds cards to the unique cards map.
     * 
     * @param potentialCombinations The list of potential combinations.
     * @param uniqueCards The map of unique cards.
     */
    private static void addCardsToUniqueCards(List<PlayedCards> potentialCombinations, HashMap<Card, String> uniqueCards) {
        for (PlayedCards p : potentialCombinations) {
            List<Card> cardsWithin = p.getCards();
            for (Card c : cardsWithin) {
                if (uniqueCards.containsKey(c)) {
                    uniqueCards.put(c, "");
                }
            }
        }
    }
    
    /**
     * Determines if a combination is worth playing based on the unique cards to avoid.
     * 
     * @param p The combination to evaluate.
     * @param uniqueCards The map of unique cards to avoid.
     * @return true if the combination is worth playing, false otherwise.
     */
    private static boolean isWorthPlaying(PlayedCards p, HashMap<Card, String> uniqueCards) {
        List<Card> cardsWithin = p.getCards();
        for (Card c : cardsWithin) {
            if (uniqueCards.containsKey(c)) {
                return false;
            }
        }
        return true;
    }
}
