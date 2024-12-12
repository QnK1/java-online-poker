package pl.edu.agh.kis.lab.pz1.GameLogic;

public class ShuffledDeckFactory extends AbstractDeckFactory{
    @Override
    public Deck getDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }
}
