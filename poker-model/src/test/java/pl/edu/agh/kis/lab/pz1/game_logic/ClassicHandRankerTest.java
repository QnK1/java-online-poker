package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClassicHandRankerTest {

    @Test
    public void pickWinnerTiebreakerTest(){
        ClassicHandRanker ranker = new ClassicHandRanker();

        Player p1 = new Player();
        Player p2 = new Player();

        Hand h1 = new Hand();
        Hand h2 = new Hand();

        List<Card> c1 = new ArrayList<>();
        List<Card> c2 = new ArrayList<>();
        List<Card> cc = new ArrayList<>();

        CommunityCards communityCards = new CommunityCards();

        cc.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cc.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        c1.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        c1.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));

        c2.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        c2.add(new Card(Card.Rank.FIVE, Card.Suit.CLUBS));

        h1.setCards(c1);
        h2.setCards(c2);
        p1.setHand(h1);
        p2.setHand(h2);

        communityCards.setVisibleCards(cc);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        assertEquals(c2, ranker.pickWinner(players, communityCards).get(0).getHand().getCards());
    }

    @Test
    public void pickWinnerTie(){
        ClassicHandRanker ranker = new ClassicHandRanker();

        Player p1 = new Player();
        Player p2 = new Player();

        Hand h1 = new Hand();
        Hand h2 = new Hand();

        List<Card> c1 = new ArrayList<>();
        List<Card> c2 = new ArrayList<>();
        List<Card> cc = new ArrayList<>();

        CommunityCards communityCards = new CommunityCards();

        cc.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        cc.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cc.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        c1.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        c1.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));

        c2.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        c2.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));

        h1.setCards(c1);
        h2.setCards(c2);
        p1.setHand(h1);
        p2.setHand(h2);

        communityCards.setVisibleCards(cc);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        assertEquals(2, ranker.pickWinner(players, communityCards).size());
    }

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

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(new Card(Card.Rank.ACE, Card.Suit.HEARTS), combo.tieBreakerCards().get(0));
        assertEquals(ComboType.STRAIGHT_FLUSH, combo.comboType());
    }

    @Test
    public void findFourOfAKindTest() {
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.FOUR_OF_A_KIND, combo.comboType());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.FOUR, combo.tieBreakerCards().get(1).rank());
        assertTrue(combo.cards().contains(new Card(Card.Rank.FOUR, Card.Suit.CLUBS)));
        for (var suit : Card.Suit.values()) {
            assertTrue(combo.cards().contains(new Card(Card.Rank.ACE, suit)));
        }
    }

    @Test
    public void finFullHouseTest() {
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.FULL_HOUSE, combo.comboType());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.THREE, combo.tieBreakerCards().get(1).rank());
        assertTrue(combo.cards().contains(new Card(Card.Rank.ACE, Card.Suit.CLUBS)));
        assertFalse(combo.cards().contains(new Card(Card.Rank.THREE, Card.Suit.HEARTS)) &&
                combo.cards().contains(new Card(Card.Rank.THREE, Card.Suit.SPADES)) &&
                combo.cards().contains(new Card(Card.Rank.THREE, Card.Suit.CLUBS))
        );
    }

    @Test
    public void findFlushTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.FLUSH, combo.comboType());
        assertEquals(Card.Rank.TEN, combo.tieBreakerCards().get(0).rank());
    }

    @Test
    public void findStraightTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.STRAIGHT, combo.comboType());
        assertEquals(Card.Rank.SIX, combo.tieBreakerCards().get(0).rank());

    }

    @Test
    public void findThreeOFAKindTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));

        CardCombo combo = handRanker.findComboFromHand(cards);

        assertNotNull(combo);
        assertEquals(ComboType.THREE_OF_A_KIND, combo.comboType());
        assertEquals(Card.Rank.TWO, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(1).rank());
        assertEquals(Card.Rank.JACK, combo.tieBreakerCards().get(2).rank());
    }

    @Test
    public void findTwoPairsTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.SPADES));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.TWO_PAIRS, combo.comboType());
        assertEquals(Card.Rank.JACK, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.FOUR, combo.tieBreakerCards().get(1).rank());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(2).rank());

    }

    @Test
    public void findPairTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.PAIR, combo.comboType());
        assertEquals(Card.Rank.TWO, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(1).rank());
        assertEquals(Card.Rank.JACK, combo.tieBreakerCards().get(2).rank());
        assertEquals(Card.Rank.EIGHT, combo.tieBreakerCards().get(3).rank());

    }

    @Test
    public void findHighCardTest(){
        ClassicHandRanker handRanker = new ClassicHandRanker();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.Rank.QUEEN, Card.Suit.SPADES));
        cards.add(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        cards.add(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.SIX, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        cards.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));

        CardCombo combo = handRanker.findComboFromHand(cards);
        assertNotNull(combo);
        assertEquals(ComboType.HIGH_CARD, combo.comboType());
        assertEquals(Card.Rank.ACE, combo.tieBreakerCards().get(0).rank());
        assertEquals(Card.Rank.ACE, combo.cards().get(0).rank());
        assertEquals(Card.Rank.QUEEN, combo.tieBreakerCards().get(1).rank());
        assertEquals(Card.Rank.QUEEN, combo.cards().get(1).rank());
        assertEquals(Card.Rank.TEN, combo.tieBreakerCards().get(2).rank());
        assertEquals(Card.Rank.TEN, combo.cards().get(2).rank());
        assertEquals(Card.Rank.EIGHT, combo.tieBreakerCards().get(3).rank());
        assertEquals(Card.Rank.EIGHT, combo.cards().get(3).rank());
        assertEquals(Card.Rank.SIX, combo.tieBreakerCards().get(4).rank());
        assertEquals(Card.Rank.SIX, combo.cards().get(4).rank());
    }
}
