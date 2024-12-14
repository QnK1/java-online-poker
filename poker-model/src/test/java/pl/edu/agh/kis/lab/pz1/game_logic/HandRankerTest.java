package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HandRankerTest {

    @Test
    public void findStraightFlushTest() {
        ClassicHandRanker handRanker = new ClassicHandRanker();

        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));

        CardCombo combo = handRanker.findStraightFlush(cards);
        assertNotNull(combo);
        assertEquals(new Card(Card.Rank.ACE, Card.Suit.HEARTS), combo.tieBreakerCards().get(0));
        assertEquals(ComboType.STRAIGHT_FLUSH, combo.comboType());

        cards.clear();
        cards.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));

        assertNull(handRanker.findStraightFlush(cards));
    }
}
