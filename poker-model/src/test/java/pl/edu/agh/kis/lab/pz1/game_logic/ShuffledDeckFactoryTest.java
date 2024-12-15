package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ShuffledDeckFactoryTest {
    @Test
    public void shuffleDeckTest() {
        DeckFactory factory = new ShuffledDeckFactory();
        Deck deck = (Deck) factory.getDeck();

        assertEquals(52, deck.getSize());

        Set<Card> cards = new HashSet<>(deck.cards);
        assertEquals(52, cards.size());

    }
}
