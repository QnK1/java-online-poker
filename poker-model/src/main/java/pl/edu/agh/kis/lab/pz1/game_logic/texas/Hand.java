package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a player's hand in a card game.
 * A hand contains a list of cards dealt to the player.
 */
@Setter
@Getter
public class Hand {

    /**
     * List of cards in the player's hand.
     */
    private List<Card> cards;

    /**
     * Default constructor that initializes the hand with an empty list of cards.
     */
    public Hand(){
        cards = new ArrayList<>();
    }

    /**
     * Constructor that initializes the hand with the specified list of cards.
     *
     * @param cards The list of cards to initialize the hand with.
     */
    public Hand(List<Card> cards){
        this.cards = cards;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add to the hand.
     */
    public void addCard(Card card){
        cards.add(card);
    }

    /**
     * Clears all cards from the player's hand.
     * This effectively resets the hand to be empty.
     */
    public void clear(){
        cards.clear();
    }
}
