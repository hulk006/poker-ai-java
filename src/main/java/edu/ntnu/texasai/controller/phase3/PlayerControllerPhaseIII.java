package edu.ntnu.texasai.controller.phase3;

import edu.ntnu.texasai.controller.HandStrengthEvaluator;
import edu.ntnu.texasai.controller.PlayerController;
import edu.ntnu.texasai.controller.phase2.PlayerControllerPhaseIINormal;
import edu.ntnu.texasai.controller.opponentmodeling.OpponentModeler;
import edu.ntnu.texasai.model.BettingDecision;
import edu.ntnu.texasai.model.BettingRound;
import edu.ntnu.texasai.model.GameHand;
import edu.ntnu.texasai.model.Player;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.opponentmodeling.ContextAction;
import edu.ntnu.texasai.model.opponentmodeling.ModelResult;

import java.util.List;

public abstract class PlayerControllerPhaseIII extends PlayerController {
    private final PlayerControllerPhaseIINormal playerControllerPhaseIINormal;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;

    protected PlayerControllerPhaseIII(PlayerControllerPhaseIINormal playerControllerPhaseIINormal, HandStrengthEvaluator
            handStrengthEvaluator, OpponentModeler opponentModeler) {
        this.playerControllerPhaseIINormal = playerControllerPhaseIINormal;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        return playerControllerPhaseIINormal.decidePreFlop(player, gameHand, cards);
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        BettingRound currentBettingRound = gameHand.getCurrentBettingRound();//当前下注的
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(),
                gameHand.getPlayersCount());//估计自己牌的强度
        int opponentsModeledCount = 0;//对手们的模型个数
        int oppponentsWithBetterEstimatedHandStrength = 0;
        //对对手进行建模
        for (Player opponent : gameHand.getPlayers())
        {
            // Only try to model opponent
            if (!opponent.equals(player)) {
            	//冲当前的一轮下注信息中，得到某一个对手的contextAction信息，包括玩家明，玩家下注操作，加注玩家数，剩余玩家，收益率
                ContextAction contextAction = currentBettingRound.getContextActionForPlayer(opponent);

                if (contextAction != null) {
                	//利用对手的信息，来估计对手的牌的强度 handStrengthAverage;//手牌强度的均值
                    //private double handStrengthDeviation;//协方差
                    //private int numberOfOccurences;//发生了多少手
                    ModelResult modelResult = opponentModeler.getEstimatedHandStrength(contextAction);
                    //如果 没有足够的回合数，或者方差过大，这个模型是不可信的
                    // If we don't have enough occurence or if the variance is big, the information is not valuable
                    if (modelResult.getNumberOfOccurences() > 10 && modelResult.getHandStrengthDeviation() <= 0.15) {
                        opponentsModeledCount++;//对手的模型计数加一
                        if (modelResult.getHandStrengthAverage() > handStrength) {
                            oppponentsWithBetterEstimatedHandStrength++;  //大于自己手牌的玩家数
                        }
                    }
                }
            }
        }
        //如果没有足够的对局信息，那么就使用第二种状态AI的决策
        // If we don't have enough context action in the current betting round
        if ((double) opponentsModeledCount / gameHand.getPlayersCount() < 0.5) {
            // We fall back to a phase II bot
            return playerControllerPhaseIINormal.decideAfterFlop(player, gameHand, cards);
        }

        return decideBet(gameHand, player, oppponentsWithBetterEstimatedHandStrength, opponentsModeledCount);
    }

    protected abstract BettingDecision decideBet(GameHand gameHand, Player player,
                                                 int oppponentsWithBetterEstimatedHandStrength,
                                                 int opponentsModeledCount);
}
