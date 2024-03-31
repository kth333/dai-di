public class asciiTest {
    public static void main(String[] args) {
        // Create a Card instance with the specified suit and rank
        Card card = new Card(Card.Rank.THREE, Card.Suit.DIAMONDS);

        // Create an instance of the Art class
        Art art = new Art();

        // Call the printAsciiArt() method with the card object
        art.printAsciiArt(card);

        System.out.println("♥");
        System.out.println("♦");
        System.out.println("♣");
        System.out.println("♠");
    }
}