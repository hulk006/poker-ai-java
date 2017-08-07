package edu.ntnu.texasai.controller.phase1;

import edu.ntnu.texasai.controller.HandPowerRanker;
import edu.ntnu.texasai.controller.PlayerController;
import edu.ntnu.texasai.model.*;
import edu.ntnu.texasai.model.cards.Card;

import javax.inject.Inject;
import java.util.List;
//
public class PlayerControllerPhaseIBluff extends PlayerController {
    private final HandPowerRanker handPowerRanker;

    @Inject
    public PlayerControllerPhaseIBluff(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "PhaseI bluff";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        int sumPower = card1.getNumber().getPower() + card2.getNumber().getPower();

        if (card1.getNumber().equals(card2.getNumber()) || sumPower <= 8) //如果花色相同，或者手牌是两个比较小的牌，加注
        {
            return BettingDecision.RAISE;
        } 
        else
        {
            if (sumPower > 16 || canCheck(gameHand, player))//或者手牌比较大，或者可以看牌
            {
                return BettingDecision.CALL;
            }
            else //其他情况就弃牌
            {
                return BettingDecision.FOLD;
            }
        }
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand,
                                           List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);

        HandPowerType handPowerType = handPower.getHandPowerType();
        if (handPowerType.equals(HandPowerType.HIGH_CARD))//如果是高牌就加注
        {
            return BettingDecision.RAISE;
        } else if (handPowerType.getPower() >= HandPowerType.STRAIGHT.getPower())//大于顺子也加注
        {
            return BettingDecision.RAISE;
        } 
        else
        {
            if(canCheck(gameHand, player))
            {
                return BettingDecision.CALL;
            }
            return BettingDecision.FOLD;
        }
    }
}
