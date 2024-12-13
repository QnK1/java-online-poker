package pl.edu.agh.kis.lab.pz1.game_logic;

import java.util.List;

public class ClassicHandRanker implements HandRanker {
    @Override
    public int pickTopHand(CommunityCards communityCards, List<Hand> hand){
        return 0;
    }

    private CardCombo findComboFromHand(CommunityCards communityCards, Hand hand){
        return null;
    }

    private CardCombo findStraightFlush(CommunityCards communityCards, Hand hand){

    }
}