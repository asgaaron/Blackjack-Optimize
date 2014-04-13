package blackjackoptimize.ga;

import java.util.HashMap;
import java.util.Map;

import lib.easyjava.ml.optimization.ga.GeneticAlgorithmSolution;
import lib.easyjava.type.Pair;
import blackjackoptimize.BlackjackGame;
import blackjackoptimize.BlackjackStrategy;
import blackjackoptimize.DealerStrategy;
import blackjackoptimize.Hand;
import blackjackoptimize.Hand.Value;

public class PlayerStrategy extends BlackjackStrategy implements GeneticAlgorithmSolution {
    private static final int BET = 2;
    private static final DealerStrategy DEALER_STRATEGY = new DealerStrategy(true);
    private static final Value[] HANDS = Value.values();
    private static final double HANDS_LENGTH = HANDS.length;
    private static final int NUM_DECKS = 4;
    private static final int NUM_ROUNDS = 1000;
    
    private long fitness;
    private long totalBet;
    private boolean scored;
    private final Map<Value, Action> strategy;

    public PlayerStrategy(final Map<Value, Action> strategy) {
        this.strategy = strategy;
        fitness = 0;
        scored = false;
    }

    @Override
    public GeneticAlgorithmSolution crossover(final GeneticAlgorithmSolution arg0) {
        final PlayerStrategy mate = (PlayerStrategy)arg0;
        final Map<Value, Action> newStrategy = new HashMap<Value, Action>();
        for(final Value value : strategy.keySet()) {
            if(value == Value.BUST) {
                newStrategy.put(value, Action.STAND);
                continue;
            }
            if(Math.random() < .5) {
                newStrategy.put(value, strategy.get(value));
            }
            else {
                newStrategy.put(value, mate.strategy.get(value));
            }
        }
        return new PlayerStrategy(newStrategy);
    }

    @Override
    public GeneticAlgorithmSolution deepCopy() {
        final Map<Value, Action> newStrategy = new HashMap<Value, Action>();
        for(final Value value : strategy.keySet()) {
            newStrategy.put(value, strategy.get(value));
        }
        return new PlayerStrategy(newStrategy);
    }

    @Override
    public long fitness() {
        if(scored) {
            return fitness;
        }
        final BlackjackGame scorer = new BlackjackGame(NUM_DECKS, DEALER_STRATEGY, this);
        Pair<Integer, Integer> results = scorer.playNHands(NUM_ROUNDS, BET);
        scored = true;
        fitness = results.getLeft();
        totalBet = results.getRight();
        return fitness;
    }

    @Override
    public Action getAction(final Hand hand) {
        return strategy.get(hand.value());
    }

    @Override
    public void mutate() {
        Value value;
        do {
            value = HANDS[(int)(Math.random() * HANDS_LENGTH)];
        }
        while(value == Value.BUST);
        
        strategy.put(value, randomAction(value.splittable()));
    }

    public double returnPercentage() {
        fitness();
        return 100.0 + 100.0 * ((double)fitness / (double)totalBet);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(final Value hand : HANDS) {
            sb.append(hand + ": " + strategy.get(hand) + "\n");
        }
        return sb.toString();
    }
}
