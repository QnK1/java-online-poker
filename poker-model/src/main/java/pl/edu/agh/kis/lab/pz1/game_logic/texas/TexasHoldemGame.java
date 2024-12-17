package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_exceptions.InvalidNumberOfPlayersException;
import pl.edu.agh.kis.lab.pz1.game_logic.Game;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class TexasHoldemGame extends Game {
    public enum GamePhase{
        PRE_FLOP,
        FLOP,
        TURN,
        RIVER
    }

    private final List<Player> players;
    private final Set<Player> folded;
    private Deck deck;
    private CommunityCards communityCards;
    private int currentPot;
    private final int smallBlindBet;
    private GamePhase gamePhase;
    private int activePlayerIndex;
    private final ClassicHandRanker handRanker;
    private final Map<GamePhase, Set<Action>> allowedActions;
    private boolean gameOver;
    private final Set<Player> checkedThisTurn;
    private final Set<Player> movedThisTurn;

    private List<String> lastWinnerNames;
    private int lastWin;
    private List<String> lastWinningCards;

    public TexasHoldemGame(List<Player> players, int initialMoney, int smallBlindBet) {
        if(initialMoney <= 0 || smallBlindBet <= 0 || initialMoney < smallBlindBet * 2){
            throw new IllegalArgumentException("Invalid money");
        }

        if(players == null || players.size() < 2){
            throw new InvalidNumberOfPlayersException("You must have at least two players");
        }

        this.checkedThisTurn = new HashSet<>();
        this.movedThisTurn = new HashSet<>();
        this.gameOver = false;

        this.lastWinnerNames = null;
        this.lastWin = 0;
        this.lastWinningCards = null;

        this.handRanker = new ClassicHandRanker();

        this.players = players;
        this.folded = new HashSet<>();

        ShuffledDeckFactory df = new ShuffledDeckFactory();

        this.deck = df.getDeck();
        this.communityCards = new CommunityCards();

        this.currentPot = 0;
        this.smallBlindBet = smallBlindBet;
        this.gamePhase = null;
        initializeMoney(initialMoney);

        allowedActions = new EnumMap<>(GamePhase.class);
        allowedActions.put(GamePhase.PRE_FLOP, new HashSet<>());
        allowedActions.put(GamePhase.FLOP, new HashSet<>());
        allowedActions.put(GamePhase.TURN, new HashSet<>());
        allowedActions.put(GamePhase.RIVER, new HashSet<>());

        allowedActions.get(GamePhase.PRE_FLOP).add(Action.CALL);
        allowedActions.get(GamePhase.PRE_FLOP).add(Action.RAISE);
        allowedActions.get(GamePhase.PRE_FLOP).add(Action.FOLD);
        allowedActions.get(GamePhase.PRE_FLOP).add(Action.LEAVE_GAME);

        allowedActions.get(GamePhase.FLOP).add(Action.CALL);
        allowedActions.get(GamePhase.FLOP).add(Action.RAISE);
        allowedActions.get(GamePhase.FLOP).add(Action.FOLD);
        allowedActions.get(GamePhase.FLOP).add(Action.CHECK);
        allowedActions.get(GamePhase.FLOP).add(Action.LEAVE_GAME);

        allowedActions.get(GamePhase.TURN).add(Action.CALL);
        allowedActions.get(GamePhase.TURN).add(Action.RAISE);
        allowedActions.get(GamePhase.TURN).add(Action.FOLD);
        allowedActions.get(GamePhase.TURN).add(Action.CHECK);
        allowedActions.get(GamePhase.TURN).add(Action.LEAVE_GAME);

        allowedActions.get(GamePhase.RIVER).add(Action.CALL);
        allowedActions.get(GamePhase.RIVER).add(Action.RAISE);
        allowedActions.get(GamePhase.RIVER).add(Action.FOLD);
        allowedActions.get(GamePhase.RIVER).add(Action.CHECK);
        allowedActions.get(GamePhase.RIVER).add(Action.LEAVE_GAME);
    }

    @Override
    public void startGame(){
        setupRound();
    }

    public boolean act(Player player, Action action, int param){
        if(action == Action.LEAVE_GAME){
            removePlayer(player);
        }
        else if(!isActionValid(player, action, param)){
            return false;
        }

        if(!allowedActions.get(gamePhase).contains(action)){
            return false;
        }

        switch(action){
            case CALL:
                makeBet(player, getMaxBet() - player.getCurrentBet());
                movedThisTurn.add(player);
            break;

            case RAISE:
                if(param <= getMaxBet() - player.getCurrentBet()){
                    return false;
                }
                makeBet(player, param);
                movedThisTurn.add(player);
            break;

            case CHECK:
                if(!areAllBetsEqual() || checkedThisTurn.contains(player)){
                    return false;
                }
                else{
                    checkedThisTurn.add(player);
                    movedThisTurn.add(player);
                }
            break;

            case LEAVE_GAME, FOLD:
                folded.add(player);
            break;

        }

        if(!gameOver && isRoundOver()){
            saveWinnerInfo();

            distributeMoney();

            if(numberOfPlayersWIthMoney() >= 2){
                setupRound();
            } else{
              gameOver = true;
            }

        }
        else if(action != Action.LEAVE_GAME || getCurrentPlayers().size() >= 2){
            if(areAllBetsEqual() && getCurrentPlayers().size() <= movedThisTurn.size()){
                nextPlayer();
                nextPhase();
            }
            else{
                nextPlayer();
            }
        }

        return true;
    }

    private void saveWinnerInfo(){
        this.lastWinnerNames = handRanker.pickWinner(getCurrentPlayers(), communityCards).stream()
                .map(Player::getName).toList();
        this.lastWin = this.currentPot / this.lastWinnerNames.size();

        List<Card> cards = new ArrayList<>(handRanker.pickWinner(getCurrentPlayers(), communityCards).get(0).getHand().getCards());
        cards.addAll(communityCards.getVisibleCards());
        this.lastWinningCards = handRanker.findComboFromHand(cards).cards().stream().map(Card::toString).toList();
    }

    private void nextPhase(){
        checkedThisTurn.clear();
        movedThisTurn.clear();

        switch(gamePhase){
            case PRE_FLOP:
                communityCards.showCards(3);
                gamePhase = GamePhase.FLOP;
            break;

            case FLOP:
                communityCards.showCards(1);
                gamePhase = GamePhase.TURN;
            break;

            case TURN:
                communityCards.showCards(1);
                gamePhase = GamePhase.RIVER;
            break;

            case RIVER:
                saveWinnerInfo();
                distributeMoney();
                setupRound();
            break;
        }
    }

    private boolean isActionValid(Player player, Action action, int param){
        return !(gameOver || param < 0 || isRoundOver() || !isPlayerActive(player) ||
                (action == Action.RAISE && param <= getMaxBet() - player.getCurrentBet()) ||
                action == Action.CALL && areAllBetsEqual() ||
                action == Action.CALL && getMaxBet() - player.getCurrentBet() > player.getMoney() ||
                param > player.getMoney()
                );
    }

    private void removePlayer(Player player){
        players.remove(player);
        if(players.size() < 2){
            gameOver = true;
        }
    }

    private int getMaxBet(){
        int maxBet = 0;
        List<Player> currentPlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();
        for(Player player : currentPlayers){
            maxBet = Math.max(maxBet, player.getCurrentBet());
        }

        return maxBet;
    }

    private int numberOfPlayersWIthMoney(){
        var playersWithMoney = players.stream()
                .filter(player -> player.getMoney() >= 2 * smallBlindBet).toList();


        return playersWithMoney.size();
    }

    private boolean isPlayerActive(Player player){
        return player == players.get(activePlayerIndex);
    }

    private boolean areAllBetsEqual(){
        List<Player> activePlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();

        int bet = activePlayers.get(0).getCurrentBet();
        for(Player player : activePlayers){
            if(player.getCurrentBet() != bet) {
                return false;
            }
        }

        return true;
    }

    private boolean isRoundOver(){
        return getNumberOfCurrentPlayers() < 2 || (areAllBetsEqual() && gamePhase == GamePhase.RIVER
        && getCurrentPlayers().size() <= movedThisTurn.size());
    }


    private List<Player> getWinners(){
        return !getCurrentPlayers().isEmpty() ?
                handRanker.pickWinner(getCurrentPlayers(), communityCards) : null;
    }

    private void distributeMoney(){
        if(players.size() >= 2){
            List<Player> winners = getWinners();
            int nOfWinners = winners.size();

            for(Player winner : winners){
                winner.setMoney(winner.getMoney() + currentPot / nOfWinners);
            }

            this.currentPot = 0;
        }
        else if(players.size() == 1){
            players.get(0).setMoney(players.get(0).getMoney() + this.currentPot);
            gameOver = true;
        }

    }

    private List<Player> getCurrentPlayers(){
        return players.stream().filter(player -> !folded.contains(player)).toList();
    }

    private void setupRound(){
        checkedThisTurn.clear();
        movedThisTurn.clear();
        rotateBlinds();
        this.folded.clear();
        this.gamePhase = GamePhase.PRE_FLOP;
        this.currentPot = 0;
        resetCards();
        dealCards();
        for(var player : players){
            player.setCurrentBet(0);
        }
        activePlayerIndex = 0;
        getBlindBets();
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

    private void getBlindBets(){
        Player smallBlind = players.get(0);
        Player bigBlind = players.get(1);

        makeBet(smallBlind, smallBlindBet);
        makeBet(bigBlind, smallBlindBet * 2);

        movedThisTurn.add(smallBlind);
        movedThisTurn.add(bigBlind);

        nextPlayer();
        nextPlayer();
    }

    private void nextPlayer(){
        do{
            activePlayerIndex = (activePlayerIndex + 1) % getNumberOfCurrentPlayers();
        } while(folded.contains(players.get(activePlayerIndex)));
    }

    private void makeBet(Player player, int amount){
        if(amount < 0 || amount > player.getMoney()) {
            throw new IllegalArgumentException("Invalid bet");
        }

        player.setCurrentBet(player.getCurrentBet() + amount);
        player.setMoney(player.getMoney() - amount);
        currentPot += amount;
    }

    private int getNumberOfCurrentPlayers(){
        List<Player> activePlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();

        return activePlayers.size();
    }

    @Override
    public String getName(){
        return "TEXAS HOLDEM";
    }
}
