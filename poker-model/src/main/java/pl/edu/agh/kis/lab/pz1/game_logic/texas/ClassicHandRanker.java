package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import org.javatuples.Pair;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;

import java.util.*;

/**
 * Implements the HandRanker interface to determine the winner(s) of a Texas Hold'em poker game
 * based on players' hands and community cards. Also includes methods for identifying specific card combinations.
 */
public class ClassicHandRanker implements HandRanker {

    /**
     * Determines the winner(s) of the game by evaluating the players' hands combined with the community cards.
     *
     * @param players         the list of players in the game
     * @param communityCards  the community cards visible on the table
     * @return a list of players who have the best hand
     */
    @Override
    public List<THPlayer> pickWinner(List<THPlayer> players, CommunityCards communityCards) {
        var playerCards = players.stream().map(p -> {
                    List<Card> cards = new ArrayList<>();
                    cards.addAll(p.getHand().getCards());
                    cards.addAll(communityCards.getVisibleCards());

                    return new Pair<>(p, cards);
                }
        ).toList();

        var playerCombos = new ArrayList<>(playerCards.stream().map(pl -> new Pair<>(
                pl.getValue0(),
                findComboFromHand(pl.getValue1())
        )).toList());

        playerCombos.sort((pc1, pc2) -> pc2.getValue1().compareTo(pc1.getValue1()));

        List<THPlayer> winners = new ArrayList<>();
        winners.add(playerCombos.get(0).getValue0());

        for (var pc : playerCombos.subList(1, playerCombos.size())) {
            if (pc.getValue1().compareTo(playerCombos.get(0).getValue1()) == 0) {
                winners.add(pc.getValue0());
            }
        }

        return winners;
    }

    /**
     * Identifies the best card combination (hand) from a given list of cards.
     *
     * @param cards the list of cards to evaluate
     * @return the best card combination found
     */
    public CardCombo findComboFromHand(List<Card> cards) {
        CardCombo combo = findStraightFlush(cards);

        if (combo == null) {
            combo = findFourOfAKind(cards);
        }

        if (combo == null) {
            combo = findFullHouse(cards);
        }

        if (combo == null) {
            combo = findFlush(cards);
        }

        if (combo == null) {
            combo = findStraight(cards);
        }

        if (combo == null) {
            combo = findThreeOfAKind(cards);
        }

        if (combo == null) {
            combo = findTwoPairs(cards);
        }

        if (combo == null) {
            combo = findPair(cards);
        }

        if (combo == null) {
            combo = findHighCard(cards);
        }

        return combo;
    }

    /**
     * Finds a straight flush combination from a given list of cards.
     *
     * @param cards the list of cards to evaluate
     * @return a {@code CardCombo} representing a straight flush, or null if none is found
     */
    private CardCombo findStraightFlush(List<Card> cards) {
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Suit, Integer> suitCounts = new EnumMap<>(Card.Suit.class);
        Card.Suit topSuit = null;
        List<Card> cardsFound = new ArrayList<>();

        for (Card card : sortedCards) {
            suitCounts.put(card.suit(), suitCounts.getOrDefault(card.suit(), 0) + 1);

            if (suitCounts.get(card.suit()) >= 5) {
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

    /**
     * Finds a four-of-a-kind combination from a given list of cards.
     *
     * @param cards the list of cards to evaluate
     * @return a {@code CardCombo} representing four of a kind, or null if none is found
     */
    private CardCombo findFourOfAKind(List<Card> cards) {
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Rank, List<Card>> rankCards = new EnumMap<>(Card.Rank.class);
        List<Card> bestCards = new ArrayList<>();

        for (var card : sortedCards) {
            rankCards.putIfAbsent(card.rank(), new ArrayList<>());
            rankCards.get(card.rank()).add(card);

            if (rankCards.get(card.rank()).size() == 4) {
                bestCards.add(card);
            }
        }

        List<Card> hand = new ArrayList<>();

        if (!bestCards.isEmpty()) {
            for (var card : sortedCards) {
                if (card.rank() != bestCards.get(0).rank()) {
                    bestCards.add(card);

                    hand.addAll(rankCards.get(bestCards.get(0).rank()));
                    hand.add(bestCards.get(1));

                    break;
                }
            }
        }

        return bestCards.isEmpty() ? null : new CardCombo(
                ComboType.FOUR_OF_A_KIND,
                new ArrayList<>(hand),
                new ArrayList<>(bestCards)
        );

    }

    /**
     * Finds a full house combination from a given list of cards.
     *
     * @param cards the list of cards to evaluate
     * @return a {@code CardCombo} representing a full house, or null if none is found
     */
    private CardCombo findFullHouse(List<Card> cards) {
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Rank, List<Card>> rankCards = new EnumMap<>(Card.Rank.class);
        Card.Rank threeRank = null;
        Card.Rank twoRank = null;

        for (var card : sortedCards) {
            rankCards.putIfAbsent(card.rank(), new ArrayList<>());
            rankCards.get(card.rank()).add(card);

            if (threeRank == null && rankCards.get(card.rank()).size() == 3) {
                threeRank = card.rank();
            } else if (threeRank != null && twoRank == null && rankCards.get(card.rank()).size() == 2) {
                twoRank = card.rank();
            }
        }

        List<Card> hand = new ArrayList<>();
        List<Card> tiebreakers = new ArrayList<>();

        if (twoRank != null) {
            hand.addAll(rankCards.get(threeRank).subList(0, 3));
            hand.addAll(rankCards.get(twoRank).subList(0, 2));

            tiebreakers.add(rankCards.get(threeRank).get(0));
            tiebreakers.add(rankCards.get(twoRank).get(0));
        }

        return twoRank != null ? new CardCombo(
                ComboType.FULL_HOUSE,
                new ArrayList<>(hand),
                new ArrayList<>(tiebreakers)
        ) : null;
    }

    /**
     * Finds a flush combination from a given list of cards.
     *
     * @param cards the list of cards to evaluate
     * @return a {@code CardCombo} representing a flush, or null if none is found
     */
    private CardCombo findFlush(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Suit, List<Card>> suitCards = new EnumMap<>(Card.Suit.class);

        for(var card : sortedCards){
            suitCards.putIfAbsent(card.suit(), new ArrayList<>());
            suitCards.get(card.suit()).add(card);
        }

        List<Card> hand = new ArrayList<>();

        for(Card.Suit suit : Card.Suit.values()){
            List<Card> cardList = suitCards.get(suit) == null ? new ArrayList<>() : suitCards.get(suit);

            if(cardList.size() >= 5){
                cardList = cardList.stream().sorted(Comparator.reverseOrder()).toList();
                cardList = cardList.subList(0, 5);
                hand.addAll(cardList);
            }
        }

        return hand.size() == 5 ? new CardCombo(
                ComboType.FLUSH,
                new ArrayList<>(hand),
                new ArrayList<>(hand)
        ) : null;
    }

    /**
     * Finds a straight in the given list of cards.
     * A straight is a hand where the cards have consecutive ranks.
     *
     * @param cards The list of cards to evaluate.
     * @return A CardCombo representing the straight, or null if no straight is found.
     */
    private CardCombo findStraight(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        List<Card> hand = new ArrayList<>();

        for(var card : sortedCards){
            if(hand.isEmpty() ||
                    hand.get(hand.size() - 1).rank().ordinal() - card.rank().ordinal() != 1){

                hand.clear();
                hand.add(card);
            }
            else{
                hand.add(card);
            }

            if(hand.size() == 5){
                break;
            }
        }

        return hand.size() == 5 ? new CardCombo(
                ComboType.STRAIGHT,
                new ArrayList<>(hand),
                new ArrayList<>(hand.subList(0, 1))
        ) : null;
    }

    /**
     * Finds a three of a kind in the given list of cards.
     * A three of a kind is a hand where three cards have the same rank.
     *
     * @param cards The list of cards to evaluate.
     * @return A CardCombo representing the three of a kind, or null if no three of a kind is found.
     */
    private CardCombo findThreeOfAKind(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Rank, List<Card>> rankCards = new EnumMap<>(Card.Rank.class);
        List<Card> hand = new ArrayList<>();
        List<Card> tiebreakers = new ArrayList<>();

        for(var card : sortedCards){
            rankCards.putIfAbsent(card.rank(), new ArrayList<>());
            rankCards.get(card.rank()).add(card);

            if(rankCards.get(card.rank()).size() == 3){
                tiebreakers.add(rankCards.get(card.rank()).get(0));
                hand.addAll(rankCards.get(card.rank()).subList(0, 3));
                int tCount = 0;
                for(var c : sortedCards){
                    if(tCount == 2){
                        break;
                    }

                    if(c.rank() != card.rank()){
                        ++tCount;
                        tiebreakers.add(c);
                        hand.add(c);
                    }
                }
                break;
            }
        }

        return hand.size() == 5 ? new CardCombo(
                ComboType.THREE_OF_A_KIND,
                new ArrayList<>(hand),
                new ArrayList<>(tiebreakers)
        ) : null;

    }

    /**
     * Finds two pairs in the given list of cards.
     * Two pairs are hands where two separate ranks each have two cards.
     *
     * @param cards The list of cards to evaluate.
     * @return A CardCombo representing the two pairs, or null if no two pairs are found.
     */
    private CardCombo findTwoPairs(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Rank, List<Card>> rankCards = new EnumMap<>(Card.Rank.class);

        List<Card> hand = new ArrayList<>();
        List<Card> tiebreakers = new ArrayList<>();
        List<Card.Rank> pairRanks = new ArrayList<>();

        for(var card : sortedCards){
            rankCards.putIfAbsent(card.rank(), new ArrayList<>());
            rankCards.get(card.rank()).add(card);
            if(rankCards.get(card.rank()).size() == 2){
                pairRanks.add(card.rank());

                if(pairRanks.size() == 2){
                    break;
                }
            }
        }
        hand.addAll(sortedCards);
        for(var card : sortedCards){
            if(!pairRanks.contains(card.rank())){
                hand.remove(card);
            }
        }

        for(var card : sortedCards){
            if(!pairRanks.contains(card.rank())){
                hand.add(card);

                break;
            }
        }

        if(pairRanks.size() == 2){
            tiebreakers.add(hand.get(0));
            tiebreakers.add(hand.get(2));
            tiebreakers.add(hand.get(4));
        }

        return pairRanks.size() == 2 ? new CardCombo(
                ComboType.TWO_PAIRS,
                new ArrayList<>(hand),
                new ArrayList<>(tiebreakers)
        ) : null;
    }

    /**
     * Finds a pair in the given list of cards.
     * A pair is a hand where two cards have the same rank.
     *
     * @param cards The list of cards to evaluate.
     * @return A CardCombo representing the pair, or null if no pair is found.
     */
    private CardCombo findPair(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        Map<Card.Rank, List<Card>> rankCards = new EnumMap<>(Card.Rank.class);
        List<Card> hand = new ArrayList<>();
        List<Card> tiebreakers = new ArrayList<>();

        for(var card : sortedCards){
            rankCards.putIfAbsent(card.rank(), new ArrayList<>());
            rankCards.get(card.rank()).add(card);
            if(rankCards.get(card.rank()).size() == 2){
                tiebreakers.add(rankCards.get(card.rank()).get(0));
                hand.addAll(rankCards.get(card.rank()));

                break;
            }
        }

        if(tiebreakers.size() == 1){
            for(var card : sortedCards){
                if(card.rank() != tiebreakers.get(0).rank()){
                    hand.add(card);
                    tiebreakers.add(card);
                }

                if(hand.size() == 5){
                    break;
                }
            }
        }

        return hand.size() == 5 ? new CardCombo(
                ComboType.PAIR,
                new ArrayList<>(hand),
                new ArrayList<>(tiebreakers)
        ) : null;


    }

    /**
     * Finds the highest card in the given list of cards.
     * The highest card is the card with the highest rank in the hand.
     *
     * @param cards The list of cards to evaluate.
     * @return A CardCombo representing the high card hand.
     */
    private CardCombo findHighCard(List<Card> cards){
        List<Card> sortedCards = cards.stream().sorted(Comparator.reverseOrder()).toList();
        List<Card> hand = new ArrayList<>();

        for(var card : sortedCards){
            hand.add(card);

            if(hand.size() == 5){
                break;
            }
        }

        return new CardCombo(
                ComboType.HIGH_CARD,
                new ArrayList<>(hand),
                new ArrayList<>(hand)
        );
    }
}