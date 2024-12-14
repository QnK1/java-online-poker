package pl.edu.agh.kis.lab.pz1.game_logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassicHandRanker implements HandRanker {
    @Override
    public List<Integer> pickTopHand(CommunityCards communityCards, List<Hand> hand){
        return new ArrayList<>(0);
    }

    protected CardCombo findComboFromHand(List<Card> cards){
        return null;
    }

    public CardCombo findStraightFlush(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();

        return null;
    }
}