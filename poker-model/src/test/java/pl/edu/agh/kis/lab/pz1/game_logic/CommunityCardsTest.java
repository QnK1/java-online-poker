package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_exceptions.TooFewHiddenCardsException;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.Card;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.CommunityCards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommunityCardsTest {

    private CommunityCards communityCards;

    @Before
    public void setUp() {
        List<Card> initialCards = Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.HEARTS),
                new Card(Card.Rank.KING, Card.Suit.DIAMONDS),
                new Card(Card.Rank.QUEEN, Card.Suit.CLUBS)
        );
        communityCards = new CommunityCards(new ArrayList<>(initialCards));
    }

    @Test
    public void testConstructorWithCards() {
        assertEquals(3, communityCards.getNumberOfHiddenCards());
        assertEquals(0, communityCards.getNumberOfVisibleCards());
    }

    @Test
    public void testDefaultConstructor() {
        CommunityCards defaultCommunityCards = new CommunityCards();
        assertEquals(0, defaultCommunityCards.getNumberOfHiddenCards());
        assertEquals(0, defaultCommunityCards.getNumberOfVisibleCards());
    }

    @Test
    public void testClear() {
        communityCards.clear();
        assertEquals(0, communityCards.getNumberOfHiddenCards());
        assertEquals(0, communityCards.getNumberOfVisibleCards());
    }

    @Test
    public void testShowCardsValid() {
        communityCards.showCards(2);

        assertEquals(1, communityCards.getNumberOfHiddenCards());
        assertEquals(2, communityCards.getNumberOfVisibleCards());
    }

    @Test
    public void testShowCardsAll() {
        communityCards.showCards(3);

        assertEquals(0, communityCards.getNumberOfHiddenCards());
        assertEquals(3, communityCards.getNumberOfVisibleCards());
    }

    @Test
    public void testShowCardsInvalid() {
        assertThrows(TooFewHiddenCardsException.class, () -> {
            communityCards.showCards(4);
        });
    }

    @Test
    public void testShowCardsDoesNotModifyVisibleCardsUnexpectedly() {
        communityCards.showCards(2);

        List<Card> expectedVisibleCards = Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.DIAMONDS),
                new Card(Card.Rank.QUEEN, Card.Suit.CLUBS)
        );

        assertEquals(expectedVisibleCards, communityCards.getVisibleCards());
    }

    @Test
    public void testEmptyHiddenCards() {
        CommunityCards emptyCommunityCards = new CommunityCards();
        assertThrows(TooFewHiddenCardsException.class, () -> {
            emptyCommunityCards.showCards(1);
        });
    }

    @Test
    public void testShowCardsAfterClear() {
        communityCards.clear();
        assertThrows(TooFewHiddenCardsException.class, () -> {
            communityCards.showCards(1);
        });
    }

    @Test
    public void testGetHiddenCards() {
        List<Card> expectedCards = Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.HEARTS),
                new Card(Card.Rank.KING, Card.Suit.DIAMONDS),
                new Card(Card.Rank.QUEEN, Card.Suit.CLUBS)
        );

        assertEquals(expectedCards, communityCards.getHiddenCards());
    }
}
