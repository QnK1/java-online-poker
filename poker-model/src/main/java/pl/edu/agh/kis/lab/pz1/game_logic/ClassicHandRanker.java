package pl.edu.agh.kis.lab.pz1.game_logic;

import java.util.*;

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
        Map<Card.Suit, Integer> suitCounts = new EnumMap<>(Card.Suit.class);
        Card.Suit topSuit = null;
        List<Card> cardsFound = new ArrayList<>();


        for(Card card : sortedCards){
            suitCounts.put(card.suit(), suitCounts.getOrDefault(card.suit(), 0) + 1);

            if(suitCounts.get(card.suit()) >= 5){
                topSuit = card.suit();
            }
        }

        for (Card card : sortedCards) {
            if (card.suit() == topSuit) {
                if (cardsFound.isEmpty() ||
                        cardsFound.get(cardsFound.size() - 1).rank().ordinal() - card.rank().ordinal() != 1) {
                    cardsFound.clear();
                    cardsFound.add(card);
                } else {
                    cardsFound.add(card);
                }

                if (cardsFound.size() == 5) {
                    break;
                }
            }
        }

        return cardsFound.size() == 5 ? new CardCombo(
                ComboType.STRAIGHT_FLUSH,
                new ArrayList<>(cardsFound),
                new ArrayList<>(cardsFound.subList(0, 1))
        ) : null;
    }

    public CardCombo findFourOfAKind(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card, Integer> cardCounts = new HashMap<>();
    }
}