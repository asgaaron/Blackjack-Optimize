package blackjackoptimizer;

public class CardCounter
{
    public int count;
    public double normalizedCount;
    public double scaledCount;
    public int numDecks;

    public CardCounter(int numDecks)
    {
        count = 0;
        normalizedCount = 0;
        scaledCount = 0;
        this.numDecks = numDecks;
    }

    public void countCard(Card card)
    {
        if(card.value <= 6 && card.value >= 2)
            count++;
        else if(card.value == 1 || card.value >= 10)
            count--;
        normalizedCount = ((double)count/(20.0*numDecks));
        scaledCount = (normalizedCount + 1) / 2;
    }
}