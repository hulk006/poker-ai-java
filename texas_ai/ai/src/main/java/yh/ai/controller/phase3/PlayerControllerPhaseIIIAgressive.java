package    yh.ai.controller.phase3;

import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.controller.phase2.PlayerControllerPhaseIINormal;
import    yh.ai.controller.opponentmodeling.OpponentModeler;
import    yh.ai.model.BettingDecision;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;

import javax.inject.Inject;

public class PlayerControllerPhaseIIIAgressive extends PlayerControllerPhaseIII {
    @Inject
    public PlayerControllerPhaseIIIAgressive(PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                                HandStrengthEvaluator handStrengthEvaluator,
                                                OpponentModeler opponentModeler) {
        super(playerControllerPhaseIINormal, handStrengthEvaluator, opponentModeler);
    }

    @Override
    public String toString() {
        return "PhaseIII Agressive";
    }

    @Override
    protected BettingDecision decideBet(GameHand gameHand, Player player,
                                        int oppponentsWithBetterEstimatedHandStrength,
                                        int opponentsModeledCount) {
        if ((double) oppponentsWithBetterEstimatedHandStrength / opponentsModeledCount > 0.5) {
            return BettingDecision.RAISE;
        } else if (canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        } else {
            return BettingDecision.FOLD;
        }
    }
}
