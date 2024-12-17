package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_exceptions.DeckIsEmptyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
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

    public List<Card> popCards(int n) throws DeckIsEmptyException {
        List<Card> cardsToPop = new ArrayList<>();
        for(int i = 0; i<n; i++){
            cardsToPop.add(popCard());
        }

        return cardsToPop;
    }

    public int getSize(){
        return cards.size();
    }

}
