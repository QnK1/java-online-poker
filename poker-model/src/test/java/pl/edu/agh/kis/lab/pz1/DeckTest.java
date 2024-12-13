package pl.edu.agh.kis.lab.pz1;


import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_exceptions.DeckIsEmptyException;
import pl.edu.agh.kis.lab.pz1.game_logic.Card;
import pl.edu.agh.kis.lab.pz1.game_logic.Deck;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    public void popCardOnEmptyDeckThrowsException() {
        Deck deck = new Deck();
        int size = deck.getSize();

        assertThrows(
                DeckIsEmptyException.class,
                () -> {
                    for(int i = 0; i < size + 1; i++){
                        deck.popCard();
                    }
                }
        );
    }

    @Test
    public void deckSizeIsCorrect() {
        Deck deck = new Deck();
        assertEquals(52, deck.getSize());

        deck.popCard();

        assertEquals(51, deck.getSize());
    }

    @Test
    public void pocCardsIsConsistent() {
        Deck deck = new Deck();
        List<Card> cards = deck.popCards(3);
        assertEquals(3, cards.size());
        assertEquals(49, deck.getSize());
    }

    @Test
    public void shuffleTest() {
        Deck deck = new Deck();
        deck.shuffle();
        assertEquals(52, deck.getSize());
        deck.popCard();
        deck.shuffle();
        assertEquals(51, deck.getSize());



    }
}
