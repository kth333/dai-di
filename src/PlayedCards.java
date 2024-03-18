import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayedCards {
    private Player player;
    private List<Card> cards;
    
    // Store the type of hand against this hand
    public static final String PASS = "Pass";
    public ArrayList<String> againstArrayList;

    public PlayedCards(Player player, Hand cards) {
        this.player = player;
        this.cards = new ArrayList<>();
        this.againstArrayList = new ArrayList<>();
        this.againstArrayList.add(PASS);

        if (cards != null) {
            for (int i = 0; i < cards.getSize(); i++) {
                this.addCard(cards.getCard(i));
            }
        }
    }

    // Add a card to the played cards
    public void addCard(Card card) {
        cards.add(card);
    }

    // Get the highest card of the hand
    public Card getHighestCard() {
        sortByRank();
        return this.cards.get(0);
    }

    // Get player for this hand
    public Player getPlayer() {
        return this.player;
    }

    public int getNumOfCards() {
        return this.cards.size();
    }

    public List<Card> getCards() {
        return this.cards;
    }

    private void sortByRank() {
        Collections.sort(cards, new RankComparator());
    }

    public boolean isValid() {
        return !this.cards.isEmpty();
    }

    public boolean winsAgainst(PlayedCards other) {
        if (other == null) {
            return true;
        }

        if (this.getNumOfCards() != other.getNumOfCards()) {
            return false;
        } else if (this.getType().equals(other.getType())) {
            if (this.getHighestCard().compareTo(other.getHighestCard()) > 0) {
                return true;
            } else {
                return false;
            }
        } else if (this.againstArrayList.contains(other.getType())) {
            return true;
        }
        return false;
    }

    public String getType() {
        // Implement logic to determine the type of played cards
        return "SomeType"; // Placeholder, replace with actual implementation
    }

    @Override
    public String toString() {
        String result = "Played: [";
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
