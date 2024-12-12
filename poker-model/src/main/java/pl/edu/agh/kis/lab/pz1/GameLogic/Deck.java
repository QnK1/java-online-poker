package pl.edu.agh.kis.lab.pz1.GameLogic;

import pl.edu.agh.kis.lab.pz1.GameExceptions.DeckIsEmptyException;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends AbstractDeck {
    public Deck() {
        cards = new ArrayList<>();

        for(var rank : Card.Rank.values()){
            for(var suit : Card.Suit.values()){
                cards.add(new Card(rank, suit));
            }
        }
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card popCard() throws DeckIsEmptyException {
        if(cards.isEmpty()){
            throw new DeckIsEmptyException("Tried to pop card from empty deck.");
        }

        return cards.remove(0);
    }

}
