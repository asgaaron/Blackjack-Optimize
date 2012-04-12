package blackjackoptimizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        int popSize = 50;
        double parentPct = .2;
        double crossoverPct = .8;
        double mutationPct = .5;
        double mutationDecayRate = .05;
        int numDecks = 6;
        int numHands = 10000;
        int betAmount = 10000;
        int numIterations = 100000;
        boolean includeBettingStrategy = true;

        ArrayList<PlayerStrategy> population = new ArrayList<PlayerStrategy>();
        CardCounter counter = new CardCounter(numDecks);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Load from file? Enter name or nothing to start anew.");
        String fileName = input.readLine();
        if(!fileName.equals(""))
        {
            File inFile = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            Double converter = new Double(1);
            popSize = (int)converter.parseDouble(reader.readLine().substring(17));
            for(int i = 0; i < popSize; i++)
            {
                int score = (int)converter.parseDouble(reader.readLine().substring(7));
                reader.readLine();
                double[] baseHitPct = new double[26];
                double[] countMultiplier = new double[26];
                for(int i2 = 0; i2 < 26; i2++)
                {
                    String line = reader.readLine();
                    int startPoint = 0;
                    while(!line.substring(startPoint, startPoint + 3).equals("%: "))
                        startPoint++;
                    int stopPoint = startPoint + 3;
                    while(line.charAt(stopPoint) != ' ')
                        stopPoint++;
                    baseHitPct[i2] = converter.parseDouble(line.substring(startPoint + 3, stopPoint)) / 100;
                    countMultiplier[i2] = converter.parseDouble(line.substring(stopPoint + 19)) / 100;
                }
                population.add(new PlayerStrategy(baseHitPct, countMultiplier));
                population.get(i).score = score;
            }
        }
        else
        {
            for(int i = 0; i < popSize; i++)
                population.add(getRandomStrategy());
        }

        scorePopulation(population, counter, numDecks, numHands, betAmount, includeBettingStrategy);
        population = sortPopulation(population);
        
        for(int i = 0; i < numIterations; i++)
        {
            int numParents = (int)(popSize * parentPct);
            ArrayList<PlayerStrategy> parents = new ArrayList<PlayerStrategy>();
            for(int i2 = 0; i2 < numParents; i2++)
                parents.add(population.get(i2));
            population = (ArrayList<PlayerStrategy>)parents.clone();
            while(population.size() < popSize)
            {
                PlayerStrategy newStrategy;
                if(Math.random() < crossoverPct)
                    newStrategy = crossoverFunction(parents);
                else
                {
                    PlayerStrategy parent = parents.get((int)(Math.random() * parents.size()));
                    newStrategy = new PlayerStrategy(parent.baseHitPct.clone(), parent.countMultiplier.clone());
                }
                if(Math.random() < mutationPct)
                    newStrategy = mutationFunction(newStrategy, mutationDecayRate, counter, numDecks, numHands, betAmount, includeBettingStrategy);
                population.add(newStrategy);
            }
            scorePopulation(population, counter, numDecks, numHands, betAmount, includeBettingStrategy);
            population = sortPopulation(population);

            System.out.println("Iteration: " + (i + 1) + "/" + numIterations + " Best Return: " + (100 + (100 * (double)population.get(0).score / (double)(population.get(0).amountBet))) + "%");
            File writeFile = new File("Solutions.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));
            writer.write("Population Size: " + popSize);
            writer.newLine();
            for(PlayerStrategy strategy : population)
            {
                writer.write("Score: " + strategy.score);
                writer.newLine();
                writer.write("Return Rate: " + (100 + (100 * (double)strategy.score / (double)(strategy.amountBet))) + "%");
                writer.newLine();
                for(int i2 = 0; i2 < 17; i2++)
                {
                    writer.write("Hard " + (i2 + 4) + ": Base Hit %: " + (strategy.baseHitPct[i2] * 100) + " Count Multiplier: " + (strategy.countMultiplier[i2] * 100));
                    writer.newLine();
                }
                for(int i2 = 17; i2 < 26; i2++)
                {
                    writer.write("Soft " + (i2 - 5) + ": Base Hit %: " + (strategy.baseHitPct[i2] * 100) + " Count Multiplier: " + (strategy.countMultiplier[i2] * 100));
                    writer.newLine();
                }       
            }
            writer.close();
        }
    }

    static public ArrayList<Card> buildNewShoe(int numDecks)
    {
        ArrayList<Card> shoe = new ArrayList<Card>();
        for(int value = 1; value <= 13; value++)
        {
            for(int numCopies = 0; numCopies < 4*numDecks; numCopies++)
                shoe.add(new Card(value));
        }
        return shuffleShoe(shoe);
    }

    static public ArrayList<Card> shuffleShoe(ArrayList<Card> shoe)
    {
        ArrayList<Card> shuffledShoe = new ArrayList<Card>();
        int cardIndex;
        while(!shoe.isEmpty())
        {
            cardIndex = (int)(Math.random() * shoe.size());
            shuffledShoe.add(shoe.remove(cardIndex));
        }
        return shuffledShoe;
    }

    static public Card dealCard(ArrayList<Card> shoe, CardCounter counter)
    {
        Card card = shoe.get(0);
        shoe.remove(card);
        counter.countCard(card);
        return card;
    }

    static public int playDealerHand(ArrayList<Card> hand, ArrayList<Card> shoe, CardCounter counter)
    {
        int total = 0;
        boolean soft = false;
        for(Card card : hand)
        {
            if(card.value != 1)
            {
                if(card.value < 10)
                    total += card.value;
                else
                    total += 10;
                if(soft && total > 21)
                {
                    total -= 10;
                    soft = false;
                }
            }
            else
            {
                if(total < 10)
                {
                    total += 11;
                    soft = true;
                }
                else
                    total += 1;
            }
        }
        while(total < 17 || (total == 17 && soft))
        {
            Card card = dealCard(shoe, counter);
            if(card.value != 1)
            {
                if(card.value < 10)
                    total += card.value;
                else
                    total += 10;
                if(soft && total > 21)
                {
                    total -= 10;
                    soft = false;
                }
            }
            else
            {
                if(total < 10)
                {
                    total += 11;
                    soft = true;
                }
                else
                    total += 1;
            }
        }
        return total;
    }

    static public PlayerStrategy getRandomStrategy()
    {
        double[] baseHitPct = new double[26];
        double[] countMultiplier = new double[26];
        for(int i = 0; i < 26; i++)
        {
            baseHitPct[i] = Math.random();
            if(Math.random() < .5)
                countMultiplier[i] = Math.random();
            else
                countMultiplier[i] = -Math.random();
        }
        return new PlayerStrategy(baseHitPct, countMultiplier);
    }

    static public void scorePopulation(ArrayList<PlayerStrategy> population, CardCounter counter, int numDecks, int numHands, int betAmount, boolean includeBettingStrategy)
    {
        for(PlayerStrategy strategy : population)
            strategy.score = evaluationFunction(strategy, counter, numDecks, numHands, betAmount, includeBettingStrategy);
    }

    static public ArrayList<PlayerStrategy> sortPopulation(ArrayList<PlayerStrategy> population)
    {
        ArrayList<PlayerStrategy> sortedPopulation = new ArrayList<PlayerStrategy>();
        for(PlayerStrategy strategy : population)
            insert(sortedPopulation, strategy);
        return sortedPopulation;
    }

    static public void insert(ArrayList<PlayerStrategy> list, PlayerStrategy item)
    {
        if(list.isEmpty())
            list.add(item);
        else
        {
            int i = 0;
            while(i < list.size() && item.score < list.get(i).score)
                i++;
            list.add(i, item);
        }
    }

    static public int evaluationFunction(PlayerStrategy strategy, CardCounter counter, int numDecks, int numHands, int betAmount, boolean includeBettingStrategy)
    {
        int score = 0;
        ArrayList<Card> shoe = buildNewShoe(numDecks);
        ArrayList<Card> playerHand;
        ArrayList<Card> dealerHand;
        strategy.amountBet = 0;
        for(int i = 0; i < numHands; i++)
        {
            playerHand = new ArrayList<Card>();
            dealerHand = new ArrayList<Card>();
            if(shoe.size() <= 15)
            {
                shoe = buildNewShoe(numDecks);
                counter.count = 0;
            }
            if(includeBettingStrategy)
                betAmount *= counter.scaledCount;
            strategy.amountBet += betAmount;
            if(betAmount < 0)
                System.out.println(betAmount);
            playerHand.add(dealCard(shoe, counter));
            dealerHand.add(dealCard(shoe, counter));
            playerHand.add(dealCard(shoe, counter));
            dealerHand.add(dealCard(shoe, counter));
            int total = 0;
            boolean soft = false;
            for(Card card : playerHand)
            {
                if(card.value != 1)
                {
                    if(card.value < 10)
                        total += card.value;
                    else
                        total += 10;
                    if(soft && total > 21)
                    {
                        total -= 10;
                        soft = false;
                    }
                }
                else
                {
                    if(total < 10)
                    {
                        total += 11;
                        soft = true;
                    }
                    else
                        total += 1;
                }
            }
            if(total == 21)
                score += (int)(1.5 * betAmount);
            else
            {
                while(total < 21 && hitIt(total, soft, counter, strategy, numDecks))
                {
                    Card card = dealCard(shoe, counter);
                    if(card.value != 1)
                    {
                        if(card.value < 10)
                            total += card.value;
                        else
                            total += 10;
                        if(soft && total > 21)
                        {
                            total -= 10;
                            soft = false;
                        }
                    }
                    else
                    {
                        if(total < 10)
                        {
                            total += 11;
                            soft = true;
                        }
                        else
                            total += 1;
                    }
                }
                if(total > 21)
                    score -= betAmount;
                else
                {
                    int dealerScore = playDealerHand(dealerHand, shoe, counter);
                    if(dealerScore > 21)
                        score += betAmount;
                    else if(dealerScore > total)
                        score -= betAmount;
                    else if(total > dealerScore)
                        score += betAmount;
                }
            }
        }
        return score;
    }

    static public boolean hitIt(int total, boolean soft, CardCounter counter, PlayerStrategy strategy, int numDecks)
    {
        double hitPct;
        if(!soft)
            hitPct = strategy.baseHitPct[total - 4] + strategy.countMultiplier[total - 4] * counter.normalizedCount;
        else
            hitPct = strategy.baseHitPct[total + 5] + strategy.countMultiplier[total + 5] * counter.normalizedCount;
        if(Math.random() < hitPct)
            return true;
        return false;
    }

    static public PlayerStrategy crossoverFunction(ArrayList<PlayerStrategy> parents)
    {
        PlayerStrategy parent1 = parents.get((int)(Math.random() * parents.size()));
        PlayerStrategy parent2 = parents.get((int)(Math.random() * parents.size()));
        double[] baseHitPct = new double[26];
        double[] countMultiplier = new double[26];
        for(int i = 0; i < 26; i++)
        {
            if(Math.random() < .5)
                baseHitPct[i] = parent1.baseHitPct[i];
            else
                baseHitPct[i] = parent2.baseHitPct[i];
            if(Math.random() < .5)
                countMultiplier[i] = parent1.countMultiplier[i];
            else
                countMultiplier[i] = parent2.countMultiplier[i];
        }
        return new PlayerStrategy(baseHitPct, countMultiplier);
    }

    static public PlayerStrategy mutationFunction(PlayerStrategy strategy, double mutationDecayRate, CardCounter counter, int numDecks, int numHands, int betAmount, boolean includeBettingStrategy)
    {
        double mutationChance = 1;
        PlayerStrategy annealStrategy;
        while(mutationChance > 0)
        {
            annealStrategy = mutate(strategy);
            annealStrategy.score = evaluationFunction(annealStrategy, counter, numDecks, numHands, betAmount, includeBettingStrategy);
            if(annealStrategy.score > strategy.score || Math.random() < mutationChance)
                strategy = annealStrategy;
            mutationChance -= mutationDecayRate;
        }
        return strategy;
    }

    static public PlayerStrategy mutate(PlayerStrategy strategy)
    {
        PlayerStrategy newStrategy = new PlayerStrategy(strategy.baseHitPct.clone(), strategy.countMultiplier.clone());
        int index = (int)(Math.random() * 26);
        if(Math.random() < .5)
            newStrategy.baseHitPct[index] = Math.random();
        else
        {
            if(Math.random() < .5)
                newStrategy.countMultiplier[index] = Math.random();
            else
                newStrategy.countMultiplier[index] = -Math.random();
        }
        return newStrategy;
    }
}