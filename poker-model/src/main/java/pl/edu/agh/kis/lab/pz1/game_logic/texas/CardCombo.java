package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import pl.edu.agh.kis.lab.pz1.game_exceptions.IncorrectCardComboException;

import java.util.List;
import java.util.Map;

/**
 * Represents a combination of cards with a specific type and tie-breaker cards.
 * This class is immutable and implements {@link Comparable} to allow comparison based on combo type and tie-breakers.
 *
 * @param comboType       the type of card combination (e.g., FULL_HOUSE, STRAIGHT, etc.)
 * @param cards           the list of cards forming the combination
 * @param tieBreakerCards the list of cards used to break ties between combinations of the same type
 */
public record CardCombo(ComboType comboType, List<Card> cards,
                        List<Card> tieBreakerCards) implements Comparable<CardCombo> {

    /**
     * A map defining the number of tie-breaker cards required for each combo type.
     */
    static Map<ComboType, Integer> tiebreakerNumbers;

    static {
        tiebreakerNumbers = Map.ofEntries(
                Map.entry(ComboType.HIGH_CARD, 5),
                Map.entry(ComboType.PAIR, 4),
                Map.entry(ComboType.TWO_PAIRS, 3),
                Map.entry(ComboType.THREE_OF_A_KIND, 3),
                Map.entry(ComboType.STRAIGHT, 1),
                Map.entry(ComboType.FLUSH, 5),
                Map.entry(ComboType.FULL_HOUSE, 2),
                Map.entry(ComboType.FOUR_OF_A_KIND, 2),
                Map.entry(ComboType.STRAIGHT_FLUSH, 1)
        );
    }

    /**
     * Constructs a new {@code CardCombo} instance.
     *
     * @throws IncorrectCardComboException if the number of cards or tie-breaker cards does not match the expected values for the combo type
     */
    public CardCombo {
        if (cards.size() != 5 || tieBreakerCards.size() != tiebreakerNumbers.get(comboType)) {
            throw new IncorrectCardComboException("Card combo doesn't have correct number of tiebreakers");
        }
    }

    /**
     * Compares this card combination with another based on combo type and tie-breaker cards.
     *
     * @param o the other card combination to compare to
     * @return a positive integer if this combination is greater, a negative integer if it is less, or zero if they are equal
     */
    @Override
    public int compareTo(CardCombo o) {
        if (this.comboType.ordinal() > o.comboType.ordinal()) {
            return 1;
        } else if (this.comboType.ordinal() < o.comboType.ordinal()) {
            return -1;
        } else {
            for (int i = 0; i < this.tieBreakerCards.size(); ++i) {
                if (this.tieBreakerCards.get(i).rank().ordinal() > o.tieBreakerCards.get(i).rank().ordinal()) {
                    return 1;
                } else if (this.tieBreakerCards.get(i).rank().ordinal() < o.tieBreakerCards.get(i).rank().ordinal()) {
                    return -1;
                }
            }
            return 0;
        }
    }
}
