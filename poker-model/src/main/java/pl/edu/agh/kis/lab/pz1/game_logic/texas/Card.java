package pl.edu.agh.kis.lab.pz1.game_logic.texas;

/**
 * Represents a playing card with a rank and suit.
 * This class is immutable and implements {@link Comparable} to allow comparison by rank.
 *
 * @param rank the rank of the card (e.g., ACE, KING, etc.)
 * @param suit the suit of the card (e.g., HEARTS, SPADES, etc.)
 */
public record Card(Card.Rank rank, Card.Suit suit) implements Comparable<Card> {

    /**
     * Enum representing the four suits in a standard deck of playing cards.
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    /**
     * Enum representing the thirteen ranks in a standard deck of playing cards.
     */
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    /**
     * Returns a string representation of the card in the format "RANK OF SUIT".
     *
     * @return the string representation of the card
     */
    @Override
    public String toString() {
        return rank + " OF " + suit;
    }

    /**
     * Compares this card to another card based on their rank.
     *
     * @param o the card to compare to
     * @return a negative integer, zero, or a positive integer if this card's rank
     *         is less than, equal to, or greater than the specified card's rank
     */
    @Override
    public int compareTo(Card o) {
        return rank.compareTo(o.rank);
    }
}
