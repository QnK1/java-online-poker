package pl.edu.agh.kis.lab.pz1.GameLogic;


public record Card(Card.Rank rank, Card.Suit suit) {
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    public String toString() {
        return rank + " OF " + suit;
    }
}
