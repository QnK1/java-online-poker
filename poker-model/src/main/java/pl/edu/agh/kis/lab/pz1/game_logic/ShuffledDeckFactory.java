package pl.edu.agh.kis.lab.pz1.game_logic;

public class ShuffledDeckFactory implements DeckFactory {
    @Override
    public Deck getDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }
}
