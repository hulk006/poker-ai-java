package    yh.ai.controller.phase2;

import yh.ai.TCP.dealMsg;
import    yh.ai.controller.EquivalenceClassController;
import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.model.BettingDecision;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;
import    yh.ai.model.cards.Card;
import    yh.ai.model.cards.EquivalenceClass;
import    yh.ai.persistence.PreFlopPersistence;

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
       //********************************************************************************************************
        //考虑下注和自己的筹码
        int blind = dealMsg.getBlind();
        int highBet = dealMsg.getHighBet();
        int myChip = dealMsg.getAiChip();
        float big = highBet/blind; //>20
        float high =  highBet/myChip ; //1/2
        if( big >=  20|| high >=  1/2 )
        {
        	  if (percentageOfWins > 0.64)
                  return BettingDecision.CALL;
        	  else
        		  return BettingDecision.FOLD;
        }
        //*********************************************************************************************************
        //没有考虑别人加注的多少
        if (percentageOfWins > 0.6)
        {
            return BettingDecision.RAISE;
        }
        else if (percentageOfWins < 0.45)
            return BettingDecision.FOLD;
        return BettingDecision.CALL;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
    	
        double p = calculateCoefficient(gameHand, player);//发了公共牌之后就需要计算自己牌的强度系数来决策，p会根据当前玩家数量来调整
        //********************************************************************************************************
        //考虑下注和自己的筹码
        int blind = dealMsg.getBlind();
        int highBet = dealMsg.getHighBet();
        int myChip = dealMsg.getAiChip();
        float big = highBet/blind; //>20
        float high =  highBet/myChip ; //1/2
        if( big >=20|| high >= 1/2 )
        {
            if (p > 0.8) {
                return BettingDecision.CALL;
            } else
                return BettingDecision.FOLD;
        }
        
        //*********************************************************************************************************      
        if (p > 0.8) {
            return BettingDecision.RAISE;
        } else if (p > 0.4 || canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        }
        return BettingDecision.FOLD;
    }
}
