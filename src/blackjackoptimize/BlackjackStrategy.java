package blackjackoptimize;

import java.util.EnumSet;

public abstract class BlackjackStrategy {
    public static enum Action {
        DOUBLE, HIT, SPLIT_OR_DOUBLE, SPLIT_OR_HIT, SPLIT_OR_STAND, STAND
    }
    
    private static final Action[] ACTIONS = Action.values();
    private static double ACTIONS_LENGTH = ACTIONS.length;
    
    private static final Action[] NON_SPLIT_ACTIONS = EnumSet.of(Action.DOUBLE, Action.HIT, Action.STAND).toArray(new Action[(int)(ACTIONS_LENGTH - 3)]);
    private static double NON_SPLIT_ACTIONS_LENGTH = ACTIONS.length - 3;

    public static Action randomAction(boolean canSplit) {
        if(canSplit) {
            return ACTIONS[(int)(Math.random() * ACTIONS_LENGTH)];
        } else {
            return NON_SPLIT_ACTIONS[(int)(Math.random() * NON_SPLIT_ACTIONS_LENGTH)];
        }
    }

    public abstract Action getAction(Hand hand);
}
