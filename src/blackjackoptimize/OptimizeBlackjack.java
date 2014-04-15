package blackjackoptimize;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lib.easyjava.io.file.FileReader;
import lib.easyjava.io.file.FileWriter;
import lib.easyjava.ml.optimization.ga.GeneticAlgorithmOptimizer;
import blackjackoptimize.ga.PlayerStrategy;
import blackjackoptimize.ga.PlayerStrategyFactory;

public class OptimizeBlackjack {
    private static final boolean LOAD_FROM_FILE = true;
    private static final int NUM_GENERATIONS = 100000;

    private static void logGeneration(final List<PlayerStrategy> generation) {
        final StringBuilder sb = new StringBuilder();
        for(final PlayerStrategy sol : generation) {
            sb.append("Return Rate: " + sol.returnPercentage() + "% | Score: " + sol.fitness() + "\n");
            sb.append(sol);
            sb.append("-----------------------------------------------\n");
        }
        Serializable gen;
        try {
            gen = (Serializable)generation;
        }
        catch(final ClassCastException e) {
            gen = new LinkedList<PlayerStrategy>(generation);
        }
        try {
            FileWriter.writeFile("generation.stats", sb.toString());
            FileWriter.writeSerializedFile("generation", gen);
        }
        catch(final IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(final String[] args) throws ClassNotFoundException, IOException {
        final GeneticAlgorithmOptimizer<PlayerStrategy> optimizer;
        if(LOAD_FROM_FILE) {
            optimizer = new GeneticAlgorithmOptimizer<PlayerStrategy>(new ArrayList<PlayerStrategy>(
                    (List<PlayerStrategy>)FileReader.readSerializedFile("generation")));
        }
        else {
            optimizer = new GeneticAlgorithmOptimizer<PlayerStrategy>(40, .9, .8, .2, new PlayerStrategyFactory());
        }
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
