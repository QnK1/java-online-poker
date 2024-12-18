package pl.edu.agh.kis.lab.pz1.game_logic.texas;

/**
 * Enumeration representing possible actions a player can take during a poker game.
 */
public enum Action {
    /**
     * Action for leaving the game permanently.
     */
    LEAVE_GAME,

    /**
     * Action for folding the current hand and forfeiting the round.
     */
    FOLD,

    /**
     * Action for checking, passing the action to the next player without betting.
     */
    CHECK,

    /**
     * Action for calling, matching the current highest bet.
     */
    CALL,

    /**
     * Action for raising, increasing the current bet amount.
     */
    RAISE
}
