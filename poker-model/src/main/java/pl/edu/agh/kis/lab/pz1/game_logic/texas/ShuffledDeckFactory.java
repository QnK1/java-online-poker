package pl.edu.agh.kis.lab.pz1.game_logic.texas;

/**
 * Factory class that creates a shuffled deck of cards.
 * This implementation of the AbstractDeckFactory interface returns a new deck that is shuffled.
 */
public class ShuffledDeckFactory implements AbstractDeckFactory {

    /**
     * Creates and returns a new shuffled deck of cards.
     * The deck is initialized with a standard 52 cards and shuffled before being returned.
     *
     * @return A shuffled deck of cards.
     */
    @Override
    public Deck getDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }
}
