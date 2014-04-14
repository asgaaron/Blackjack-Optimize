package blackjackoptimize.ga;

import java.util.HashMap;
import java.util.Map;

import lib.easyjava.ml.optimization.ga.AbstractSolutionFactory;
import blackjackoptimize.BlackjackStrategy;
import blackjackoptimize.BlackjackStrategy.Action;
import blackjackoptimize.Card.Type;
import blackjackoptimize.Hand.Value;

public class PlayerStrategyFactory extends AbstractSolutionFactory<PlayerStrategy> {
    @Override
    public PlayerStrategy randomSolution() {
        final Map<Value, Map<Type, Action>> strategy = new HashMap<Value, Map<Type, Action>>();
        for(final Value hand : Value.values()) {
            final Map<Type, Action> strat = new HashMap<Type, Action>();
            for(final Type type : Type.values()) {
                strat.put(type, BlackjackStrategy.randomAction(hand.splittable()));
            }
            strategy.put(hand, strat);
        }
        final Map<Type, Action> bustStrat = new HashMap<Type, Action>();
        for(final Type type : Type.values()) {
            bustStrat.put(type, Action.STAND);
        }
        strategy.put(Value.BUST, bustStrat);
        return new PlayerStrategy(strategy);
    }
}
