package pl.edu.agh.kis.lab.pz1.game_logic;

import java.util.List;

public interface HandRanker {
    int pickTopHand(CommunityCards communityCards, List<Hand> hand);
}
