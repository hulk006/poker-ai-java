package edu.ntnu.texasai.model;

import java.util.List;

import edu.ntnu.texasai.model.cards.CardNumber;
//表示牌的类型，还有选中一手牌
public class HandPower implements Comparable<HandPower> {
    private final HandPowerType handPowerType ;
    private final List<CardNumber> tieBreakingInformation;

    public HandPower(final HandPowerType handPowerType,
            final List<CardNumber> tieBreakingInformation) {
        this.handPowerType = handPowerType;
        this.tieBreakingInformation = tieBreakingInformation;
    }
//与其他手牌强度对比， 返回牌型的不同。如果相同返回0，不同返回typeDifference
    public int compareTo(HandPower other) {
        int typeDifference = handPowerType.getPower()
                - other.handPowerType.getPower();
        if (typeDifference == 0) {//如果==0,说明牌型一样，比较牌的大小
            for (int i = 0; i < tieBreakingInformation.size(); i++) {
                int tieDifference = tieBreakingInformation.get(i).getPower()
                        - other.tieBreakingInformation.get(i).getPower();
                if (tieDifference != 0) {
                    return tieDifference;
                }
            }
            return 0;
        }

        return typeDifference;
    }

    @Override
    public String toString() {
        return handPowerType.toString() + " "
                + tieBreakingInformation.toString();
    }

    public HandPowerType getHandPowerType() {
        return handPowerType;
    }

    public List<CardNumber> getTieBreakingInformation() {
        return tieBreakingInformation;
    }
}
