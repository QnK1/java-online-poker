package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_exceptions.DeckIsEmptyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a standard deck of playing cards for a Texas Hold'em game.
 * The deck contains 52 cards, and operations such as shuffling and popping cards are supported.
 */
@Getter
public class Deck extends AbstractDeck {

    /**
     * Constructor that initializes the deck with all 52 standard playing cards.
     * The deck is created with all combinations of ranks and suits.
     */
    public Deck() {
        cards = new ArrayList<>();

        for (var rank : Card.Rank.values()) {
            for (var suit : Card.Suit.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the cards in the deck using a random shuffle.
     */
    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pops (removes and returns) the top card from the deck.
     * If the deck is empty, a DeckIsEmptyException is thrown.
     *
     * @return The top card from the deck.
     * @throws DeckIsEmptyException if the deck is empty.
     */
    @Override
    public Card popCard() throws DeckIsEmptyException {
        if (cards.isEmpty()) {
            throw new DeckIsEmptyException("Tried to pop card from empty deck.");
        }

        return cards.remove(0);
    }

    /**
     * Pops (removes and returns) a specified number of cards from the deck.
     * If the deck does not contain enough cards, a DeckIsEmptyException is thrown.
     *
     * @param n The number of cards to pop from the deck.
     * @return A list of cards removed from the deck.
     * @throws DeckIsEmptyException if the deck is empty or contains fewer cards than requested.
     */
    public List<Card> popCards(int n) throws DeckIsEmptyException {
        List<Card> cardsToPop = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cardsToPop.add(popCard());
        }

        return cardsToPop;
    }

    /**
     * Gets the current size of the deck (the number of remaining cards).
     *
     * @return The number of cards remaining in the deck.
     */
    public int getSize() {
        return cards.size();
    }

}
