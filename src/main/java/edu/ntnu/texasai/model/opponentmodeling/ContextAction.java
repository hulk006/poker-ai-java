package edu.ntnu.texasai.model.opponentmodeling;

import edu.ntnu.texasai.model.BettingDecision;
import edu.ntnu.texasai.model.BettingRoundName;
import edu.ntnu.texasai.model.Player;
//包含一个玩家的在一轮中的信息
public class ContextAction {
    private final Player player;//玩家
    private final BettingDecision bettingDecision;//下注决定
    private final BettingRoundName bettingRoundName;//当前是那一轮
    private final ContextRaises contextRaises;//加注的玩家数
    private final ContextPlayers contextPlayers;//剩余的玩家数
    private final ContextPotOdds contextPotOdds;//收益率 ，名称待定

    public ContextAction(Player player, BettingDecision bettingDecision, BettingRoundName bettingRoundName,
                         int numberOfRaises, int numberOfPlayersRemaining, double potOdds) {
        this.player = player;
        this.bettingDecision = bettingDecision;
        this.bettingRoundName = bettingRoundName;
        contextRaises = ContextRaises.valueFor(numberOfRaises);
        contextPlayers = ContextPlayers.valueFor(numberOfPlayersRemaining);
        contextPotOdds = ContextPotOdds.valueFor(potOdds);
    }
//判断ContextAction是否相等
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContextAction)) {
            return false;
        }

        ContextAction other = (ContextAction) o;

        return (player.equals(other.player) && bettingDecision.equals(other.bettingDecision) && bettingRoundName
                .equals(other.bettingRoundName) && contextRaises.equals(other.contextRaises) && contextPlayers.equals
                (other.contextPlayers) && contextPotOdds.equals(other.contextPotOdds));
    }

    public Player getPlayer() {
        return player;
    }

    public BettingDecision getBettingDecision() {
        return bettingDecision;
    }

    public BettingRoundName getBettingRoundName() {
        return bettingRoundName;
    }

    public ContextRaises getContextRaises() {
        return contextRaises;
    }

    public ContextPlayers getContextPlayers() {
        return contextPlayers;
    }

    public ContextPotOdds getContextPotOdds() {
        return contextPotOdds;
    }
}
