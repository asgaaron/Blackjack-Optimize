package blackjackoptimize;

import java.io.IOException;
import java.util.List;

import lib.easyjava.io.file.FileWriter;
import lib.easyjava.ml.optimization.ga.GeneticAlgorithmOptimizer;
import blackjackoptimize.ga.PlayerStrategy;
import blackjackoptimize.ga.PlayerStrategyFactory;

public class OptimizeBlackjack {
    private static final int NUM_GENERATIONS = 100;

    private static void logGeneration(final List<PlayerStrategy> generation) {
        final StringBuilder sb = new StringBuilder();
        for(final PlayerStrategy sol : generation) {
            sb.append("Return Rate: " + sol.returnPercentage() + "% | Score: " + sol.fitness() + "\n");
            sb.append(sol);
            sb.append("-----------------------------------------------\n");
        }
        try {
            FileWriter.writeFile("generation.txt", sb.toString());
        }
        catch(final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final GeneticAlgorithmOptimizer<PlayerStrategy> optimizer = new GeneticAlgorithmOptimizer<PlayerStrategy>(40, .25, .6, .2, new PlayerStrategyFactory());
        for(int i = 1; i < NUM_GENERATIONS; i++) {
            final List<PlayerStrategy> generation = optimizer.population();
            System.out.println("BEST SCORE FOR GENERATION " + i + ": " + generation.get(0).fitness());
            logGeneration(generation);
            optimizer.nextGeneration();
        }
        final List<PlayerStrategy> generation = optimizer.population();
        System.out.println("BEST SCORE FOR GENERATION " + NUM_GENERATIONS + ": " + generation.get(0).fitness());
        logGeneration(generation);
    }
}
