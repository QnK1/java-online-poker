package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.lab.pz1.game_exceptions.TooFewHiddenCardsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the community cards in a game of Texas Hold'em.
 * Community cards are shared by all players and are revealed progressively throughout the game.
 */
@Getter
@Setter
public class CommunityCards {

    /**
     * List of hidden cards that have not yet been revealed to the players.
     */
    private List<Card> hiddenCards;

    /**
     * List of visible cards that have been revealed and are visible to all players.
     */
    private List<Card> visibleCards;

    /**
     * Constructor that initializes the community cards with the given list of hidden cards.
     *
     * @param cards A list of hidden cards.
     */
    public CommunityCards(List<Card> cards){
        hiddenCards = cards;
        visibleCards = new ArrayList<>();
    }

    /**
     * Default constructor that initializes the community cards with empty lists for hidden and visible cards.
     */
    public CommunityCards(){
        hiddenCards = new ArrayList<>();
        visibleCards = new ArrayList<>();
    }

    /**
     * Clears both the hidden and visible cards, resetting the community cards.
     */
    public void clear(){
        hiddenCards.clear();
        visibleCards.clear();
    }

    /**
     * Gets the number of hidden cards that are yet to be revealed.
     *
     * @return The number of hidden cards.
     */
    public int getNumberOfHiddenCards(){
        return hiddenCards.size();
    }

    /**
     * Gets the number of visible cards that have been revealed.
     *
     * @return The number of visible cards.
     */
    public int getNumberOfVisibleCards(){
        return visibleCards.size();
    }

    /**
     * Reveals a specified number of hidden cards and moves them to the visible cards list.
     * Throws an exception if there are not enough hidden cards to reveal.
     *
     * @param n The number of hidden cards to reveal.
     * @throws TooFewHiddenCardsException if the number of cards to reveal exceeds the available hidden cards.
     */
    public void showCards(int n){
        if(n > hiddenCards.size()){
            throw new TooFewHiddenCardsException("Too few hidden cards");
        }

        visibleCards.addAll(hiddenCards.subList(hiddenCards.size() - n, hiddenCards.size()));
        for(int i = n; i > 0; --i){
            hiddenCards.remove(hiddenCards.size() - 1);
        }
    }

}
