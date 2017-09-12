package    yh.ai.model.opponentmodeling;
//一个模型的结果的类，用于存储对手的模型
public class ModelResult {
    private double handStrengthAverage;//手牌强度的均值
    private double handStrengthDeviation;//协方差
    private int numberOfOccurences;//发生了多少手

    public ModelResult(double handStrengthAverage, double handStrengthDeviation, int numberOfOccurences) {
        this.handStrengthAverage = handStrengthAverage;
        this.handStrengthDeviation = handStrengthDeviation;
        this.numberOfOccurences = numberOfOccurences;
    }

    public double getHandStrengthAverage() {
        return handStrengthAverage;
    }

    public double getHandStrengthDeviation() {
        return handStrengthDeviation;
    }

    public int getNumberOfOccurences() {
        return numberOfOccurences;
    }
}
