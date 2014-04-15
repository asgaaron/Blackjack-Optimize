package blackjackoptimize;

import java.util.ArrayList;
import java.util.List;

import lib.easyjava.type.Pair;
import blackjackoptimize.BlackjackStrategy.Action;
import blackjackoptimize.Card.Type;
import blackjackoptimize.Hand.Value;

public class BlackjackGame {
    private static final Type[] CARDS = Card.Type.values();
    private final BlackjackStrategy dealer, player;
    private final int numDecks;

    public BlackjackGame(final int numDecks, final BlackjackStrategy dealer, final BlackjackStrategy player) {
        this.numDecks = numDecks;
        this.dealer = dealer;
        this.player = player;
    }

    private List<Card> buildShoe() {
        final List<Card> shoe = new ArrayList<Card>();
        for(final Type type : CARDS) {
            for(int i = 0; i < 4 * numDecks; i++) {
                shoe.add(new Card(type));
            }
        }
        for(int i = shoe.size() - 1; i > 0; i--) {
            final int j = (int)(Math.random() * (i + 1));
            final Card cardI = shoe.get(i);
            shoe.set(i, shoe.get(j));
            shoe.set(j, cardI);
        }
        return shoe;
    }

    public Pair<Integer, Integer> playAHand(final int bet) {
        final List<Card> shoe = buildShoe();
        final Hand dealerHand = new Hand(shoe.remove(0), shoe.remove(0));
        final List<Pair<Hand, Integer>> playerHands = new ArrayList<Pair<Hand, Integer>>();
        playerHands.add(new Pair<Hand, Integer>(new Hand(shoe.remove(0), shoe.remove(0)), bet));
        int i = 0;
        Action action;
        boolean done;
        while(i < playerHands.size()) {
            done = false;
            do {
                final Pair<Hand, Integer> hand = playerHands.get(i);
                action = player.getAction(hand.getLeft().value(), dealerHand.faceUp().type);
                switch(action) {
                    case HIT:
                        hand.getLeft().add(shoe.remove(0));
                        break;
                    case DOUBLE:
                        hand.getLeft().add(shoe.remove(0));
                        hand.setRight(hand.getRight() * 2);
                        done = true;
                        break;
                    case SPLIT_OR_HIT:
                        Hand newHand = splitHand(hand.getLeft(), shoe);
                        if(newHand != null) {
                            playerHands.add(new Pair<Hand, Integer>(newHand, hand.getRight()));
                        }
                        else {
                            hand.getLeft().add(shoe.remove(0));
                        }
                        break;
                    case SPLIT_OR_STAND:
                        newHand = splitHand(hand.getLeft(), shoe);
                        if(newHand != null) {
                            playerHands.add(new Pair<Hand, Integer>(newHand, hand.getRight()));
                        }
                        else {
                            done = true;
                        }
                        break;
                    case SPLIT_OR_DOUBLE:
                        newHand = splitHand(hand.getLeft(), shoe);
                        if(newHand != null) {
                            playerHands.add(new Pair<Hand, Integer>(newHand, hand.getRight()));
                        }
                        else {
                            hand.getLeft().add(shoe.remove(0));
                            hand.setRight(hand.getRight() * 2);
                            done = true;
                        }
                        break;
                    case STAND:
                        done = true;
                        break;
                }
            }
            while(!done);
            i++;
        }
        do {
            action = dealer.getAction(dealerHand.value(), null);
            if(action == Action.HIT) {
                dealerHand.add(shoe.remove(0));
            }
        }
        while(action != Action.STAND);
        int totalBet = 0;
        int totalReturn = 0;
        for(final Pair<Hand, Integer> hand : playerHands) {
            totalBet += hand.getRight();
            if(hand.getLeft().value() == Value.BUST) {
                totalReturn -= hand.getRight();
                continue;
            }
            if(hand.getLeft().isBlackjack()) {
                totalReturn += hand.getRight() * 1.5;
                continue;
            }
            if(dealerHand.value() == Value.BUST) {
                totalReturn += hand.getRight();
                continue;
            }
            final int comp = dealerHand.value().getValue() - hand.getLeft().value().getValue();
            if(comp > 0) {
                totalReturn -= hand.getRight();
            }
            else if(comp < 0) {
                totalReturn += hand.getRight();
            }
        }
        return new Pair<Integer, Integer>(totalReturn, totalBet);
    }

    public Pair<Integer, Integer> playNHands(final int n, final int bet) {
        int totalReturned = 0;
        int totalBet = 0;
        for(int i = 0; i < n; i++) {
            final Pair<Integer, Integer> results = playAHand(bet);
            totalReturned += results.getLeft();
            totalBet += results.getRight();
        }
        return new Pair<Integer, Integer>(totalReturned, totalBet);
    }

    private Hand splitHand(final Hand hand, final List<Card> shoe) {
        final Hand newHand = hand.split();
        if(newHand == null) {
            return null;
        }
        newHand.add(shoe.remove(0));
        hand.add(shoe.remove(0));
        return newHand;
    }
}
