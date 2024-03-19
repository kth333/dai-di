import java.util.*;

public class Hand {
    private Map<Player, List<Card>> hands;

    public Hand() {
        this.hands = new HashMap<>();
    }

    // Add a card to the hand of a specific player
    public void addCard(Player player, Card card) {
        List<Card> playerHand = hands.getOrDefault(player, new ArrayList<>());
        playerHand.add(card);
        hands.put(player, playerHand);
    }

    // Removes cards that have been played from the hand of a specific player
    public void removeCards(Player player, PlayedCards playedCards) {
        List<Card> playerHand = hands.get(player);
        if (playerHand != null) {
            playerHand.removeAll(playedCards.getCards());
        }
    }

    // Clears the hand of a specific player
    public void clear(Player player) {
        hands.remove(player);
    }

    // Returns the hand of a specific player
    public List<Card> getHand(Player player) {
        return hands.getOrDefault(player, new ArrayList<>());
    }

    // Checks if the hand of a specific player is empty
    public boolean isEmpty(Player player) {
        List<Card> playerHand = hands.get(player);
        return playerHand == null || playerHand.isEmpty();
    }

    // Returns the size of the hand of a specific player
    public int getSize(Player player) {
        List<Card> playerHand = hands.get(player);
        return playerHand != null ? playerHand.size() : 0;
    }

    // Returns a copy of the hand of a specific player to avoid modification from outside
    public List<Card> getCards(Player player) {
        List<Card> playerHand = hands.get(player);
        return playerHand != null ? new ArrayList<>(playerHand) : new ArrayList<>();
    }
}