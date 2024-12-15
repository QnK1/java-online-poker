package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import pl.edu.agh.kis.lab.pz1.game_exceptions.InvalidNumberOfPlayersException;
import pl.edu.agh.kis.lab.pz1.game_logic.Game;

import java.util.*;

public class TexasHoldemGame extends Game {
    private enum GamePhase{
        PRE_FLOP,
        FLOP,
        TURN,
        RIVER
    };

    private final List<Player> players;
    private List<Player> currentPlayers;
    private Deck deck;
    private CommunityCards communityCards;
    private int currentPot;
    private final int smallBlindBet;
    private GamePhase gamePhase;
    private int currentPlayerIndex;

    public TexasHoldemGame(List<Player> players, int initialMoney, int smallBlindBet) {
        if(players.size() < 2){
            throw new InvalidNumberOfPlayersException("You must have at least two players");
        }

        this.players = players;
        this.currentPlayers = players;

        ShuffledDeckFactory df = new ShuffledDeckFactory();

        this.deck = df.getDeck();

        this.currentPot = 0;
        this.smallBlindBet = smallBlindBet;
        this.gamePhase = GamePhase.PRE_FLOP;
        initializeMoney(initialMoney);

    }

    @Override
    public void startGame(){
        while(players.size() >= 2){
            startNextRound();
        }
    }

    private void startNextRound(){
        setupRound();
        startPreFlop();
    }

    private void startPreFlop(){
        getBlindBets();

        Set<Action> allowedActions = new HashSet<>();
        allowedActions.add(Action.CALL);
        allowedActions.add(Action.RAISE);
        allowedActions.add(Action.LEAVE_GAME);

        startBetting(allowedActions);
    }

    private void startFlop(){

    }

    private void startTurn(){

    }

    private void startRiver(){

    }

    private void startBetting(Set<Action> allowedActions){
        for(int i = currentPlayerIndex; i < players.size(); ++i){
            currentPlayers.get(i).readAction(allowedActions);
        }
    }

    private void getBlindBets(){
        players.get(0).setCurrentBet(smallBlindBet);
        players.get(1).setCurrentBet(smallBlindBet * 2);

        currentPot += players.get(0).getCurrentBet();
        currentPot += players.get(1).getCurrentBet();

        currentPlayerIndex = 2;
    }

    private void setupRound(){
        rotateBlinds();
        this.currentPlayers = players;
        this.gamePhase = GamePhase.PRE_FLOP;
        this.currentPot = 0;
        resetCards();
        dealCards();
        for(var player : players){
            player.setCurrentBet(0);
        }
        currentPlayerIndex = 0;
    }

    private void resetCards(){
        for(var player : players){
            player.setHand(new Hand());
        }

        ShuffledDeckFactory df = new ShuffledDeckFactory();
        this.deck = df.getDeck();

        this.communityCards = new CommunityCards();
    }

    private void dealCards(){
        for(var player : players){
            player.setHand(new Hand(deck.popCards(2)));
        }

        communityCards.setHiddenCards(deck.popCards(5));
    }

    private void rotateBlinds(){
        Collections.rotate(players, -1);
    }

    private void initializeMoney(int initialMoney){
        for(var player : players){
            player.setMoney(initialMoney);
            player.setCurrentBet(0);
        }
    }
}
