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

    public Pair<Integer, Integer> playAHand(int bet) {
        final List<Card> shoe = buildShoe();
        final Hand dealerHand = new Hand(shoe.remove(0), shoe.remove(0));
        final List<Pair<Hand, Integer>> playerHands = new ArrayList<Pair<Hand, Integer>>();
        playerHands.add(new Pair<Hand, Integer>(new Hand(shoe.remove(0), shoe.remove(0)), bet));
        int i = 0;
        Action action;
        boolean done;
        while(i < playerHands.size()) {
            do {
                done = false;
                final Pair<Hand, Integer> hand = playerHands.get(i);
                action = player.getAction(hand.getLeft());
                switch(action) {
                    case HIT:
                        hand.getLeft().add(shoe.remove(0));
                    case DOUBLE:
                        hand.getLeft().add(shoe.remove(0));
                        hand.setRight(hand.getRight() * 2);
                        bet += hand.getRight();
                        done = true;
                    case SPLIT_OR_HIT:
                        Hand newHand = splitHand(hand.getLeft(), shoe);
                        if(newHand != null) {
                            playerHands.add(new Pair<Hand, Integer>(newHand, hand.getRight()));
                            bet += hand.getRight();
                        }
                        else {
                            hand.getLeft().add(shoe.remove(0));
                        }
                    case SPLIT_OR_STAND:
                        newHand = splitHand(hand.getLeft(), shoe);
                        if(newHand != null) {
                            playerHands.add(new Pair<Hand, Integer>(newHand, hand.getRight()));
                            bet += hand.getRight();
                        }
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
                        bet += hand.getRight();
                    case STAND:
                        done = true;
                }
            }
            while(!done);
            i++;
        }
        do {
            action = dealer.getAction(dealerHand);
            if(action == Action.HIT) {
                dealerHand.add(shoe.remove(0));
            }
        }
        while(action != Action.STAND);
        int total = 0;
        for(final Pair<Hand, Integer> hand : playerHands) {
            if(hand.getLeft().value() == Value.BUST) {
                total -= hand.getRight();
                continue;
            }
            if(hand.getLeft().isBlackjack()) {
                total += hand.getRight() * 1.5;
                continue;
            }
            if(dealerHand.value() == Value.BUST) {
                total += hand.getRight();
                continue;
            }
            final int comp = dealerHand.value().getValue() - hand.getLeft().value().getValue();
            if(comp > 0) {
                total -= hand.getRight();
            }
            else if(comp < 0) {
                total += hand.getRight();
            }
        }
        
        return new Pair<Integer, Integer>(total, bet);
    }

    public Pair<Integer, Integer> playNHands(final int n, final int bet) {
        int totalReturned = 0;
        int totalBet = 0;
        for(int i = 0; i < n; i++) {
            Pair<Integer, Integer> results = playAHand(bet);
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
