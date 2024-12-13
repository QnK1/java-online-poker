package pl.edu.agh.kis.lab.pz1.game_logic;


import pl.edu.agh.kis.lab.pz1.game_exceptions.TooFewHiddenCardsException;

import java.util.ArrayList;
import java.util.List;

public class CommunityCards{
    private List<Card> hiddenCards;
    private List<Card> visibleCards;

    public CommunityCards(List<Card> cards){
        hiddenCards = cards;
        visibleCards = new ArrayList<>();
    }

    public CommunityCards(){
        hiddenCards = new ArrayList<>();
        visibleCards = new ArrayList<>();
    }

    public void clear(){
        hiddenCards.clear();
        visibleCards.clear();
    }

    public int getNumberOfHiddenCards(){
        return hiddenCards.size();
    }

    public int getNumberOfVisibleCards(){
        return visibleCards.size();
    }

    public void showCards(int n){
        if(n > hiddenCards.size()){
            throw new TooFewHiddenCardsException("Too few hidden cards");
        }

        visibleCards.addAll(hiddenCards.subList(hiddenCards.size() - n, hiddenCards.size()));
        for(int i = n; i > 0; --i){
            hiddenCards.remove(hiddenCards.size() - 1);

        }
    }

    public List<Card> getHiddenCards(){
        return hiddenCards;
    }

    public List<Card> getVisibleCards(){
        return visibleCards;
    }
}
