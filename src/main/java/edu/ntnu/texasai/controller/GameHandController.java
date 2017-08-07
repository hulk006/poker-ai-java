package edu.ntnu.texasai.controller;

import edu.ntnu.texasai.controller.opponentmodeling.OpponentModeler;
import edu.ntnu.texasai.model.*;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.gameproperties.GameProperties;
import edu.ntnu.texasai.utils.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GameHandController {
    protected final Logger logger;
    private final HandPowerRanker handPowerRanker;//手牌的多大？
    private final GameProperties gameProperties;
    private final StatisticsController statisticsController;//统计的控制器
    private final HandStrengthEvaluator handStrengthEvaluator;//手牌估计
    private final OpponentModeler opponentModeler;//对手模型

    @Inject
    public GameHandController(final Logger logger,
                              final HandPowerRanker handPowerRanker,
                              final GameProperties gameProperties,
                              final StatisticsController statisticsController,
                              final HandStrengthEvaluator handStrengthEvaluator,
                              final OpponentModeler opponentModeler) {
        this.logger = logger;
        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }
//这个类的输入是一局比赛
    public void play(Game game) {
        logger.log("-----------------------------------------");
        logger.logImportant("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");
        GameHand gameHand = createGameHand(game);//建立一个小局游戏

        Boolean haveWinner = false;//一局游戏开始没有胜利玩家
      //判断不是最后一次下注 POST_RIVER和 有胜利玩家
        while (!gameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER)
                && !haveWinner) 
        {
        	//玩一轮游戏的主函数，to see ,输入是一个对局，在这一轮中所有的active的玩家都下注一次
            haveWinner = playRound(gameHand);
        }

        if (!haveWinner) {
            showDown(gameHand);//结束对局
        }
    }
//一个私有的建立对局的函数，输入是一局比赛，把新的一轮游戏加进去，Game中有一个addGameHand 函数
    private GameHand createGameHand(Game game) {
        GameHand gameHand = new GameHand(game.getPlayers());//构造函数是还有几个玩家在玩
        game.addGameHand(gameHand);
        return gameHand;
    }
//开始玩一轮游戏，输入是gameHand，输出如果是有胜利的玩家就为ture，游戏没有结束false
    protected Boolean playRound(GameHand gameHand) {
        gameHand.nextRound();//判断是哪一轮下注，得到第几轮信息，然后发牌
        logBettingRound(gameHand);//记录
        int toPlay = gameHand.getPlayersCount();//获得玩家数量
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(gameHand);//盲注
            toPlay--; // Big blinds don't have to call on himself if no raise :)大盲位在本轮下注中的特殊性
        }

        int turn = 1;//计算第几次下注 
        int numberOfPlayersAtBeginningOfRound = gameHand.getPlayersCount();//开局玩家数。与上面的有一点不同
        //对于每一个玩家来说 都进行一次下注的决定，这里是几个AI都可自主决定下注
        while (toPlay > 0) {
            Player player = gameHand.getNextPlayer();//获得玩家，后面需要改这个东西
            //×××××××××××××××××××××××下注的决定，在Player Controller 里面××××××××××××××××××××××××××××××××××××××××××××
            BettingDecision bettingDecision = player.decide(gameHand);//to see

            // We can't raise at second turn 如何计算得到自己应该加注，但是我们不能加注，变成跟注
            //一轮下注完之后，turn = numberOfPlayersAtBeginningOfRound第turn次下注
            if (turn > numberOfPlayersAtBeginningOfRound
                    && bettingDecision.equals(BettingDecision.RAISE)) {
                bettingDecision = BettingDecision.CALL;
            }
            //如果当前ai加注，那么后面的都要玩？？？？ to think about
            // After a raise, every active players after the raiser must play
            if (bettingDecision.equals(BettingDecision.RAISE)) {
                toPlay = gameHand.getPlayersCount() - 1;
            }

            applyDecision(gameHand, player, bettingDecision);
            turn++;//一个玩家下注后 turn +1
            toPlay--;//剩余下注玩家 -1
        }
        //在所有的玩家都下注一轮后，检查是否有胜利的者，gameHand.getPlayersCount() == 1
        // Check if we have a winner
        if (gameHand.getPlayersCount() == 1) {
            Player winner = gameHand.getCurrentPlayer();
            winner.addMoney(gameHand.getTotalBets());
            logger.log(winner + ": WIN! +" + gameHand.getTotalBets() + "$");
            return true;
        }
        return false;
    }

    private void logBettingRound(GameHand gameHand) {
        String logMsg = "---" + gameHand.getBettingRoundName();
        logMsg += " (" + gameHand.getPlayersCount() + " players, ";
        logMsg += gameHand.getTotalBets() + "$)";
        if (!gameHand.getSharedCards().isEmpty()) {
            logMsg += " " + gameHand.getSharedCards();
        }
        logger.log(logMsg);
    }

    private void takeBlinds(GameHand gameHand) {
        Player smallBlindPlayer = gameHand.getNextPlayer();
        Player bigBlindPlayer = gameHand.getNextPlayer();
        //显示大盲和小盲
        logger.log("player#"+smallBlindPlayer.getNumber() + ": Small blind "
                + gameProperties.getSmallBlind() + "$");
        logger.log("player#" + bigBlindPlayer.getNumber() + ": Big blind "
                + gameProperties.getBigBlind() + "$");
        //把大盲和小盲放入奖池
        gameHand.getCurrentBettingRound().placeBet(smallBlindPlayer,
                gameProperties.getSmallBlind());
        gameHand.getCurrentBettingRound().placeBet(bigBlindPlayer,
                gameProperties.getBigBlind());
    }
    //AI做出决定后，将这个决定应用，
    private void applyDecision(GameHand gameHand, Player player, BettingDecision bettingDecision) {
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(),
                gameHand.getPlayersCount());//估计手牌强度值
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength);

        BettingRound bettingRound = gameHand.getCurrentBettingRound();
        if(player.IsAI() == true)
        {
        	  logger.log("player#"+player.getNumber() + ": " + bettingDecision + " "
                      + bettingRound.getBetForPlayer(player) + "$");
        }
        else
        {
        	   logger.log(player + ": " + bettingDecision + " "
                       + bettingRound.getBetForPlayer(player) + "$");
        }
        //logger.log(player + ": " + bettingDecision + " "   + bettingRound.getBetForPlayer(player) + "$");
    }
    //在德州扑克中，翻了河牌之后，可能有多个玩家牌的大小相同，就有多个胜利玩家
    private List<Player> getWinners(GameHand gameHand) {
        Iterable<Player> activePlayers = gameHand.getPlayers();//活着的玩家
        List<Card> sharedCards = gameHand.getSharedCards();//公牌

        HandPower bestHandPower = null;
        List<Player> winners = new ArrayList<Player>();
        for (Player player : activePlayers) {
            List<Card> mergeCards = new ArrayList<Card>(player.getHoleCards());
            mergeCards.addAll(sharedCards);//合并手牌与公牌
            HandPower handPower = handPowerRanker.rank(mergeCards);//利用7张牌来判定牌的大小

            logger.log(player + ": " + handPower);
            //if 					handPower>bestHandPower     
            if (bestHandPower == null || handPower.compareTo(bestHandPower) > 0)
            {
                winners.clear();
                winners.add(player);
                bestHandPower = handPower;
            }
            else if (handPower.equals(bestHandPower)) //也可能有多个玩家胜利 
            {
                winners.add(player);
            }
        }
        statisticsController.storeWinners(winners);
        return winners;
    }

    protected void showDown(GameHand gameHand) {
        logger.log("--- Showdown");

        // Showdown
        List<Player> winners = getWinners(gameHand);

        // Gains
        int gain = gameHand.getTotalBets() / winners.size();
        int modulo = gameHand.getTotalBets() % winners.size();
        for (Player winner : winners) {
            int gainAndModulo = gain;
            if (modulo > 0) {
                gainAndModulo += modulo;
            }
            winner.addMoney(gainAndModulo);
            logger.log("WINNER: "+winner + ": WIN! +" + gainAndModulo + "$");

            modulo--;
        }

        // Opponent modeling
        opponentModeler.save(gameHand);
    }
}
