package pl.edu.agh.kis.lab.pz1.game_logic;


public record Card(Card.Rank rank, Card.Suit suit) implements Comparable<Card> {
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    @Override
    public String toString() {
        return rank + " OF " + suit;
    }

    @Override
    public int compareTo(Card o) {
        return rank.compareTo(o.rank);
    }
}
