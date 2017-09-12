package    yh.ai.controller.phase2;

import yh.ai.TCP.dealMsg;
import    yh.ai.controller.EquivalenceClassController;
import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.model.BettingDecision;
import    yh.ai.model.BettingRoundName;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;
import    yh.ai.model.cards.Card;
import    yh.ai.model.cards.EquivalenceClass;
import    yh.ai.model.opponentmodeling.ContextPlayers;
import    yh.ai.persistence.PreFlopPersistence;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerPhaseIIBluff extends PlayerControllerPhaseII {
    private final EquivalenceClassController equivalenceClassController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PlayerControllerPhaseIIBluff(final EquivalenceClassController equivalenceClassController,
                                        final PreFlopPersistence preFlopPersistence,
                                        final HandStrengthEvaluator handStrengthEvaluator) {
        super(handStrengthEvaluator);

        this.equivalenceClassController = equivalenceClassController;
        this.preFlopPersistence = preFlopPersistence;
    }

    @Override
    public String toString() {
        return "PhaseII bluff";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        EquivalenceClass equivalenceClass = this.equivalenceClassController.cards2Equivalence(card1, card2);
        double percentageOfWins = preFlopPersistence.retrieve(gameHand.getPlayers().size(), equivalenceClass);
    	System.out.println("手牌的胜率为：" +  percentageOfWins);
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
    	
        if (percentageOfWins > 0.6 || percentageOfWins < 0.1)//bluff
            return BettingDecision.RAISE;
        else if (  percentageOfWins < 0.45)
            return BettingDecision.FOLD;
        return BettingDecision.CALL;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        double p = calculateCoefficient(gameHand, player);
        System.out.println("AI手牌的强度为：" + p);

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
        
        
        // Bluff
        if(p < 0.2){
            if(gameHand.getBettingRoundName().equals(BettingRoundName.POST_FLOP)){
                return BettingDecision.RAISE;
            }
            else if(ContextPlayers.valueFor(gameHand.getPlayersCount()).equals(ContextPlayers.FEW)){
                // Not too much player in post-turn and post-river
                return BettingDecision.RAISE;
            }
            else{
                return BettingDecision.FOLD;
            }
        }

        if (p > 0.8) {
            return BettingDecision.RAISE;
        } else if (p > 0.4 || canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        }
        return BettingDecision.FOLD;
    }
}
