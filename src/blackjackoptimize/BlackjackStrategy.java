package blackjackoptimize;

public abstract class BlackjackStrategy {
    public static enum Action {
        DOUBLE, HIT, SPLIT_OR_DOUBLE, SPLIT_OR_HIT, SPLIT_OR_STAND, STAND
    }

    private static final Action[] ACTIONS = Action.values();
    private static double ACTIONS_LENGTH = ACTIONS.length;

    public static Action randomAction() {
        return ACTIONS[(int)(Math.random() * ACTIONS_LENGTH)];
    }

    public abstract Action getAction(Hand hand);
}
