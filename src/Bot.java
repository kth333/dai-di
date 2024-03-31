package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Bot extends Player {
    // creates a list of botnames
    private static final String[] botNames = { "Yeow Leong", "Zhi Yuan", "Lay Foo", "Fang Yuan", "Tony", "Lily Kong" };
    private static final Random random = new Random();
    private static final int PAUSE_IN_MS = 2000;// Constant to pause bot for 2 seconds

    public static List<String> usedNames = new ArrayList<>();

    /** A list to keep track of used bot names. */
    public static List<String> usedNames = new ArrayList<>();

    /**
     * Constructs a Bot object with a randomly generated name that is not already used by other players.
     *
     * @param playerNames The list of player names in the game.
     * @param usedNames   The list of names already used by players.
     */

    public Bot(List<String> playerNames, List<String> usedNames) {
        super(generateBotName(playerNames, usedNames));
        usedNames.add(getName());
    }
       /**
     * Generates a unique bot name that is not already used by other players.
     *
     * @param playerNames The list of player names in the game.
     * @param usedNames   The list of names already used by players.
     * @return A unique bot name.
     */

    private static String generateBotName(List<String> playerNames, List<String> usedNames) {
        for (String usedname : playerNames) {
            usedNames.add(usedname);
        }
        String name;
        do {
            name = botNames[random.nextInt(botNames.length)];
        } while (usedNames.contains(name));
        return name;
    }

    /**
     * This method lets the bot chooses which valid combination to play (the
     * strategy of the bot)
     * This method is abstract and must be implemented by all bots to define the
     * difficulty level of the bot
     * 
     * @param previousCards     The previously played cards.
     * @param consecutivePasses The number of consecutive passes made by players in
     *                          the game.
     * @return A PlayResult object representing the result of the play.
     */
    public abstract PlayResult play(PlayedCards previousCards, int consecutivePasses);

    /**
     * Gets all valid combinations that can be played from the given hand,
     * considering the previous cards played.
     * 
     * @param hand          The list of cards available in the player's hand.
     * @param previousCards The previously played cards, or null if no cards have
     *                      been played yet.
     * @return A list of PlayedCards objects representing valid combinations that
     *         can be played.
     */
    public static List<PlayedCards> getAllValidCombinations(List<Card> hand, PlayedCards previousCards) {
        List<PlayedCards> validCombinations = new ArrayList<>();

        if (previousCards == null) {
            validCombinations.addAll(findSingles(hand));
            validCombinations.addAll(findDoubles(hand));
            validCombinations.addAll(findTriples(hand));
            validCombinations.addAll(findFiveCombination(hand));

        } else {
            String previousType = previousCards.getType();

            // Check if previousCards was a single, double, triple, or combination of 5
            // cards
            switch (previousType) {
                case "Single":
                    validCombinations.addAll(findSingles(hand));
                    break;
                case "Pair":
                    validCombinations.addAll(findDoubles(hand));
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
     * Finds all combinations of singles from the given hand.
     * 
     * @param hand The list of cards to generate singles from.
     * @return A list of PlayedCards objects representing valid singles.
     */
    public static List<PlayedCards> findSingles(List<Card> hand) {
        List<PlayedCards> singles = new ArrayList<>();
        for (Card card : hand) {
            List<Card> single = new ArrayList<>();
            single.add(card);
            singles.add(new PlayedCards(single));
        }
        return singles;
    }

    /**
     * Finds all combinations of pairs from the given hand.
     * 
     * @param hand The list of cards to generate pairs from.
     * @return A list of PlayedCards objects representing valid pairs.
     */
    public static List<PlayedCards> findDoubles(List<Card> hand) {
        List<PlayedCards> doubles = new ArrayList<>();
        for (int i = 0; i < hand.size() - 1; i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                List<Card> doubleCards = new ArrayList<>();
                doubleCards.add(hand.get(i));
                doubleCards.add(hand.get(j));
                PlayedCards doubleCombination = new PlayedCards(doubleCards);
                if (HandValidator.isDouble(doubleCombination)) {
                    doubles.add(doubleCombination);
                }
            }
        }
        return doubles;
    }

    /**
     * Finds all combinations of triples from the given hand.
     * 
     * @param hand The list of cards to generate triples from.
     * @return A list of PlayedCards objects representing valid triples.
     */
    public static List<PlayedCards> findTriples(List<Card> hand) {
        List<PlayedCards> triples = new ArrayList<>();
        findTriples(hand, 0, new ArrayList<Card>(), triples);
        return triples;
    }

    /**
     * Pauses the bot for 2 seconds
     * 
     */
    public static void pause() {
        try {
            // pause execution for 2 seconds
            Thread.sleep(PAUSE_IN_MS);
        } catch (InterruptedException e) {
            // handle the exception
            e.printStackTrace();
        }
    }

    /**
     * Recursively finds combinations of triples from the given hand, starting from
     * the specified index.
     * 
     * @param hand               The list of cards to generate triples from.
     * @param start              The index to start generating triples from.
     * @param currentCombination The current combination being built.
     * @param triples            The list to store valid triples.
     */
    private static void findTriples(List<Card> hand, int start, List<Card> currentCombination,
            List<PlayedCards> triples) {
        if (currentCombination.size() == 3) {
            PlayedCards tripleCombination = new PlayedCards(new ArrayList<>(currentCombination));
            if (HandValidator.isTriple(tripleCombination)) {
                triples.add(tripleCombination);
            }
            return;
        }

        for (int i = start; i < hand.size(); i++) {
            currentCombination.add(hand.get(i));
            findTriples(hand, i + 1, currentCombination, triples);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    /**
     * Finds all combinations of 5 cards from the given hand that form valid poker
     * hands.
     * 
     * @param hand The list of cards to generate combinations from.
     * @return A list of PlayedCards objects representing valid combinations.
     */
    public static List<PlayedCards> findFiveCombination(List<Card> hand) {
        List<PlayedCards> combinations = new ArrayList<>();
        findCombinations(hand, 0, new ArrayList<Card>(), combinations);
        return combinations;
    }

    /**
     * Recursively finds combinations of cards from the given hand, starting from
     * the specified index.
     * 
     * @param hand               The list of cards to generate combinations from.
     * @param start              The index to start generating combinations from.
     * @param currentCombination The current combination being built.
     * @param combinations       The list to store valid combinations.
     */
    private static void findCombinations(List<Card> hand, int start, List<Card> currentCombination,
            List<PlayedCards> combinations) {
        if (currentCombination.size() == 5) {
            PlayedCards combination = new PlayedCards(new ArrayList<>(currentCombination));
            if (HandValidator.isStraightFlush(combination) ||
                    HandValidator.isFourOfAKind(combination) ||
                    HandValidator.isFullHouse(combination) ||
                    HandValidator.isFlush(combination) ||
                    HandValidator.isStraight(combination)) {
                combinations.add(combination);
            }
            return;
        }

        for (int i = start; i < hand.size(); i++) {
            currentCombination.add(hand.get(i));
            findCombinations(hand, i + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}
