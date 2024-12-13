package pl.edu.agh.kis.lab.pz1;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_logic.Card;
import pl.edu.agh.kis.lab.pz1.game_logic.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HandTest {

    private Hand hand;
    private List<Card> initialCards;

    @Before
    public void setUp() {
        initialCards = Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.HEARTS),
                new Card(Card.Rank.KING, Card.Suit.DIAMONDS)
        );
        hand = new Hand(new ArrayList<>(initialCards));
    }

    @Test
    public void testDefaultConstructor() {
        Hand defaultHand = new Hand();
        assertNotNull(defaultHand);
        assertTrue(defaultHand.getCards().isEmpty());
    }

    @Test
    public void testConstructorWithCards() {
        assertEquals(2, hand.getCards().size());
        assertEquals(initialCards, hand.getCards());
    }

    @Test
    public void testClear() {
        hand.clear();
        assertEquals(0, hand.getCards().size());
    }

    @Test
    public void testClearOnEmptyHand() {
        Hand emptyHand = new Hand();
        emptyHand.clear();
        assertEquals(0, emptyHand.getCards().size());
    }

    @Test
    public void testAddCardToEmptyHand() {
        Hand emptyHand = new Hand();
        Card card = new Card(Card.Rank.TEN, Card.Suit.SPADES);
        emptyHand.addCard(card);

        assertEquals(1, emptyHand.getCards().size());
        assertEquals(card, emptyHand.getCards().get(0));
    }
}

