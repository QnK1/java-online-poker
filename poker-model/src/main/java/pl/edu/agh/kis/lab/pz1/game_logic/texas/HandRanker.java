package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import java.util.List;

/**
 * Interface defining a contract for ranking the hands of players in a Texas Hold'em game.
 * Implementations of this interface will determine the winner(s) based on the players' hands and community cards.
 */
public interface HandRanker {

    /**
     * Picks the winner(s) from the list of players based on their hands and the community cards.
     * The winner is determined by comparing the hand rankings according to the rules of Texas Hold'em.
     *
     * @param players A list of players whose hands are to be evaluated.
     * @param communityCards The community cards shared by all players.
     * @return A list of players who have the best hand(s).
     */
    List<THPlayer> pickWinner(List<THPlayer> players, CommunityCards communityCards);
}
