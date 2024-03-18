import java.util.ArrayList;
import java.util.List;

public class HandValidator {

    public enum CombinationRanking {
        SINGLE, DOUBLE, TRIPLE, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH
    };

    public static boolean isStraightFlush(PlayedCards playedCards) {
        return isStraight(playedCards) && isFlush(playedCards);
    }

    public static boolean isFourOfAKind(PlayedCards playedCards) {
        List<Card> cards = playedCards.getCards();
        if (cards.size() != 5) {
            return false;
        }
        int[] rankCount = new int[Card.Rank.values().length];
        for (Card card : cards) {
            rankCount[card.getRank().ordinal()]++;
        }
        for (int count : rankCount) {
            if (count >= 4) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFullHouse(PlayedCards playedCards) {
        List<Card> cards = playedCards.getCards();
        if (cards.size() != 5) {
            return false;
        }
        int[] rankCount = new int[Card.Rank.values().length];
        for (Card card : cards) {
            rankCount[card.getRank().ordinal()]++;
        }
        boolean foundTriple = false;
        boolean foundPair = false;
        for (int count : rankCount) {
            if (count == 3) {
                foundTriple = true;
            } else if (count == 2) {
                foundPair = true;
            }
        }
        return foundTriple && foundPair;
    }

    public static boolean isFlush(PlayedCards playedCards) {
        List<Card> cards = playedCards.getCards();
        if (cards.size() != 5) {
            return false;
        }
        // Make the first card suit and check consecutive suited cards
        Card.Suit firstCardSuit = cards.get(0).getSuit();

        // Check if all the cards have the same suit as the first hand
        // If one is not the same suit, return false
        for (Card card : cards) {
            if (card.getSuit() != firstCardSuit) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStraight(PlayedCards playedCards) {
        List<Card> cards = playedCards.getCards();
        if (cards.size() != 5) {
            return false;
        }
        playedCards.sortByRank();
        // if (hand.getCard(0).getRank().ordinal() == 0) { // check if lowest rank is 0
        // for (int i = 1; i < hand.size(); i++) {
        // Card currentCard = hand.getCard(i);
        // if (currentCard.getRank().ordinal() >= 9) { // check if rank 9 or above exist
        // Rank newRank = currentCard.getRank() - 13;
        // currentCard.setRank(newRank);
        // // hand.setCard(i, currentCard); // Update the card in the hand list with the
        // new rank
        // }
        // }
        // hand.sortByRank(); // sort again
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i + 1).getRank().ordinal() != cards.get(i).getRank().ordinal() + 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTriple(PlayedCards playedCards) {
        List<Card> cards = playedCards.getCards();
        if (cards.size() != 3) {
            return false;
        }

        // Get the rank of the first card
        Card.Rank firstCardRank = cards.get(0).getRank();

        // Check if all cards have the same rank as the first card
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getRank() != firstCardRank) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDouble(PlayedCards playedCards) {
        if (playedCards.getNumOfCards() == 2) {
            Card firstCard = playedCards.getCards().get(0);
            Card secondCard = playedCards.getCards().get(1);
            return firstCard.getRank() == secondCard.getRank();
        }
        return false;
    }

    public static boolean isSingle(PlayedCards playedCards) {
        return playedCards.getNumOfCards() == 1;
    }

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
                    // If previousCards was a combination of 5 cards, generate all possible
                    // combinations
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

    private static List<PlayedCards> findSingles(List<Card> hand) {
        List<PlayedCards> singles = new ArrayList<>();
        for (Card card : hand) {
            List<Card> single = new ArrayList<>();
            single.add(card);
            singles.add(new PlayedCards(null, single));
        }
        return singles;
    }

    private static List<PlayedCards> findDoubles(List<Card> hand) {
        List<PlayedCards> doubles = new ArrayList<>();
        for (int i = 0; i < hand.size() - 1; i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                List<Card> doubleCards = new ArrayList<>();
                doubleCards.add(hand.get(i));
                doubleCards.add(hand.get(j));
                PlayedCards doubleCombination = new PlayedCards(null, doubleCards);
                if (doubleCombination.isValid() && HandValidator.isDouble(doubleCombination)) {
                    doubles.add(doubleCombination);
                }
            }
        }
        return doubles;
    }

    private static List<PlayedCards> findTriples(List<Card> hand) {
        List<PlayedCards> triples = new ArrayList<>();
        for (int i = 0; i < hand.size() - 2; i++) {
            for (int j = i + 1; j < hand.size() - 1; j++) {
                for (int k = j + 1; k < hand.size(); k++) {
                    List<Card> tripleCards = new ArrayList<>();
                    tripleCards.add(hand.get(i));
                    tripleCards.add(hand.get(j));
                    tripleCards.add(hand.get(k));
                    PlayedCards tripleCombination = new PlayedCards(null, tripleCards);
                    if (tripleCombination.isValid() && HandValidator.isTriple(tripleCombination)) {
                        triples.add(tripleCombination);
                    }
                }
            }
        }
        return triples;
    }

    private static List<PlayedCards> findFiveCombination(List<Card> hand) {
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
                            PlayedCards combination = new PlayedCards(null, combinationCards);
                            if (combination.isValid() && HandValidator.isStraightFlush(combination) ||
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

    public static CombinationRanking getCombinationRanking(PlayedCards playedCards) {
        if (isSingle(playedCards)) {
            return CombinationRanking.SINGLE;
        } else if (isDouble(playedCards)) {
            return CombinationRanking.DOUBLE;
        } else if (isTriple(playedCards)) {
            return CombinationRanking.TRIPLE;
        } else if (isStraightFlush(playedCards)) {
            return CombinationRanking.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(playedCards)) {
            return CombinationRanking.FOUR_OF_A_KIND;
        } else if (isFullHouse(playedCards)) {
            return CombinationRanking.FULL_HOUSE;
        } else if (isFlush(playedCards)) {
            return CombinationRanking.FLUSH;
        } else if (isStraight(playedCards)) {
            return CombinationRanking.STRAIGHT;
        } else {
            return null;
        }
    }
}
