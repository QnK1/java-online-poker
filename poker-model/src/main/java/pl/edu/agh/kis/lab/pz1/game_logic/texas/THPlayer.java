package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;

@Getter
@Setter
public class THPlayer extends Player {
    /**
     * The hand of the player, consisting of the cards they hold.
     */
    private Hand hand;

    /**
     * The amount of money the player has available to bet.
     */
    private int money;

    /**
     * The current bet placed by the player in the ongoing round.
     */
    private int currentBet;
}
