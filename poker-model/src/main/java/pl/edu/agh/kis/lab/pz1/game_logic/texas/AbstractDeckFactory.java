package pl.edu.agh.kis.lab.pz1.game_logic.texas;

/**
 * Factory interface for creating instances of AbstractDeck.
 * This interface provides a method to obtain a deck of cards.
 */
public interface AbstractDeckFactory {
    /**
     * Creates and returns an instance of AbstractDeck.
     *
     * @return a new instance of AbstractDeck
     */
    AbstractDeck getDeck();
}