package pl.edu.agh.kis.lab.pz1.game_logic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Hand{
    private List<Card> cards;

    public Hand(){
        cards = new ArrayList<>();
    }

    public Hand(List<Card> cards){
        this.cards = cards;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void clear(){
        cards.clear();
    }
}
