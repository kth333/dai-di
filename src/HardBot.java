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
        
        List<Card> botHand = getHand().getCardsInHand();
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
    
    private boolean canPlayCombination(PlayedCards previousCards, PlayedCards combination) {
        if (previousCards == null || combination.winsAgainst(previousCards)) {
            Card startCard = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
            return !hasCard(startCard) || combination.getCards().contains(startCard);
        }
        return false;
    }
    /**
     * Removes the played combination from the bot's hand and logs the play.
     * 
     * @param botHand The current hand of the bot.
     * @param combination The combination of cards being played.
     * @return The played combination.
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
    

    //this method checks if a set of cards contains any high cards
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
     * Generates all valid combinations of cards that can be played based on the given hand and the previously played cards.
     * 
     * If the previously played cards are null, it generates all possible combinations from the hand. 
     * Otherwise, it checks the type of the previously played cards (single, pair, three of a kind, etc.) and generates valid combinations accordingly.
     * 
     * After generating the valid combinations, it filters out the combinations that win against the previously played cards.
     * 
     * @param hand The current hand of the player.
     * @param previousCards The cards that were played in the previous turn.
     * @return A list of all valid combinations of cards that can be played, filtered to only include winning combinations.
     */
    public static List<PlayedCards> getAllValidCombinations(List<Card> hand, PlayedCards previousCards) {
        List<PlayedCards> validCombinations = new ArrayList<>();
    
        if (previousCards == null) {
            validCombinations = getValidCombinations(hand);
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
     * Generates all valid combinations of cards that can be played based on the given hand.
     * 
     * It first tries to find valid five card combinations. If none are found, it tries to find valid three card combinations.
     * If still none are found, it tries to find valid two card combinations. If no two card combinations are found, it adds all single card combinations.
     * 
     * @param hand The current hand of the player.
     * @return A list of all valid combinations of cards that can be played.
     */
    private static List<PlayedCards> getValidCombinations(List<Card> hand) {
        List<PlayedCards> validCombinations = new ArrayList<>();
    
        validCombinations.addAll(findValidCombinations(hand, 5, Card.Rank.ACE));
    
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findValidCombinations(hand, 3, Card.Rank.ACE));
        }
    
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findValidCombinations(hand, 2, Card.Rank.TWO));
        }
    
        if (validCombinations.isEmpty()) {
            validCombinations.addAll(findSingles(hand));
        }
    
        return validCombinations;
    }
    


    /**
     * Finds valid combinations of cards from a given hand based on the combination size and a rank to avoid.
     * 
     * It first determines the potential combinations based on the combination size. Then, it checks each potential combination to see if it's worth playing,
     * considering the rank to avoid. If a combination is worth playing, it's added to the list of valid combinations.
     * 
     * @param hand The current hand of the player.
     * @param combinationSize The size of the card combination to find (e.g., 2 for a pair, 3 for three of a kind, etc.).
     * @param rankToAvoid The rank of the card to avoid when finding combinations.
     * @return A list of all valid combinations of cards that can be played.
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
            PlayedCards worthPlaying = botStrategy(p, new Card(Card.Suit.DIAMONDS, rankToAvoid));
            if (worthPlaying != null) {
                validCombinations.add(worthPlaying);
            }
        }
    
        return validCombinations;
    }
    
    /**
     * Finds efficient combinations of cards from a given hand based on the combination size and whether to avoid twos.
     * It first determines the potential combinations based on the combination size. Then, it checks each potential combination to see if it's worth playing,
     * considering whether to avoid twos. If a combination is worth playing, it's added to the list of efficient combinations.
     * If no efficient combinations are found, it tries to find any combination that doesn't use a two.
     * 
     * @param hand The current hand of the player.
     * @param combinationSize The size of the card combination to find (e.g., 1 for a single, 2 for a pair, etc.).
     * @param avoidTwos A boolean indicating whether to avoid twos when finding combinations.
     * @return A list of all efficient combinations of cards that can be played.
     */
    private static List<PlayedCards> findEfficientCombinations(List<Card> hand, int combinationSize, boolean avoidTwos) {
        List<PlayedCards> efficientCombinations = new ArrayList<>();
        List<PlayedCards> potentialCombinations;
    
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
    
        HashMap<Card, String> uniqueCards = getUniqueCards(hand, avoidTwos);
    
        for (PlayedCards p : potentialCombinations) {
            if (isWorthPlaying(p, uniqueCards)) {
                efficientCombinations.add(p);
            }
        }
    
        if (efficientCombinations.isEmpty()) {
            for (PlayedCards p : potentialCombinations) {
                PlayedCards worthPlaying = botStrategy(p, new Card(Card.Suit.DIAMONDS, Card.Rank.TWO));
                if (worthPlaying != null) {
                    efficientCombinations.add(worthPlaying);
                }
            }
        }
    
        return efficientCombinations;
    }
    
    /**
     * Generates a map of unique cards from a given hand, with an option to avoid twos.
     * 
     * It first finds potential five card and three card combinations from the hand. Then, it adds the cards from these combinations to the map of unique cards.
     * If the avoidTwos parameter is true, it also adds all twos to the map of unique cards.
     * 
     * @param hand The current hand of the player.
     * @param avoidTwos A boolean indicating whether to avoid twos when generating the map of unique cards.
     * @return A map of unique cards from the hand.
     */
    private static HashMap<Card, String> getUniqueCards(List<Card> hand, boolean avoidTwos) {
        HashMap<Card, String> uniqueCards = new HashMap<>();
    
        List<PlayedCards> potentialFiveCombinations = findFiveCombination(hand);
        List<PlayedCards> potentialThreeCombinations = findTriples(hand);
    
        addCardsToUniqueCards(potentialFiveCombinations, uniqueCards);
        addCardsToUniqueCards(potentialThreeCombinations, uniqueCards);
    
        if (avoidTwos) {
            uniqueCards.put(new Card(Card.Suit.DIAMONDS, Card.Rank.TWO), "");
            uniqueCards.put(new Card(Card.Suit.CLUBS, Card.Rank.TWO), "");
            uniqueCards.put(new Card(Card.Suit.HEARTS, Card.Rank.TWO), "");
            uniqueCards.put(new Card(Card.Suit.SPADES, Card.Rank.TWO), "");
        }
    
        return uniqueCards;
    }
    
    /**
     * Adds cards from potential combinations to a map of unique cards.
     * 
     * It iterates over each potential combination, and for each combination, it iterates over each card within that combination.
     * If the card is not already in the map of unique cards, it adds the card to the map.
     * 
     * @param potentialCombinations A list of potential combinations of cards.
     * @param uniqueCards A map of unique cards.
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
     * Determines if a given combination of cards is worth playing based on a map of unique cards.
     * 
     * It iterates over each card within the combination. If any card is in the map of unique cards, it returns false, indicating that the combination is not worth playing.
     * If none of the cards in the combination are in the map of unique cards, it returns true, indicating that the combination is worth playing.
     * 
     * @param p The combination of cards to check.
     * @param uniqueCards A map of unique cards.
     * @return A boolean indicating whether the combination is worth playing.
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
