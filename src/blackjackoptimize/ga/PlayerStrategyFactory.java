package blackjackoptimize.ga;

import java.util.HashMap;
import java.util.Map;

import lib.easyjava.ml.optimization.ga.AbstractSolutionFactory;
import blackjackoptimize.BlackjackStrategy;
import blackjackoptimize.BlackjackStrategy.Action;
import blackjackoptimize.Hand.Value;

public class PlayerStrategyFactory extends AbstractSolutionFactory<PlayerStrategy> {
    @Override
    public PlayerStrategy randomSolution() {
        final Map<Value, Action> strategy = new HashMap<Value, Action>();
        for(final Value hand : Value.values()) {
            strategy.put(hand, BlackjackStrategy.randomAction(hand.splittable()));
        }
        strategy.put(Value.BUST, Action.STAND);
        return new PlayerStrategy(strategy);
    }
}
