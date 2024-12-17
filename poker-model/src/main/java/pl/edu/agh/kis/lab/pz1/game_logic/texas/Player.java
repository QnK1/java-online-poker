package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private Hand hand;
    private int money;
    private int currentBet;
    private String name;
}
