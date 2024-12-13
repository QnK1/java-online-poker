package pl.edu.agh.kis.lab.pz1.game_logic;


import pl.edu.agh.kis.lab.pz1.game_exceptions.IncorrectCardComboException;

import java.util.List;
import java.util.Map;

public record CardCombo(ComboType comboType, List<Card> cards,
                        List<Card> tieBreakerCards) implements Comparable<CardCombo> {
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

    public CardCombo {
        if (cards.size() != 5 || tieBreakerCards.size() != tiebreakerNumbers.get(comboType)) {
            throw new IncorrectCardComboException("Card combo doesn't have correct number of tiebreakers");
        }

    }

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
