package    yh.ai.controller.opponentmodeling;

import    yh.ai.model.BettingRound;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;
import    yh.ai.model.opponentmodeling.ContextAction;
import    yh.ai.model.opponentmodeling.ContextAggregate;
import    yh.ai.model.opponentmodeling.ContextInformation;
import    yh.ai.model.opponentmodeling.ModelResult;
import    yh.ai.persistence.OpponentsModelPersistence;

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
            for (ContextInformation contextInformation : bettingRound.getContextInformations())    //一个玩家的下注信息
            { 
                Player player = contextInformation.getContextAction().getPlayer();

                if (showdownPlayers.contains(player)) {
                    // Only save context opponent modeling for players who reach showdown
                	//只存储对手的模型，（完成对局的），如果他完成对局我才能看到他的手牌
                    addToPlayerModel(contextInformation);
                }
            }
        }
    }
    //在data 的数据库里面，里面有对手模型的表格，有手牌强度的表格，通过玩家的操作数据，来估计对手的手牌强度
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

    private ContextAggregate getContextAggregate(ContextAction contextAction) 
    {
        Player player = contextAction.getPlayer();//获得玩家的id，和信息
        List<ContextAggregate> contextAggregates = playerModels.get(player);//从map中，找到这个玩家的list历史信息

        if (contextAggregates == null) {
            contextAggregates = new ArrayList<ContextAggregate>();//如果这个玩家没有历史信息就新建一个，并且放入map中
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
