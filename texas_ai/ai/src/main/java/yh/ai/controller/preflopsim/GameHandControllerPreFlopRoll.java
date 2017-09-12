package    yh.ai.controller.preflopsim;

import com.google.inject.Inject;

import    yh.ai.controller.GameHandController;
import    yh.ai.controller.HandPowerRanker;
import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.controller.StatisticsController;
import    yh.ai.controller.opponentmodeling.OpponentModeler;
import    yh.ai.model.BettingRoundName;
import    yh.ai.model.Game;
import    yh.ai.model.GameHand;
import    yh.ai.model.cards.EquivalenceClass;
import    yh.ai.model.preflopsim.GameHandPreFlopRoll;
import    yh.ai.model.gameproperties.GameProperties;
import    yh.ai.utils.Logger;

public class GameHandControllerPreFlopRoll extends GameHandController {

    @Inject
    public GameHandControllerPreFlopRoll(Logger logger,
            HandPowerRanker handPowerRanker, GameProperties gameProperties,
            StatisticsController statisticsController, HandStrengthEvaluator handStrengthEvaluator, OpponentModeler opponentModeler) {
        super(logger, handPowerRanker, gameProperties, statisticsController, handStrengthEvaluator, opponentModeler);
    }

    public void play(Game game, EquivalenceClass equivalenceClass) {
        logger.log("-----------------------------------------");
        logger.log("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");
        logger.log("-----------------------------------------");
        logger.log(equivalenceClass.toString());
        logger.log("-----------------------------------------");
        GameHand gameHand = createGameHand(game, equivalenceClass);

        Boolean haveWinner = false;
        while (!gameHand.getBettingRoundName().equals(
                BettingRoundName.POST_RIVER)
                && !haveWinner) {
            haveWinner = playRound(gameHand);
        }

        if (!haveWinner) {
            showDown(gameHand);
        }
    }

    private GameHand createGameHand(Game game, EquivalenceClass equivalenceClass) {
        GameHand gameHand = new GameHandPreFlopRoll(game.getPlayers(),
                equivalenceClass);
        game.addGameHand(gameHand);
        return gameHand;
    }

}
