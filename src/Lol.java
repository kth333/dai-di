public class Lol {
    public enum Rank {
        // Enum values...
    
        public Rank adjustRank(int adjustment) {
            int newOrdinal = this.ordinal() + adjustment;
            if (newOrdinal < 0 || newOrdinal >= values().length) {
                throw new IllegalArgumentException("Invalid rank adjustment");
            }
            return values()[newOrdinal];
        }
    }
}
