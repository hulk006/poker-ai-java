package    yh.ai.controller.phase2;

import    yh.ai.controller.HandStrengthEvaluator;
import    yh.ai.controller.PlayerController;
import    yh.ai.model.BettingRoundName;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;
import    yh.ai.model.opponentmodeling.ContextRaises;
//计算牌的强度系数
public abstract class PlayerControllerPhaseII extends PlayerController {
    private final HandStrengthEvaluator handStrengthEvaluator;//手牌估计器，利用仿真来计算手牌的强度
    //构造函数输入一个估记器
    protected PlayerControllerPhaseII(final HandStrengthEvaluator handStrengthEvaluator) {
        this.handStrengthEvaluator = handStrengthEvaluator;
    }
    //计算系数函数，输入是游戏对局信息，和某一个玩家p=胜率^玩家数
    protected double calculateCoefficient(GameHand gameHand, Player player) {
        double p = this.handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(),
                gameHand.getPlayers().size());//牌的强度系数，输入：手牌加公牌+玩家数量

        // Decision must depends on the number of players
        p = p * (1 + gameHand.getPlayersCount() / 20);

        // Last round, why not?
        if (gameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER)) {
            p += 0.3;
        }
        // Lot of raises, be careful超过3个人有加注行为，那就小心一点
        if (ContextRaises.valueFor(gameHand.getCurrentBettingRound().getNumberOfRaises()).equals(ContextRaises.MANY)) {
            p -= 0.3;
        }

        return p;
    }
}
