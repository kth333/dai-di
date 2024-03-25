package Dai_Di.Players;

import Dai_Di.Cards.PlayResult;
import Dai_Di.Cards.PlayedCards;
import Dai_Di.Cards.Card;
import Dai_Di.Cards.HandValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Bot extends Player {
    //creates a list of botnames
    private static final String[] botNames = { "Yeow Leong", "Zhi Yuan", "Lay Foo", "Fang Yuan", "Tony", "Lily Kong" };
    private static final Random random = new Random();
    public static List<String> usedNames = new ArrayList<>();

    public Bot(List<String> playerNames, List<String> usedNames) {
        super(generateBotName(playerNames, usedNames));
        usedNames.add(getName());
    }

    private static String generateBotName(List<String> playerNames, List<String> usedNames) {
        for (String usedname:playerNames){
            usedNames.add(usedname);
        }
        String name;
        do {
            name = botNames[random.nextInt(botNames.length)];
        } while (usedNames.contains(name));
        return name;
    }

    public abstract PlayResult play(PlayedCards previousCards, int consecutivePasses);

    public static List<PlayedCards> getAllValidCombinations(List<Card> hand, PlayedCards previousCards) {
        List<PlayedCards> validCombinations = new ArrayList<>();

        if (previousCards == null) {
                        // If previousCards is null, any combination in the hand is valid
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

    public static List<PlayedCards> findSingles(List<Card> hand) {
        List<PlayedCards> singles = new ArrayList<>();
        for (Card card : hand) {
            List<Card> single = new ArrayList<>();
            single.add(card);
            singles.add(new PlayedCards(single));
        }
        return singles;
    }

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

    public static List<PlayedCards> findTriples(List<Card> hand) {
        List<PlayedCards> triples = new ArrayList<>();
        for (int i = 0; i < hand.size() - 2; i++) {
            for (int j = i + 1; j < hand.size() - 1; j++) {
                for (int k = j + 1; k < hand.size(); k++) {
                    List<Card> tripleCards = new ArrayList<>();
                    tripleCards.add(hand.get(i));
                    tripleCards.add(hand.get(j));
                    tripleCards.add(hand.get(k));
                    PlayedCards tripleCombination = new PlayedCards(tripleCards);
                    if (HandValidator.isTriple(tripleCombination)) {
                        triples.add(tripleCombination);
                    }
                }
            }
        }
        return triples;
    }

    public static List<PlayedCards> findFiveCombination(List<Card> hand) {
        List<PlayedCards> combinations = new ArrayList<>();

        // Check if the hand has at least 5 cards
        if (hand.size() < 5) {
            return combinations; // Return an empty list if the hand doesn't have enough cards
        }

        // Generate all combinations of 5 cards
        for (int i = 0; i < hand.size() - 4; i++) {
            for (int j = i + 1; j < hand.size() - 3; j++) {
                for (int k = j + 1; k < hand.size() - 2; k++) {
                    for (int l = k + 1; l < hand.size() - 1; l++) {
                        for (int m = l + 1; m < hand.size(); m++) {
                            List<Card> combinationCards = new ArrayList<>();
                            combinationCards.add(hand.get(i));
                            combinationCards.add(hand.get(j));
                            combinationCards.add(hand.get(k));
                            combinationCards.add(hand.get(l));
                            combinationCards.add(hand.get(m));
                            PlayedCards combination = new PlayedCards(combinationCards);
                            if (HandValidator.isStraightFlush(combination) ||
                                    HandValidator.isFourOfAKind(combination) ||
                                    HandValidator.isFullHouse(combination) ||
                                    HandValidator.isFlush(combination) ||
                                    HandValidator.isStraight(combination)) {
                                combinations.add(combination);
                            }
                        }
                    }
                }
            }
        }
        return combinations;
    }
}