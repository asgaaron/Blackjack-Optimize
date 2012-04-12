package blackjackoptimizer;

public class PlayerStrategy
{
    public double[] baseHitPct;
    public double[] countMultiplier;
    int score;
    int amountBet;

    public PlayerStrategy(double[] baseHitPct, double[] countMultiplier)
    {
        this.baseHitPct = baseHitPct;
        this.countMultiplier = countMultiplier;
        score = 0;
        amountBet = 0;
    }
}