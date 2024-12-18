package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;

import java.util.List;

/**
 * Abstract representation of a deck of cards.
 * This class provides basic functionality for managing a deck of cards, including shuffling and retrieving cards.
 */
@Getter
public abstract class AbstractDeck {
    /**
     * List of cards in the deck.
     */
    protected List<Card> cards;

    /**
     * Shuffles the deck of cards, randomizing their order.
     */
    public abstract void shuffle();

    /**
     * Removes and returns the top card from the deck.
     *
     * @return the top card of the deck
     */
    public abstract Card popCard();
}