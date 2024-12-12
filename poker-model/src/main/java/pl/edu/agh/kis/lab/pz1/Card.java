package pl.edu.agh.kis.lab.pz1;

import lombok.Data;

@Data
public class Card {
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    private final Rank rank;
    private final Suit suit;

    public String toString(){
        return rank + " OF " + suit;
    }
}
