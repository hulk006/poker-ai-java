package    yh.ai.controller.phase3;

import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.controller.phase2.PlayerControllerPhaseIINormal;
import    yh.ai.controller.opponentmodeling.OpponentModeler;
import    yh.ai.model.BettingDecision;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;

import javax.inject.Inject;

public class PlayerControllerPhaseIIIConservative extends PlayerControllerPhaseIII {
    @Inject
    public PlayerControllerPhaseIIIConservative(PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                                HandStrengthEvaluator handStrengthEvaluator,
                                                OpponentModeler opponentModeler) {
        super(playerControllerPhaseIINormal, handStrengthEvaluator, opponentModeler);
    }

    @Override
    public String toString() {
        return "PhaseIII Conservative";
    }

    @Override
    protected BettingDecision decideBet(GameHand gameHand, Player player,
                                        int oppponentsWithBetterEstimatedHandStrength,
                                        int opponentsModeledCount) {
        if (oppponentsWithBetterEstimatedHandStrength == 0) {
            return BettingDecision.RAISE;
        } else if (canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        } else {
            return BettingDecision.FOLD;
        }
    }
}
