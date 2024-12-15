package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import pl.edu.agh.kis.lab.pz1.game_logic.AbstractDeckFactory;

public class ShuffledDeckFactory implements AbstractDeckFactory {
    @Override
    public Deck getDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }
}
