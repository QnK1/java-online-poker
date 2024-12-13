package pl.edu.agh.kis.lab.pz1.game_logic;

import java.util.List;

public abstract class AbstractDeck {
    protected List<Card> cards;

    public abstract void shuffle();
    public abstract Card popCard();
}
