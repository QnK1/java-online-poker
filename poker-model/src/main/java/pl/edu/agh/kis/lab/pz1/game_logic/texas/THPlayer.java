package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;

@Getter
@Setter
public class THPlayer extends Player {
    private Hand hand;
    private int money;
    private int currentBet;
    private String name;
}
