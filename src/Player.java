public class Player {
    private String name;
    private CardList hand;
    private double points;

    public Player(String name) {
        this.name = name;
        this.hand = new CardList();
        this.points = 100;
    }

    public Player(String name, double value) {
        this(name);
        this.points = value;
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.removeCard(card);
            // might need to add the card to the area being played
        }
    }

    public CardList getHand() {
        return hand;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return this.points;
    }

    public void addPoints(double add) {
        points += add;
    }

    public void deductPoints(double deduct) {
        points -= deduct;
    }

    public double loseGame(double rate) {
        double deduct = rate * getNumOfCards();
        deductPoints(deduct);
        return deduct;
    }

    public void removeAllCards() {
        hand.removeAllCards();
    }

    public int getNumOfCards() {
        return hand.size();
    }

    public void sortHand() {
        hand.sort();
    }

    public boolean has(Card card) {
        return hand.contains(card);
    }

    public CardList play(Integer cardIndex) {
        if (cardIndex == null) {
            return null;
        }

        CardList playedCards = new CardList();
        for (int index=0;index<cardIndex;index++) {
            if (index < hand.size()) {
                Card card = hand.getCard(index);
                playedCards.addCard(card);
                hand.removeCard(card);
            }
        }
        return playedCards;
    }

}

