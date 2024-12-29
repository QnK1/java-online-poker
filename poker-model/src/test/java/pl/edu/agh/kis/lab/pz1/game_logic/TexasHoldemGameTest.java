package pl.edu.agh.kis.lab.pz1.game_logic;

import org.junit.Test;
import pl.edu.agh.kis.lab.pz1.game_exceptions.InvalidNumberOfPlayersException;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.Action;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.Card;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TexasHoldemGameTest {

    @Test
    public void constructorThrowsForInvalidArgumentsTest() {
        List<THPlayer> players = new ArrayList<>();

        assertThrows(InvalidNumberOfPlayersException.class, () -> {
            new TexasHoldemGame(players, 100, 2);
        });

        players.add(new THPlayer());

        assertThrows(InvalidNumberOfPlayersException.class, () -> {
            new TexasHoldemGame(players, 100, 2);
        });

        assertThrows(InvalidNumberOfPlayersException.class, () -> {
            new TexasHoldemGame(null, 100, 2);
        });

        players.add(new THPlayer());
        players.add(new THPlayer());

        assertThrows(IllegalArgumentException.class, () -> {
            new TexasHoldemGame(players, 0, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new TexasHoldemGame(players, 3, 2);
        });
    }

    @Test
    public void dealCardsTest() {
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);

        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        List<Card> hand1 = player1.getHand().getCards();
        List<Card> hand2 = player2.getHand().getCards();
        List<Card> hiddenCards = game.getCommunityCards().getHiddenCards();

        assertEquals(2, hand1.size());
        assertEquals(2, hand2.size());
        assertEquals(5, hiddenCards.size());

        for (Card card : hand1) {
            assertFalse(hiddenCards.contains(card));
            assertFalse(hand2.contains(card));
        }
        for (Card card : hand2) {
            assertFalse(hiddenCards.contains(card));
            assertFalse(hand1.contains(card));
        }
    }

    @Test
    public void leaveGameActionTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();
        var activeTHPlayerIndex = game.getActivePlayerIndex();

        assertEquals("Bob", game.getPlayers().get(activeTHPlayerIndex).getName());
        assertFalse(game.isGameOver());
        game.act(players.get(activeTHPlayerIndex), Action.LEAVE_GAME, 0);
        assertTrue(game.isGameOver());
    }

    @Test
    public void invalidActionTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        assertFalse(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, -1));
        assertFalse(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 1));
    }

    @Test
    public void callTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));
        assertEquals(TexasHoldemGame.GamePhase.FLOP, game.getGamePhase());
        assertFalse(game.isGameOver());
        assertEquals(8, game.getCurrentPot());
        assertEquals(96, players.get(0).getMoney());
        assertEquals(96, players.get(0).getMoney());
        assertEquals(3, game.getCommunityCards().getVisibleCards().size());
        game.act(players.get(game.getActivePlayerIndex()), Action.LEAVE_GAME, 0);
        assertTrue(game.isGameOver());
    }

    @Test
    public void raiseTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));
        assertEquals(TexasHoldemGame.GamePhase.FLOP, game.getGamePhase());
        assertFalse(game.isGameOver());
        assertEquals(8, game.getCurrentPot());

        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 1));
        assertEquals(TexasHoldemGame.GamePhase.FLOP, game.getGamePhase());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 2));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));
        assertEquals(12, game.getCurrentPot());
        assertEquals(TexasHoldemGame.GamePhase.TURN, game.getGamePhase());
        assertEquals(4, game.getCommunityCards().getVisibleCards().size());
        assertEquals(1, game.getCommunityCards().getHiddenCards().size());
        assertFalse(game.isGameOver());

        int activeIndex = game.getActivePlayerIndex();
        assertFalse(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));
        assertEquals(game.getActivePlayerIndex(), activeIndex);
        assertEquals(94, players.get(0).getMoney());
        assertEquals(94, players.get(1).getMoney());
    }

    @Test
    public void foldDuringGameTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        var winner = players.get(game.getActivePlayerIndex());
        assertEquals(98, winner.getMoney());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));
        for(var player : players){
            assertEquals(4, player.getCurrentBet());
            assertEquals(96, player.getMoney());
        }

        assertEquals(8, game.getCurrentPot());
        assertEquals(TexasHoldemGame.GamePhase.FLOP, game.getGamePhase());
        assertFalse(game.isGameOver());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.FOLD, 0));

        assertFalse(game.isGameOver());
        assertEquals(TexasHoldemGame.GamePhase.PRE_FLOP, game.getGamePhase());
        assertEquals(6, game.getCurrentPot());
        assertEquals(100, winner.getMoney());
        assertEquals(0, game.getCommunityCards().getVisibleCards().size());

    }

    @Test
    public void checkTest(){
        List<THPlayer> players = new ArrayList<>();
        var player1 = new THPlayer();
        var player2 = new THPlayer();
        player1.setName("Joe");
        player2.setName("Bob");
        players.add(player1);
        players.add(player2);
        TexasHoldemGame game = new TexasHoldemGame(players, 100, 2);
        game.startGame();

        int activeIndex = game.getActivePlayerIndex();
        assertFalse(game.act(players.get(game.getActivePlayerIndex()), Action.CHECK, 0));
        assertEquals(game.getActivePlayerIndex(), activeIndex);

        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 10));
        assertEquals(TexasHoldemGame.GamePhase.PRE_FLOP, game.getGamePhase());
        assertFalse(game.isGameOver());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 11));
        assertEquals(TexasHoldemGame.GamePhase.PRE_FLOP, game.getGamePhase());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));

        assertEquals(TexasHoldemGame.GamePhase.FLOP, game.getGamePhase());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CHECK, 0));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 1));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));

        assertEquals(TexasHoldemGame.GamePhase.TURN, game.getGamePhase());
        assertEquals(16, players.get(0).getCurrentBet());
        assertEquals(4, game.getCommunityCards().getVisibleCards().size());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CHECK, 0));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 1));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));

        assertEquals(TexasHoldemGame.GamePhase.RIVER, game.getGamePhase());
        assertEquals(17, players.get(0).getCurrentBet());
        assertEquals(17, players.get(1).getCurrentBet());
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 1));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.RAISE, 2));
        assertTrue(game.act(players.get(game.getActivePlayerIndex()), Action.CALL, 0));

        var wonPot = game.getLastWin();
        assertTrue(wonPot == 38 || wonPot == 19);

    }
}
