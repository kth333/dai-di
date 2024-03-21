import java.util.*;

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
