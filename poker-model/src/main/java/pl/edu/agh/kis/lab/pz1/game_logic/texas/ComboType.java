package pl.edu.agh.kis.lab.pz1.game_logic.texas;

/**
 * Enum representing the different types of poker hands (combinations) that can be found in a game of Texas Hold'em.
 * Each enum constant corresponds to a distinct hand combination, ranked from lowest to highest.
 */
public enum ComboType {
    /**
     * Represents a hand with the highest card as the best combination (no pairs or other hands).
     */
    HIGH_CARD,

    /**
     * Represents a hand with two cards of the same rank.
     */
    PAIR,

    /**
     * Represents a hand with two separate pairs of cards.
     */
    TWO_PAIRS,

    /**
     * Represents a hand with three cards of the same rank.
     */
    THREE_OF_A_KIND,

    /**
     * Represents a hand with five consecutive cards of any suit.
     */
    STRAIGHT,

    /**
     * Represents a hand with five cards of the same suit, regardless of rank.
     */
    FLUSH,

    /**
     * Represents a hand with three cards of the same rank and two cards of another rank.
     */
    FULL_HOUSE,

    /**
     * Represents a hand with four cards of the same rank.
     */
    FOUR_OF_A_KIND,

    /**
     * Represents a hand with five consecutive cards of the same suit.
     */
    STRAIGHT_FLUSH,
}
