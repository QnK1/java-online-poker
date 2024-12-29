package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_exceptions.InvalidNumberOfPlayersException;
import pl.edu.agh.kis.lab.pz1.game_logic.Game;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;

import java.util.*;

/**
 * Class representing a Texas Hold'em game, extending the abstract Game class.
 * It manages the game state, players, community cards, and the game phases.
 */
@Getter
public class TexasHoldemGame extends Game {

    /**
     * Enum representing the different phases of the Texas Hold'em game.
     */
    public enum GamePhase {
        PRE_FLOP,  // Before community cards are revealed
        FLOP,      // First three community cards are revealed
        TURN,      // Fourth community card is revealed
        RIVER      // Fifth and final community card is revealed
    }

    private final List<THPlayer> players;  // List of players in the game
    private final Set<THPlayer> folded;    // Set of players who have folded
    private Deck deck;                     // The deck used in the game
    private CommunityCards communityCards; // Community cards on the table
    private int currentPot;                // The current pot of money in the game
    private final int smallBlindBet;       // The small blind bet value
    private GamePhase gamePhase;           // The current phase of the game
    private int activePlayerIndex;         // The index of the currently active player
    private final ClassicHandRanker handRanker;  // Hand ranking strategy used to determine winners
    private final Map<GamePhase, Set<Action>> allowedActions; // Map of allowed actions for each game phase
    private boolean gameOver;              // Indicates if the game is over
    private final Set<THPlayer> checkedThisTurn; // Set of players who have checked this turn
    private final Set<THPlayer> movedThisTurn;  // Set of players who have moved this turn

    private List<String> lastWinnerNames;     // Names of the last round's winners
    private int lastWin;                      // Amount won in the last round
    private List<String> lastWinningCards;    // Cards that contributed to the last round's win
    private final Map<String, Action> allowedCommands; // Map of command strings to corresponding actions
    boolean newWinnerFound;                  // Flag indicating whether a new winner was found



    /**
     * Constructs a TexasHoldemGame with the specified parameters.
     *
     * @param players The list of players in the game.
     * @param initialMoney The initial amount of money for each player.
     * @param smallBlindBet The value of the small blind bet.
     * @throws IllegalArgumentException if the initial money or small blind bet is invalid.
     * @throws InvalidNumberOfPlayersException if there are fewer than two players.
     */
    public TexasHoldemGame(List<THPlayer> players, int initialMoney, int smallBlindBet) {
        if (initialMoney <= 0 || smallBlindBet <= 0 || initialMoney < smallBlindBet * 2) {
            throw new IllegalArgumentException("Invalid money");
        }

        if (players == null || players.size() < 2) {
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

        allowedCommands = new HashMap<>();
        allowedCommands.put("LEAVE", Action.LEAVE_GAME);
        allowedCommands.put("CALL", Action.LEAVE_GAME);
        allowedCommands.put("CHECK", Action.CHECK);
        allowedCommands.put("RAISE", Action.RAISE);
        allowedCommands.put("FOLD", Action.FOLD);

        this.newWinnerFound = false;
    }

    /**
     * Executes a command from a player in the game.
     *
     * @param p The player who is executing the command.
     * @param command The command string to be executed.
     * @return True if the command was successfully executed, false otherwise.
     */
    @Override
    public boolean executeCommand(Player p, String command) {
        THPlayer player = (THPlayer) p;
        var args = Arrays.asList(command.strip().split(" "));
        if (args.isEmpty() || !allowedCommands.containsKey(args.get(0))) {
            return false;
        }

        return switch (args.get(0)) {
            case "LEAVE" -> act(player, Action.LEAVE_GAME, 0);
            case "CHECK" -> act(player, Action.CHECK, 0);
            case "CALL" -> act(player, Action.CALL, 0);
            case "RAISE" -> {
                if (args.size() != 2) {
                    yield false;
                }
                yield act(player, Action.RAISE, Integer.parseInt(args.get(1)));
            }
            case "FOLD" -> act(player, Action.FOLD, 0);
            default -> false;
        };
    }

    /**
     * Gets the current game state for a specific player.
     *
     * @param name The name of the player whose game state is requested.
     * @return A string representation of the current game state for the player.
     */
    @Override
    public String getGameState(String name) {
        THPlayer player = getPlayerByName(name);
        THPlayer activePlayer = players.get(activePlayerIndex);

        if (player == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Current phase: ").append(gamePhase);
        sb.append("\n");
        for (THPlayer p : players) {
            sb.append(p.getName());
            if (p == activePlayer) {
                sb.append(" (active)");
            }
            sb.append(" - bet: ").append(p.getCurrentBet()).append("\n");
        }
        sb.append("\n");
        sb.append(name);
        if (activePlayer == player) {
            sb.append(" (active)");
        }
        sb.append(" - money: ").append(player.getMoney()).append("\n");
        for (Card card : player.getHand().getCards()) {
            sb.append(card).append("\n");
        }
        sb.append("\n");
        sb.append("Table: ").append("\n");
        for (Card card : communityCards.getVisibleCards()) {
            sb.append(card).append("\n");
        }

        return sb.toString();
    }

    /**
     * Gets a player by name.
     *
     * @param name The name of the player.
     * @return The player object if found, null otherwise.
     */
    private THPlayer getPlayerByName(String name) {
        for (var player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Starts the game by setting up a round.
     */
    @Override
    public void startGame() {
        setupRound();
    }


    /**
     * Executes a player's action in the game (e.g., CALL, RAISE, CHECK, FOLD, LEAVE_GAME).
     * It validates the action and processes it accordingly, including updating the pot,
     * managing the game state, and transitioning to the next player/phase if necessary.
     *
     * @param player the player performing the action
     * @param action the action to be performed
     * @param param the additional parameter for the action (e.g., bet amount for RAISE)
     * @return true if the action was successfully executed, false otherwise
     */
    public boolean act(THPlayer player, Action action, int param){
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
                newWinnerFound = false;
            break;

            case RAISE:
                if(param <= getMaxBet() - player.getCurrentBet()){
                    return false;
                }
                makeBet(player, param);
                movedThisTurn.add(player);
                newWinnerFound = false;
            break;

            case CHECK:
                if(!areAllBetsEqual() || checkedThisTurn.contains(player)){
                    return false;
                }
                else{
                    checkedThisTurn.add(player);
                    movedThisTurn.add(player);
                    newWinnerFound = false;
                }
            break;

            case LEAVE_GAME, FOLD:
                folded.add(player);
                newWinnerFound = false;
            break;

        }

        if(!gameOver && isRoundOver()){
            saveWinnerInfo();
            newWinnerFound = true;

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

    /**
     * Saves the information about the winner(s) of the current round, including their names,
     * the amount they won, and the combination of cards they used to win.
     */
    private void saveWinnerInfo(){
        this.lastWinnerNames = handRanker.pickWinner(getCurrentPlayers(), communityCards).stream()
                .map(THPlayer::getName).toList();
        this.lastWin = this.currentPot / this.lastWinnerNames.size();

        List<Card> cards = new ArrayList<>(handRanker.pickWinner(getCurrentPlayers(), communityCards).get(0).getHand().getCards());
        cards.addAll(communityCards.getVisibleCards());
        this.lastWinningCards = handRanker.findComboFromHand(cards).cards().stream().map(Card::toString).toList();
    }

    /**
     * Advances the game to the next phase (e.g., from PRE_FLOP to FLOP, or from TURN to RIVER).
     * It also manages card visibility and the flow of the game between phases.
     */
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

    /**
     * Validates whether the specified action is valid for the given player and current game state.
     * It checks conditions such as whether the player is active, if the action is permitted in the current phase,
     * and if the player's bet is valid.
     *
     * @param player the player performing the action
     * @param action the action to be performed
     * @param param the additional parameter for the action (e.g., bet amount for RAISE)
     * @return true if the action is valid, false otherwise
     */
    private boolean isActionValid(THPlayer player, Action action, int param){
        return !(gameOver || param < 0 || isRoundOver() || !isPlayerActive(player) ||
                (action == Action.RAISE && param <= getMaxBet() - player.getCurrentBet()) ||
                action == Action.CALL && areAllBetsEqual() ||
                action == Action.CALL && getMaxBet() - player.getCurrentBet() > player.getMoney() ||
                param > player.getMoney()
                );
    }

    /**
     * Removes a player from the game, updating the game state if necessary (e.g., ending the game if there are fewer than 2 players).
     *
     * @param player the player to be removed
     */
    private void removePlayer(THPlayer player){
        players.remove(player);
        if(players.size() < 2){
            gameOver = true;
        }
    }

    /**
     * Gets the maximum bet from the current active players, considering their current bets.
     *
     * @return the maximum bet
     */
    private int getMaxBet(){
        int maxBet = 0;
        List<THPlayer> currentPlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();
        for(var player : currentPlayers){
            maxBet = Math.max(maxBet, player.getCurrentBet());
        }

        return maxBet;
    }

    /**
     * Returns the number of players who still have money greater than or equal to twice the small blind amount.
     *
     * @return the number of players with money
     */
    private int numberOfPlayersWIthMoney(){
        var playersWithMoney = players.stream()
                .filter(player -> player.getMoney() >= 2 * smallBlindBet).toList();


        return playersWithMoney.size();
    }

    /**
     * Determines if the specified player is the current active player in the game.
     *
     * @param player the player to check
     * @return true if the player is the active player, false otherwise
     */
    private boolean isPlayerActive(THPlayer player){
        return player == players.get(activePlayerIndex);
    }

    /**
     * Checks if all active players have placed the same bet in the current round.
     *
     * @return true if all bets are equal, false otherwise
     */
    private boolean areAllBetsEqual(){
        List<THPlayer> activePlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();

        int bet = activePlayers.get(0).getCurrentBet();
        for(THPlayer player : activePlayers){
            if(player.getCurrentBet() != bet) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the current round is over, either because there are fewer than two players or because all players have made their moves.
     *
     * @return true if the round is over, false otherwise
     */
    private boolean isRoundOver(){
        return getNumberOfCurrentPlayers() < 2 || (areAllBetsEqual() && gamePhase == GamePhase.RIVER
        && getCurrentPlayers().size() <= movedThisTurn.size());
    }

    /**
     * Returns a list of the players who won the current round.
     *
     * @return a list of the winning players
     */
    private List<THPlayer> getWinners(){
        return !getCurrentPlayers().isEmpty() ?
                handRanker.pickWinner(getCurrentPlayers(), communityCards) : null;
    }

    /**
     * Distributes the money in the pot to the winners of the round.
     * If only one player remains, they receive all the money.
     */
    private void distributeMoney(){
        if(players.size() >= 2){
            List<THPlayer> winners = getWinners();
            int nOfWinners = winners.size();

            for(var winner : winners){
                winner.setMoney(winner.getMoney() + currentPot / nOfWinners);
            }

            this.currentPot = 0;
        }
        else if(players.size() == 1){
            players.get(0).setMoney(players.get(0).getMoney() + this.currentPot);
            gameOver = true;
        }

    }

    /**
     * Returns the list of current players who have not folded.
     *
     * @return a list of the current active players
     */
    private List<THPlayer> getCurrentPlayers(){
        return players.stream().filter(player -> !folded.contains(player)).toList();
    }

    /**
     * Sets up a new round by clearing the game state (bets, folded players, etc.)
     * and dealing new cards to players.
     */
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

    /**
     * Resets the cards for all players and the community cards, and prepares a new shuffled deck.
     */
    private void resetCards(){
        for(var player : players){
            player.setHand(new Hand());
        }

        ShuffledDeckFactory df = new ShuffledDeckFactory();
        this.deck = df.getDeck();

        this.communityCards = new CommunityCards();
    }

    /**
     * Deals two cards to each player and sets the community cards for the round.
     */
    private void dealCards(){
        for(var player : players){
            player.setHand(new Hand(deck.popCards(2)));
        }

        communityCards.setHiddenCards(deck.popCards(5));
    }

    /**
     * Rotates the players so that the blinds shift to the next player.
     */
    private void rotateBlinds(){
        Collections.rotate(players, -1);
    }

    /**
     * Initializes the money for each player at the start of the game, setting their current bet to 0.
     *
     * @param initialMoney the initial amount of money each player has
     */
    private void initializeMoney(int initialMoney){
        for(var player : players){
            player.setMoney(initialMoney);
            player.setCurrentBet(0);
        }
    }

    /**
     * Collects the small blind and big blind bets from the first two players in the list.
     */
    private void getBlindBets(){
        THPlayer smallBlind = players.get(0);
        THPlayer bigBlind = players.get(1);

        makeBet(smallBlind, smallBlindBet);
        makeBet(bigBlind, smallBlindBet * 2);

        movedThisTurn.add(smallBlind);
        movedThisTurn.add(bigBlind);

        nextPlayer();
        nextPlayer();
    }

    /**
     * Advances the active player to the next player who has not folded.
     */
    private void nextPlayer(){
        do{
            activePlayerIndex = (activePlayerIndex + 1) % getNumberOfCurrentPlayers();
        } while(folded.contains(players.get(activePlayerIndex)));
    }

    /**
     * Makes a bet for the given player and updates the player's money and the current pot.
     *
     * @param player the player making the bet
     * @param amount the amount to bet
     */
    private void makeBet(THPlayer player, int amount){
        if(amount < 0 || amount > player.getMoney()) {
            throw new IllegalArgumentException("Invalid bet");
        }

        player.setCurrentBet(player.getCurrentBet() + amount);
        player.setMoney(player.getMoney() - amount);
        currentPot += amount;
    }

    /**
     * Returns the number of current active players who have not folded.
     *
     * @return the number of active players
     */
    private int getNumberOfCurrentPlayers(){
        List<THPlayer> activePlayers = players.stream().filter(player ->
                !folded.contains(player)).toList();

        return activePlayers.size();
    }

}
