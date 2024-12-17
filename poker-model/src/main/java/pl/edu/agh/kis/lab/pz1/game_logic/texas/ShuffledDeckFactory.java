package pl.edu.agh.kis.lab.pz1.game_logic.texas;

public class ShuffledDeckFactory implements AbstractDeckFactory {
    @Override
    public Deck getDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }
}
