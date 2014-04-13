package blackjackoptimize;

import java.util.LinkedList;
import java.util.List;

public class Hand {
    public static enum Value {
        BUST(-1, false), HARD_10(10, true), HARD_11(11, false), HARD_12(12, true), HARD_13(13, false), HARD_14(14, true), HARD_15(15, false), HARD_16(16, true), HARD_17(17, false), HARD_18(18, true), HARD_19(19, false), HARD_20(20, true), HARD_21(
                21, false), HARD_4(4, true), HARD_5(5, false), HARD_6(6, true), HARD_7(7, false), HARD_8(8, true), HARD_9(9, false), SOFT_12(12, true), SOFT_13(13, false), SOFT_14(14, false), SOFT_15(15, false), SOFT_16(16, false), SOFT_17(
                17, false), SOFT_18(18, false), SOFT_19(19, false), SOFT_20(20, false), SOFT_21(21, false);
        private final int value;
        private final boolean splittable;

        private Value(final int value, final boolean splittable) {
            this.value = value;
            this.splittable = splittable;
        }

        public int getValue() {
            return value;
        }
        
        public boolean splittable() {
           return splittable;
        }
    }

    public List<Card> cards;

    private Hand(final Card card) {
        cards = new LinkedList<Card>();
        cards.add(card);
    }

    public Hand(final Card card0, final Card card1) {
        cards = new LinkedList<Card>();
        cards.add(card0);
        cards.add(card1);
    }

    public void add(final Card card) {
        cards.add(card);
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && value().value == 21;
    }
    
    public Hand split() {
        if(cards.size() != 2 || cards.get(0).type != cards.get(1).type) {
            return null;
        }
        return new Hand(cards.remove(0));
    }

    public Value value() {
        int softTotal = 0;
        int hardTotal = 0;
        for(final Card card : cards) {
            softTotal += card.value(true);
            hardTotal += card.value(false);
        }
        while(softTotal > 21 && hardTotal != softTotal) {
            softTotal -= 10;
        }
        if(hardTotal != softTotal) {
            switch(softTotal) {
                case 12:
                    return Value.SOFT_12;
                case 13:
                    return Value.SOFT_13;
                case 14:
                    return Value.SOFT_14;
                case 15:
                    return Value.SOFT_15;
                case 16:
                    return Value.SOFT_16;
                case 17:
                    return Value.SOFT_17;
                case 18:
                    return Value.SOFT_18;
                case 19:
                    return Value.SOFT_19;
                case 20:
                    return Value.SOFT_20;
                case 21:
                    return Value.SOFT_21;
                default:
                    return Value.BUST;
            }
        }
        else {
            switch(hardTotal) {
                case 4:
                    return Value.HARD_4;
                case 5:
                    return Value.HARD_5;
                case 6:
                    return Value.HARD_6;
                case 7:
                    return Value.HARD_7;
                case 8:
                    return Value.HARD_8;
                case 9:
                    return Value.HARD_9;
                case 10:
                    return Value.HARD_10;
                case 11:
                    return Value.HARD_11;
                case 12:
                    return Value.HARD_12;
                case 13:
                    return Value.HARD_13;
                case 14:
                    return Value.HARD_14;
                case 15:
                    return Value.HARD_15;
                case 16:
                    return Value.HARD_16;
                case 17:
                    return Value.HARD_17;
                case 18:
                    return Value.HARD_18;
                case 19:
                    return Value.HARD_19;
                case 20:
                    return Value.HARD_20;
                case 21:
                    return Value.HARD_21;
                default:
                    return Value.BUST;
            }
        }
    }
}
