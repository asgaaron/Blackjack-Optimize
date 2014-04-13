package blackjackoptimize;

public class Card {
    public static enum Type {
        ACE, EIGHT, FIVE, FOUR, JACK, KING, NINE, QUEEN, SEVEN, SIX, TEN, THREE, TWO
    }

    public Type type;

    public Card(final Type type) {
        this.type = type;
    }

    public int value(final boolean soft) {
        switch(type) {
            case ACE:
                if(soft) {
                    return 11;
                }
                else {
                    return 1;
                }
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            default:
                return 10;
        }
    }
}