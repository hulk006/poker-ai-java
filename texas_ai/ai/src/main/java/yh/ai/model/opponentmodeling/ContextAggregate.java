package    yh.ai.model.opponentmodeling;

import java.util.ArrayList;
import java.util.List;
//判断对手是否是有攻击性的，如何判断是否为攻击性的
public class ContextAggregate {
    private final ContextAction contextAction;//存储一个玩家的历史信息
    List<Double> handStrengths = new ArrayList<Double>();//存储手牌强度的列表啊

    public ContextAggregate(final ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    public void addOccurrence(double handStrength) {
        handStrengths.add(handStrength);
    }

    public ContextAction getContextAction() {
        return contextAction;
    }
//手牌强度的均值
    public double getHandStrengthAverage() {
        double sum = 0;
        for (Double handStrength : handStrengths) {
            sum += handStrength;
        }

        return sum / getNumberOfOccurrences();
    }
//求手牌强度的协方差
    public double getDeviation() {
        double avg = getHandStrengthAverage();
        double variance = 0;//方差

        for (Double handStrength : handStrengths) {
            variance += Math.pow(handStrength - avg, 2);
        }

        return Math.sqrt(variance / getNumberOfOccurrences());
    }
//一共有多少手
    public int getNumberOfOccurrences() {
        return handStrengths.size();
    }
}
