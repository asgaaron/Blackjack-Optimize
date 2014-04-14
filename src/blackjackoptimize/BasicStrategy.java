package blackjackoptimize;

public class BasicStrategy extends BlackjackStrategy {
    @Override
    public Action getAction(final Hand hand, final Card dealerFaceUp) {
        switch(hand.value()) {
            case HARD_4:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.SPLIT_OR_HIT;
                    case THREE:
                        return Action.SPLIT_OR_HIT;
                    case FOUR:
                        return Action.SPLIT_OR_HIT;
                    case FIVE:
                        return Action.SPLIT_OR_HIT;
                    case SIX:
                        return Action.SPLIT_OR_HIT;
                    case SEVEN:
                        return Action.SPLIT_OR_HIT;
                    default:
                        return Action.HIT;
                }
            case HARD_5:
                return Action.HIT;
            case HARD_6:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.SPLIT_OR_HIT;
                    case THREE:
                        return Action.SPLIT_OR_HIT;
                    case FOUR:
                        return Action.SPLIT_OR_HIT;
                    case FIVE:
                        return Action.SPLIT_OR_HIT;
                    case SIX:
                        return Action.SPLIT_OR_HIT;
                    case SEVEN:
                        return Action.SPLIT_OR_HIT;
                    default:
                        return Action.HIT;
                }
            case HARD_7:
                return Action.HIT;
            case HARD_8:
                switch(dealerFaceUp.type) {
                    case FIVE:
                        return Action.SPLIT_OR_HIT;
                    case SIX:
                        return Action.SPLIT_OR_HIT;
                    default:
                        return Action.HIT;
                }
            case HARD_9:
                switch(dealerFaceUp.type) {
                    case THREE:
                        return Action.DOUBLE;
                    case FOUR:
                        return Action.DOUBLE;
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case HARD_10:
                switch(dealerFaceUp.type) {
                    case ACE:
                        return Action.HIT;
                    case KING:
                        return Action.HIT;
                    case QUEEN:
                        return Action.HIT;
                    case JACK:
                        return Action.HIT;
                    case TEN:
                        return Action.HIT;
                    default:
                        return Action.DOUBLE;
                }
            case HARD_11:
                switch(dealerFaceUp.type) {
                    case ACE:
                        return Action.HIT;
                    default:
                        return Action.DOUBLE;
                }
            case HARD_12:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.SPLIT_OR_HIT;
                    case THREE:
                        return Action.SPLIT_OR_HIT;
                    case FOUR:
                        return Action.SPLIT_OR_STAND;
                    case FIVE:
                        return Action.SPLIT_OR_STAND;
                    case SIX:
                        return Action.SPLIT_OR_STAND;
                    default:
                        return Action.HIT;
                }
            case HARD_13:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.STAND;
                    case THREE:
                        return Action.STAND;
                    case FOUR:
                        return Action.STAND;
                    case FIVE:
                        return Action.STAND;
                    case SIX:
                        return Action.STAND;
                    default:
                        return Action.HIT;
                }
            case HARD_14:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.SPLIT_OR_STAND;
                    case THREE:
                        return Action.SPLIT_OR_STAND;
                    case FOUR:
                        return Action.SPLIT_OR_STAND;
                    case FIVE:
                        return Action.SPLIT_OR_STAND;
                    case SIX:
                        return Action.SPLIT_OR_STAND;
                    case SEVEN:
                        return Action.SPLIT_OR_HIT;
                    default:
                        return Action.HIT;
                }
            case HARD_15:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.STAND;
                    case THREE:
                        return Action.STAND;
                    case FOUR:
                        return Action.STAND;
                    case FIVE:
                        return Action.STAND;
                    case SIX:
                        return Action.STAND;
                    default:
                        return Action.HIT;
                }
            case HARD_16:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.SPLIT_OR_STAND;
                    case THREE:
                        return Action.SPLIT_OR_STAND;
                    case FOUR:
                        return Action.SPLIT_OR_STAND;
                    case FIVE:
                        return Action.SPLIT_OR_STAND;
                    case SIX:
                        return Action.SPLIT_OR_STAND;
                    default:
                        return Action.SPLIT_OR_HIT;
                }
            case HARD_17:
                return Action.STAND;
            case HARD_18:
                switch(dealerFaceUp.type) {
                    case SEVEN:
                        return Action.STAND;
                    case TEN:
                        return Action.STAND;
                    case JACK:
                        return Action.STAND;
                    case QUEEN:
                        return Action.STAND;
                    case KING:
                        return Action.STAND;
                    case ACE:
                        return Action.STAND;
                    default:
                        return Action.SPLIT_OR_STAND;
                }
            case HARD_19:
                return Action.STAND;
            case HARD_20:
                return Action.STAND;
            case HARD_21:
                return Action.STAND;
            case SOFT_12:
                return Action.SPLIT_OR_HIT;
            case SOFT_13:
                switch(dealerFaceUp.type) {
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case SOFT_14:
                switch(dealerFaceUp.type) {
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case SOFT_15:
                switch(dealerFaceUp.type) {
                    case FOUR:
                        return Action.DOUBLE;
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case SOFT_16:
                switch(dealerFaceUp.type) {
                    case FOUR:
                        return Action.DOUBLE;
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case SOFT_17:
                switch(dealerFaceUp.type) {
                    case THREE:
                        return Action.DOUBLE;
                    case FOUR:
                        return Action.DOUBLE;
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    default:
                        return Action.HIT;
                }
            case SOFT_18:
                switch(dealerFaceUp.type) {
                    case TWO:
                        return Action.STAND;
                    case THREE:
                        return Action.DOUBLE;
                    case FOUR:
                        return Action.DOUBLE;
                    case FIVE:
                        return Action.DOUBLE;
                    case SIX:
                        return Action.DOUBLE;
                    case SEVEN:
                        return Action.STAND;
                    case EIGHT:
                        return Action.STAND;
                    default:
                        return Action.HIT;
                }
            case SOFT_19:
                return Action.STAND;
            case SOFT_20:
                return Action.STAND;
            case SOFT_21:
                return Action.STAND;
            default:
                return Action.STAND;
        }
    }
}
