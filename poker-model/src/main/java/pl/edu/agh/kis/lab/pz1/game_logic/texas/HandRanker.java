package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import java.util.List;

public interface HandRanker {
    List<THPlayer> pickWinner(List<THPlayer> players, CommunityCards communityCards);
}
