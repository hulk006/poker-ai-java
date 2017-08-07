package edu.ntnu.texasai.controller.opponentmodeling;

import edu.ntnu.texasai.model.BettingRound;
import edu.ntnu.texasai.model.GameHand;
import edu.ntnu.texasai.model.Player;
import edu.ntnu.texasai.model.opponentmodeling.ContextAction;
import edu.ntnu.texasai.model.opponentmodeling.ContextAggregate;
import edu.ntnu.texasai.model.opponentmodeling.ContextInformation;
import edu.ntnu.texasai.model.opponentmodeling.ModelResult;
import edu.ntnu.texasai.persistence.OpponentsModelPersistence;

import javax.inject.Inject;
import java.util.*;
//对手的模型
public class OpponentModeler {
	//建立一个map《玩家，玩家的攻击性信息（列表）》
    private final Map<Player, List<ContextAggregate>> playerModels = new HashMap<Player, List<ContextAggregate>>();
    private final OpponentsModelPersistence opponentsModelPersistence;

    @Inject
    public OpponentModeler(final OpponentsModelPersistence opponentsModelPersistence) {
        this.opponentsModelPersistence = opponentsModelPersistence;
    }

    public void save(GameHand gameHand) {
        Deque<Player> showdownPlayers = gameHand.getPlayers();

        for (BettingRound bettingRound : gameHand.getBettingRounds()) {
            for (ContextInformation contextInformation : bettingRound.getContextInformations()) {
                Player player = contextInformation.getContextAction().getPlayer();

                if (showdownPlayers.contains(player)) {
                    // Only save context opponent modeling for players who reach showdown
                	//只存储对手的模型，（完成对局的）
                    addToPlayerModel(contextInformation);
                }
            }
        }
    }
    //在data 的数据库里面，里面有对手模型的表格，有手牌强度的表格
    public ModelResult getEstimatedHandStrength(ContextAction contextAction) {
        return opponentsModelPersistence.retrieve(contextAction);//检索，得到的是模型结果，具体如何对应的，没有写清楚
    }

    public Map<Player, List<ContextAggregate>> getPlayerModels() {
        return playerModels;
    }

    private void addToPlayerModel(ContextInformation contextInformation) {
        ContextAggregate contextAggregate = getContextAggregate(contextInformation.getContextAction());
        contextAggregate.addOccurrence(contextInformation.getHandStrength());
    }

    private ContextAggregate getContextAggregate(ContextAction contextAction) {
        Player player = contextAction.getPlayer();

        List<ContextAggregate> contextAggregates = playerModels.get(player);

        if (contextAggregates == null) {
            contextAggregates = new ArrayList<ContextAggregate>();
            playerModels.put(player, contextAggregates);
        }


        for (ContextAggregate contextAggregate : contextAggregates) {
            if (contextAggregate.getContextAction().equals(contextAction)) {
                return contextAggregate;
            }
        }

        ContextAggregate contextAggregate = new ContextAggregate(contextAction);
        contextAggregates.add(contextAggregate);

        return contextAggregate;
    }
}
