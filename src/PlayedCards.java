import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayedCards {
    private Player player;
    private List<Card> cards;

    // Default constructor
    public PlayedCards() {
        this.player = null;
        this.cards = new ArrayList<>();
    }

    public PlayedCards(Player player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    // Add a card to the played cards
    public void addCard(Card card) {
        this.cards.add(card);
    }

    // Get the highest card of the hand
    public Card getHighestCard() {
        sortByRank();
        return this.cards.get(0);
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getNumOfCards() {
        return this.cards.size();
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public boolean isValidSize() {
        int size = this.cards.size();
        return size == 1 || size == 2 || size == 3 || size == 5;
    }

    public boolean winsAgainst(PlayedCards other) {
        // Always wins against null (pass)
        if (other == null) {
            return true;
        }

        int thisSize = this.getNumOfCards();
        int otherSize = other.getNumOfCards();

        // Compare the number of cards in each set
        if (thisSize != otherSize) {
            return false; // If the number of cards is different, they cannot win against each other (invalid)
        } else {
            if (thisSize == 5) {
                // Get the combination rankings for both hands
                HandValidator.CombinationRanking thisRanking = HandValidator.getCombinationRanking(this);
                HandValidator.CombinationRanking otherRanking = HandValidator.getCombinationRanking(other);

                // Compare the combination rankings
                int rankingComparison = thisRanking.compareTo(otherRanking);
                if (rankingComparison > 0) {
                    return true;
                } else if (rankingComparison == 0) {
                    // If the rankings are equal, compare the highest cards
                    int rankComparison = this.getHighestCard().compareTo(other.getHighestCard());
                    if (rankComparison > 0) {
                        return true;
                    } else if (rankComparison == 0) {
                        // If ranks are equal, compare the suits
                        int suitComparison = this.getHighestCard().getSuit().compareTo(other.getHighestCard().getSuit());
                        if (suitComparison > 0) {
                            return true;
                        }
                    }
                }
            } else {
                // If the number of cards is not five, compare based on the highest card
                int rankComparison = this.getHighestCard().compareTo(other.getHighestCard());
                if (rankComparison > 0) {
                    return true;
                } else if (rankComparison == 0) {
                    // If ranks are equal, compare the suits
                    int suitComparison = this.getHighestCard().getSuit().compareTo(other.getHighestCard().getSuit());
                    if (suitComparison > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void sortByRank() {
        Collections.sort(cards, new RankComparator());
    }

    public String getType() {
        int size = this.getNumOfCards();
        switch (size) {
            case 1:
                return "Single";
            case 2:
                if (HandValidator.isDouble(this)) {
                    return "Pair";
                } else {
                    return "Invalid";
                }
            case 3:
                if (HandValidator.isTriple(this)) {
                    return "Three of a Kind";
                } else {
                    return "Invalid";
                }
            case 5:
                if (HandValidator.isStraightFlush(this)) {
                    return "Straight Flush";
                } else if (HandValidator.isFourOfAKind(this)) {
                    return "Four of a Kind";
                } else if (HandValidator.isFullHouse(this)) {
                    return "Full House";
                } else if (HandValidator.isFlush(this)) {
                    return "Flush";
                } else if (HandValidator.isStraight(this)) {
                    return "Straight";
                }
                return "Invalid";
            default:
                return "Invalid";
        }
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < cards.size(); i++) {
            result += cards.get(i);
            if (i < cards.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}