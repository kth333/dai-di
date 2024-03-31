package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.Card.Rank;
import src.Card.Suit;

public class Art {

    private static final Map<Suit, String> SUIT_SYMBOLS = new HashMap<>();
    private static final Map<Rank, String> RANK_ABBREVIATIONS = new HashMap<>();
    private static final String FILE_PATH = "ascii/ascii.txt";

    public static void printWelcomeArt() {
        // Read and print the contents of the ASCII art file
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Replace "\n" with actual newline characters
                line = line.replace("\\n", "\n");
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        SUIT_SYMBOLS.put(Suit.DIAMONDS, "♦");
        SUIT_SYMBOLS.put(Suit.HEARTS, "♥");
        SUIT_SYMBOLS.put(Suit.CLUBS, "♣");
        SUIT_SYMBOLS.put(Suit.SPADES, "♠");

        // Using single-character abbreviations for all ranks, including "T" for "TEN"
        RANK_ABBREVIATIONS.put(Rank.THREE, "3 ");
        RANK_ABBREVIATIONS.put(Rank.FOUR, "4 ");
        RANK_ABBREVIATIONS.put(Rank.FIVE, "5 ");
        RANK_ABBREVIATIONS.put(Rank.SIX, "6 ");
        RANK_ABBREVIATIONS.put(Rank.SEVEN, "7 ");
        RANK_ABBREVIATIONS.put(Rank.EIGHT, "8 ");
        RANK_ABBREVIATIONS.put(Rank.NINE, "9 ");
        RANK_ABBREVIATIONS.put(Rank.TEN, "10"); // TEN as T
        RANK_ABBREVIATIONS.put(Rank.JACK, "J ");
        RANK_ABBREVIATIONS.put(Rank.QUEEN, "Q ");
        RANK_ABBREVIATIONS.put(Rank.KING, "K ");
        RANK_ABBREVIATIONS.put(Rank.ACE, "A ");
        RANK_ABBREVIATIONS.put(Rank.TWO, "2 ");
    }

    public static String getCardString(Card card, boolean isFirstCard, boolean isLastCard) {
        String suitSymbol = SUIT_SYMBOLS.get(card.getSuit());
        String rankAbbreviation = RANK_ABBREVIATIONS.get(card.getRank());

        // Determine the maximum length of rank abbreviations in the deck
        int maxRankAbbreviationLength = RANK_ABBREVIATIONS.values().stream().mapToInt(String::length).max().orElse(0);

        // Calculate padding for rank abbreviation
        String paddedRankAbbreviation = rankAbbreviation
                + " ".repeat(maxRankAbbreviationLength - rankAbbreviation.length());

        // Create card lines with padded rank abbreviation and suit symbol
        // String indexBorder = String.format(" %d ", index);
        String topBottom = " _______ ";
        String secondLine = String.format("|%-2s     |", paddedRankAbbreviation); // Pad rank to 2 characters
        String middleLine = String.format("|   %-2s  |", suitSymbol); // Pad suit to 2 characters
        String fourthLine = String.format("|     %-2s|", paddedRankAbbreviation); // Pad rank to 2 characters

        // Adjust bottom border for non-first and non-last cards
        String bottomBorder = "|_______|"; // Default bottom border
        if (!isFirstCard) {
            if (isLastCard) {
                bottomBorder = "  |_______|"; // Add one space before the vertical line
            } else {
                bottomBorder = "  |_______|"; // Add two spaces before the vertical line
            }
        }

        return String.join("\n", topBottom, secondLine, middleLine, fourthLine, bottomBorder);
    }
    
    public static String getAsciiRepresentation(List<Card> cards) {
        // Initialize a StringBuilder to hold the entire ASCII representation
        StringBuilder asciiRepresentation = new StringBuilder();
        boolean isFirstCard = true;
    
        // Prepare StringBuilder arrays to hold each line of the ASCII representation for all cards
        StringBuilder[] cardLines = new StringBuilder[5]; // 5 lines in the ASCII representation
        for (int i = 0; i < cardLines.length; i++) {
            cardLines[i] = new StringBuilder();
        }
    
        // Fill StringBuilder arrays with the ASCII art of each card, line by line
        for (Card card : cards) {
            String cardString = getCardString(card, isFirstCard, cards.indexOf(card) == cards.size() - 1);
            isFirstCard = false;
            String[] lines = cardString.split("\n"); // Split the cardString into lines
    
            for (int i = 0; i < lines.length; i++) {
                // Append each line of the current card's ASCII art to the corresponding StringBuilder
                cardLines[i].append(lines[i]);
                if (i < lines.length - 1) {
                    // Add spacing between cards for all but the last line to align vertically
                    cardLines[i].append("  ");
                }
            }
        }
    
        // Combine all StringBuilder objects into a single string, separating lines by '\n'
        for (StringBuilder line : cardLines) {
            asciiRepresentation.append(line).append("\n");
        }
    
        // Return the entire ASCII representation as a string
        return asciiRepresentation.toString();
    }
}
