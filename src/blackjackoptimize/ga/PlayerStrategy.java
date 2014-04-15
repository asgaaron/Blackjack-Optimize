package blackjackoptimize.ga;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lib.easyjava.ml.optimization.ga.GeneticAlgorithmSolution;
import lib.easyjava.type.Pair;
import blackjackoptimize.BlackjackGame;
import blackjackoptimize.BlackjackStrategy;
import blackjackoptimize.Card;
import blackjackoptimize.Card.Type;
import blackjackoptimize.DealerStrategy;
import blackjackoptimize.Hand;
import blackjackoptimize.Hand.Value;

public class PlayerStrategy extends BlackjackStrategy implements GeneticAlgorithmSolution, Serializable {
    private static final int BET = 100;
    private static final Type[] CARDS = Type.values();
    private static final double CARDS_LENGTH = CARDS.length;
    private static final DealerStrategy DEALER_STRATEGY = new DealerStrategy(true);
    private static final Value[] HANDS = Value.values();
    private static final double HANDS_LENGTH = HANDS.length;
    private static final int NUM_DECKS = 6;
    private static final int NUM_ROUNDS = 10000;
    private static final long serialVersionUID = 3795914867346387162L;
    private long fitness;
    private boolean scored;
    private final Map<Value, Map<Type, Action>> strategy;
    private long totalBet;

    public PlayerStrategy(final Map<Value, Map<Type, Action>> strategy) {
        this.strategy = strategy;
        fitness = 0;
        scored = false;
    }

    @Override
    public GeneticAlgorithmSolution crossover(final GeneticAlgorithmSolution arg0) {
        final PlayerStrategy mate = (PlayerStrategy)arg0;
        final Map<Value, Map<Type, Action>> newStrategy = new HashMap<Value, Map<Type, Action>>();
        for(final Value value : strategy.keySet()) {
            if(value == Value.BUST) {
                final Map<Type, Action> bustStrat = new HashMap<Type, Action>();
                for(final Type type : Type.values()) {
                    bustStrat.put(type, Action.STAND);
                }
                newStrategy.put(value, bustStrat);
                continue;
            }
            final Map<Type, Action> strat = new HashMap<Type, Action>();
            for(final Type type : strategy.get(value).keySet()) {
                if(Math.random() < .5) {
                    strat.put(type, strategy.get(value).get(type));
                }
                else {
                    strat.put(type, mate.strategy.get(value).get(type));
                }
            }
            newStrategy.put(value, strat);
        }
        return new PlayerStrategy(newStrategy);
    }

    @Override
    public GeneticAlgorithmSolution deepCopy() {
        final Map<Value, Map<Type, Action>> newStrategy = new HashMap<Value, Map<Type, Action>>();
        for(final Value value : strategy.keySet()) {
            final Map<Type, Action> strat = new HashMap<Type, Action>();
            for(final Type type : strategy.get(value).keySet()) {
                strat.put(type, strategy.get(value).get(type));
            }
            newStrategy.put(value, strat);
        }
        return new PlayerStrategy(newStrategy);
    }

    @Override
    public long fitness() {
        if(scored) {
            return fitness;
        }
        final BlackjackGame scorer = new BlackjackGame(NUM_DECKS, DEALER_STRATEGY, this);
        final Pair<Integer, Integer> results = scorer.playNHands(NUM_ROUNDS, BET);
        scored = true;
        fitness = results.getLeft();
        totalBet = results.getRight();
        return fitness;
    }

    @Override
    public Action getAction(final Hand hand, final Card dealerFaceUp) {
        return strategy.get(hand.value()).get(dealerFaceUp.type);
    }

    @Override
    public void mutate() {
        Value value;
        do {
            value = HANDS[(int)(Math.random() * HANDS_LENGTH)];
        }
        while(value == Value.BUST);
        final Type faceUp = CARDS[(int)(Math.random() * CARDS_LENGTH)];
        strategy.get(value).put(faceUp, randomAction(value.splittable()));
    }

    public double returnPercentage() {
        fitness();
        return 100.0 + 100.0 * ((double)fitness / (double)totalBet);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(final Value hand : HANDS) {
            for(final Type card : CARDS) {
                sb.append(hand + " against " + card + ": " + strategy.get(hand).get(card) + "\n");
            }
        }
        return sb.toString();
    }
}
