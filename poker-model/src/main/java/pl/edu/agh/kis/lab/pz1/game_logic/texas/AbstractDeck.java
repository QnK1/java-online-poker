package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class AbstractDeck {
    protected List<Card> cards;

    public abstract void shuffle();
    public abstract Card popCard();
}
