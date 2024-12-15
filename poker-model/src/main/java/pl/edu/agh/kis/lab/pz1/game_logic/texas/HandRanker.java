package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import java.util.List;

public interface HandRanker {
    List<Player> pickWinner(List<Player> players,  CommunityCards communityCards);
}
