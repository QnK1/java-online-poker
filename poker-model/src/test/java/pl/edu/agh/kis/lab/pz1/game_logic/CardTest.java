package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardTest {

    @Test
    public void cardToStringTest(){
        Card card1 = new Card(Card.Rank.THREE, Card.Suit.HEARTS);
        Card card2 = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        Card card3 = new Card(Card.Rank.TWO, Card.Suit.DIAMONDS);
        String cardString1 = card1.toString();
        String cardString2 = card2.toString();
        String cardString3 = card3.toString();
        assertEquals("THREE OF HEARTS", cardString1);
        assertEquals("ACE OF SPADES", cardString2);
        assertEquals("TWO OF DIAMONDS", cardString3);
    }
}
