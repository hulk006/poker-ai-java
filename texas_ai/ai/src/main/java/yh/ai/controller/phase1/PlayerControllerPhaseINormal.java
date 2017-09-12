package    yh.ai.controller.phase1;

import    yh.ai.controller.HandPowerRanker;
import    yh.ai.controller.PlayerController;
import    yh.ai.model.*;
import    yh.ai.model.cards.Card;

import javax.inject.Inject;
import java.util.List;
//这个phasei正常模式只根据自己的手牌进行决策，pre_flop是对子或者大的高牌才跟注
public class PlayerControllerPhaseINormal extends PlayerController {
    private final HandPowerRanker handPowerRanker;//根据自己的手牌和公共牌得到最大的手牌

    @Inject
    public PlayerControllerPhaseINormal(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "PhaseI normal";
    }
    //在只有公共牌的时候
    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);

        if (card1.getNumber().equals(card2.getNumber())) //一对就加注
        { 
            return BettingDecision.RAISE;
        } 
        else if (card1.getNumber().getPower() + card2.getNumber().getPower() > 16
                || canCheck(gameHand, player))//如果是大牌，或者可以看牌 就跟注 
        {
            return BettingDecision.CALL;//TODO 计入看牌的机制
        } 
        else //正常模式弃牌的几率比较大，只看自己的手牌
        {
            return BettingDecision.FOLD;
        }
    }
    //在发了公共牌的时候
    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);//5张牌的类型 

        HandPowerType handPowerType = handPower.getHandPowerType();//牌类型
        if (handPowerType.equals(HandPowerType.HIGH_CARD))//如果高排 
        {
            if (canCheck(gameHand, player))//如果能够看牌，可以返回跟注，也可以返回check 
            {
                return BettingDecision.CALL;
            }
            return BettingDecision.FOLD; //否则就弃牌
        } 
        else if (handPowerType.getPower() >= HandPowerType.STRAIGHT.getPower()) //如过大于顺子就加注
        {
            return BettingDecision.RAISE;
        } 
        else//大于高排小于顺子就跟注
        {
            return BettingDecision.CALL;
        }
    }
}
