package blackjackoptimize;

public class DealerStrategy extends BlackjackStrategy {
    private final boolean hitSoft17;

    public DealerStrategy(final boolean hitSoft17) {
        this.hitSoft17 = hitSoft17;
    }

    @Override
    public Action getAction(final Hand hand, final Card dealerFaceUp) {
        switch(hand.value()) {
            case BUST:
                return Action.STAND;
            case SOFT_18:
                return Action.STAND;
            case SOFT_19:
                return Action.STAND;
            case SOFT_20:
                return Action.STAND;
            case SOFT_21:
                return Action.STAND;
            case HARD_17:
                return Action.STAND;
            case HARD_18:
                return Action.STAND;
            case HARD_19:
                return Action.STAND;
            case HARD_20:
                return Action.STAND;
            case HARD_21:
                return Action.STAND;
            case SOFT_17:
                if(hitSoft17) {
                    return Action.HIT;
                }
                else {
                    return Action.STAND;
                }
            default:
                return Action.HIT;
        }
    }
}
