package edu.ntnu.texasai.controller.phase2;

import edu.ntnu.texasai.controller.EquivalenceClassController;
import edu.ntnu.texasai.controller.HandStrengthEvaluator;
import edu.ntnu.texasai.model.BettingDecision;
import edu.ntnu.texasai.model.GameHand;
import edu.ntnu.texasai.model.Player;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.cards.EquivalenceClass;
import edu.ntnu.texasai.persistence.PreFlopPersistence;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerPhaseIINormal extends PlayerControllerPhaseII {
    private final EquivalenceClassController equivalenceClassController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PlayerControllerPhaseIINormal(final EquivalenceClassController equivalenceClassController,
                                         final PreFlopPersistence preFlopPersistence,
                                         final HandStrengthEvaluator handStrengthEvaluator) {
        super(handStrengthEvaluator);

        this.equivalenceClassController = equivalenceClassController;
        this.preFlopPersistence = preFlopPersistence;
    }

    @Override
    public String toString() {
        return "PhaseII normal";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
    	
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        //手牌转化为数学表达，花色+数字
        EquivalenceClass equivalenceClass = this.equivalenceClassController.cards2Equivalence(card1, card2);
        //手牌的胜率表
        //利用数据库文件，得到两张手牌的胜率，类似与查表，这里是建立了一个固定格式的数据库，然后输入特定字符去访问得到胜率
        double percentageOfWins = preFlopPersistence.retrieve(gameHand.getPlayers().size(), equivalenceClass);
        if (percentageOfWins > 0.6)
            return BettingDecision.RAISE;
        else if (percentageOfWins < 0.45)
            return BettingDecision.FOLD;
        return BettingDecision.CALL;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        double p = calculateCoefficient(gameHand, player);//发了公共牌之后就需要计算自己牌的强度系数来决策，p会根据当前玩家数量来调整
        if (p > 0.8) {
            return BettingDecision.RAISE;
        } else if (p > 0.4 || canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        }
        return BettingDecision.FOLD;
    }
}
