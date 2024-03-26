import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HardBot extends Bot {

    public HardBot(List<String> playerNames, List<String> usedNames) {
        super(playerNames, usedNames);
    }

    @Override
    public PlayResult play(PlayedCards previousCards, int consecutivePasses,int turn) {
        
        // Get the bot player's hand
        List<Card> botHand = getHand().getCardsInHand();

        // Get all valid combinations in the bot's hand
        List<PlayedCards> validCombinations = getAllValidCombinations(botHand, previousCards);

        if (validCombinations != null) {
            // Check each valid combination against the previous cards
            for (PlayedCards combination : validCombinations) {

                if (previousCards == null || combination.winsAgainst(previousCards)) {
                    Card startCard=new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
                    if (hasCard(startCard) && !combination.getCards().contains(startCard)) {
                        // If bot has 3 of Diamonds and the combination doesn't contain 3 of Diamonds, continue searching
                        continue;
                    }
                    // If the combination wins or there are no previous cards, play it
                    previousCards = combination; // Update the previous cards
                    System.out.println("\n" + getName() + " played: " + combination);
                    botHand.removeAll(combination.getCards()); // Remove the played cards from the bot's hand
                    if (previousCards != null) {
                        consecutivePasses = 0;
                        return new PlayResult(this,previousCards, consecutivePasses,turn);
                    }
                }
            }
        }
        System.out.println("\n" + getName() + " passed their turn.");
        consecutivePasses++;
        return new PlayResult(this,previousCards, consecutivePasses,turn);
    }

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
                System.out.println(individual);
                return null;
            }
        }

        //if we reach here, means that the set of cards did not contain any cards stronger
        //than the minimum strength card
        return Cards;

    }   

    public static List<PlayedCards> getAllValidCombinations(List<Card> hand, PlayedCards previousCards) {
        List<PlayedCards> validCombinations = new ArrayList<>();

        if (previousCards == null) {
            // If previous card is null, the bot can play whatever he wants

            //get all the five card combis
            List<PlayedCards> potentialFiveCombinations = findFiveCombination(hand);
            for (PlayedCards p : potentialFiveCombinations){

                //if the 5 card combi doesnt use any ACES, play it
                PlayedCards worth_playing = botStrategy(p, new Card(Card.Suit.DIAMONDS, Card.Rank.ACE));
                if (worth_playing != null){
                    validCombinations.add(worth_playing);
                }
            }

            //if we go into here, it means that there was no 5 card combination that was worth playing
            //if bots hand has any 3 of a kind, he should play it
            if (validCombinations.size() == 0){

                //get all the five card combis
                List<PlayedCards> potentialThreeCombinations = findTriples(hand);

                //if the three card combination doesnt use any ACES, play it
                for (PlayedCards p : potentialThreeCombinations){
                    PlayedCards worth_playing = botStrategy(p, new Card(Card.Suit.DIAMONDS, Card.Rank.ACE));
                    if (worth_playing != null){
                        validCombinations.add(worth_playing);
                    }
                }
            }

            //if we go into here, it means that there is no good 5 card combis or 3 card combis
            //bot will try to play a pair
            if (validCombinations.size() == 0){

                //get all two card combinations
                List<PlayedCards> potentialTwoCombinations = findDoubles(hand);

                //if the pair doesnt use any TWO's, play it
                for (PlayedCards p : potentialTwoCombinations){
                    PlayedCards worth_playing = botStrategy(p, new Card(Card.Suit.DIAMONDS, Card.Rank.TWO));
                    if (worth_playing != null){
                        validCombinations.add(worth_playing);
                    }
                }
            }

            //if we get here, it means it can only play singles
            validCombinations.addAll(findSingles(hand));

        } else {
            String previousType = previousCards.getType();

            // Check if previousCards was a single, double, triple, or combination of 5
            // cards
            switch (previousType) {
                case "Single":
                    //make bot prefer to play singles that doesnt break their 5 card, 3 card, pairs
                    //if dont have, just find any random single

                    List<PlayedCards> potentialFiveCombinations = findFiveCombination(hand);
                    List<PlayedCards> potentialThreeCombinations = findTriples(hand);
                    List<PlayedCards> potentialTwoCombinations = findDoubles(hand);

                    //create hashmap to find unique cards within each combination
                    HashMap<Card, String> uniqueCards = new HashMap<Card, String>();

                    //find cards that are used for potential 5 card combis
                    for (PlayedCards p : potentialFiveCombinations){
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c) ){
                                uniqueCards.put(c, "");
                            }
                        }
                    }

                    //find cards that are used for potential 3 card combis
                    for (PlayedCards p : potentialThreeCombinations){
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c) ){
                                uniqueCards.put(c, "");
                            }
                        }
                    }

                    //find cards that are used for potential 2 card combis
                    for (PlayedCards p : potentialTwoCombinations){
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c) ){
                                uniqueCards.put(c, "");
                            }
                        }
                    }

                    List<PlayedCards> potentialSingles = findSingles(hand);
                    List<PlayedCards> efficientSingles = new ArrayList<>();

                    for (PlayedCards p : potentialSingles){
                        boolean worth_playing = true;
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c)){
                                worth_playing = false;
                            }
                        }

                        if (worth_playing){
                            efficientSingles.add(p);
                        }
                    }

                    //if couldnt find any efficient singles, just put any.
                    if (efficientSingles.size() == 0){
                        validCombinations.addAll(potentialSingles);

                    //play an efficient single card
                    } else {
                        validCombinations.addAll(efficientSingles);
                    }
                    break;

                case "Pair":
                    //get all possible combinations of pairs
                    List<PlayedCards> potentialDoubles = findDoubles(hand);

                    //if no valid pairs, just break and no need to check for efficient strategies
                    if (potentialDoubles == null){
                        System.out.println("got here");
                        break;
                    }
            
                    List<PlayedCards> efficientDoubles = new ArrayList<>();

                    potentialFiveCombinations = findFiveCombination(hand);
                    potentialThreeCombinations = findTriples(hand);

                    //create hashmap to find unique cards within each combination
                    uniqueCards = new HashMap<Card, String>();

                    //find cards that are used for potential 5 card combis
                    for (PlayedCards p : potentialFiveCombinations){
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c) ){
                                uniqueCards.put(c, "");
                            }
                        }
                    }

                    //find cards that are used for potential 3 card combis
                    for (PlayedCards p : potentialThreeCombinations){
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c) ){
                                uniqueCards.put(c, "");
                            }
                        }
                    }

                    //add 2's to hashmap its bad strategy to use 2's as a pair
                    uniqueCards.put(new Card(Card.Suit.DIAMONDS, Card.Rank.TWO), "");
                    uniqueCards.put(new Card(Card.Suit.CLUBS, Card.Rank.TWO), "");
                    uniqueCards.put(new Card(Card.Suit.HEARTS, Card.Rank.TWO), "");
                    uniqueCards.put(new Card(Card.Suit.SPADES, Card.Rank.TWO), "");

                    //if potential pairs use any valuable cards (2's or part of combo's) , dont
                    //add it to potential pairs
                    for (PlayedCards p : potentialDoubles){
                        boolean worth_playing = true;
                        List<Card> cardsWithin = p.getCards();
                        for (Card c : cardsWithin){
                            if ( uniqueCards.containsKey(c)){
                                worth_playing = false;
                            }
                        }

                        if (worth_playing){
                            efficientDoubles.add(p);
                        }
                    }

                    //if couldnt find any efficient pairs, just put any. EXCEPT 2's as
                    //it is generally a bad strategy
                    if (efficientDoubles.size() == 0){
                        for (PlayedCards p : potentialDoubles){
                            PlayedCards worth_playing = botStrategy(p, new Card(Card.Suit.DIAMONDS, Card.Rank.TWO));
                            if (worth_playing != null){
                                validCombinations.add(worth_playing);
                            }
                        }
                    } else {
                        validCombinations.addAll(efficientDoubles);
                    }
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
}