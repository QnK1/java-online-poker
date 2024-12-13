package pl.edu.agh.kis.lab.pz1;

import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_exceptions.IncorrectCardComboException;
import pl.edu.agh.kis.lab.pz1.game_logic.Card;
import pl.edu.agh.kis.lab.pz1.game_logic.CardCombo;
import pl.edu.agh.kis.lab.pz1.game_logic.ComboType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardComboTest {
    @Test
    public void testConstructorThrowsException() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        List<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));

        assertThrows(IncorrectCardComboException.class,
                () -> {
                    new CardCombo(ComboType.STRAIGHT_FLUSH, cards, cards1);
                });
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));

        cards1.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));

        assertThrows(IncorrectCardComboException.class,
                () -> {
                    new CardCombo(ComboType.STRAIGHT_FLUSH, cards, cards1);
                });

    }

    @Test
    public void testCompareToWorksCorrectly() {
        List<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards1.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards1.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        cards1.add(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        cards1.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));

        CardCombo combo1 = new CardCombo(ComboType.FOUR_OF_A_KIND, cards1, cards1.subList(3, 5));

        List<Card> cards2 = new ArrayList<>(cards1);
        cards2.remove(4);
        cards2.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));

        CardCombo combo2 = new CardCombo(ComboType.FOUR_OF_A_KIND, cards2, cards2.subList(3, 5));

        assertEquals(-1, combo1.compareTo(combo2));
        assertEquals(1, combo2.compareTo(combo1));

        cards2.remove(4);
        cards2.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));

        combo1 = new CardCombo(ComboType.FOUR_OF_A_KIND, cards2, cards2.subList(3, 5));
        combo2 = new CardCombo(ComboType.FOUR_OF_A_KIND, cards1, cards1.subList(3, 5));
        assertEquals(0, combo1.compareTo(combo2));
        assertEquals(0, combo1.compareTo(combo2));

        combo1 = new CardCombo(ComboType.HIGH_CARD, cards1, cards1);
        combo2 = new CardCombo(ComboType.FLUSH, cards1, cards1);
        assertEquals(1, combo2.compareTo(combo1));
        assertEquals(-1, combo1.compareTo(combo2));
    }
}
