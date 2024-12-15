package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Player {
    private Hand hand;
    private int money;
    private int currentBet;
    boolean active;

    private Action queuedAction;

    void queueAction(Action action) {
        queuedAction = action;
    }

    Action readAction(Set<Action> allowedActions) {
        return null;
    }
}
