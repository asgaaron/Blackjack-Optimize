package blackjackoptimize;

import java.util.LinkedList;
import java.util.List;

public class Hand {
    public static enum Value {
        BUST(-1), HARD_10(10), HARD_11(11), HARD_12(12), HARD_13(13), HARD_14(14), HARD_15(15), HARD_16(16), HARD_17(17), HARD_18(18), HARD_19(19), HARD_20(20), HARD_21(
                21), HARD_4(4), HARD_5(5), HARD_6(6), HARD_7(7), HARD_8(8), HARD_9(9), SOFT_12(12), SOFT_13(13), SOFT_14(14), SOFT_15(15), SOFT_16(16), SOFT_17(
                17), SOFT_18(18), SOFT_19(19), SOFT_20(20), SOFT_21(21);
        private final int value;

        private Value(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
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
