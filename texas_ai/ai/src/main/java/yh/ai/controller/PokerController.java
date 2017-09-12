package    yh.ai.controller;

import yh.ai.model.BettingDecision;
import    yh.ai.model.Game;
import    yh.ai.model.Player;
import    yh.ai.model.gameproperties.GameProperties;
import    yh.ai.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PokerController {
	
    private final Game game;//游戏的类
    private final Logger logger;//记录的类
    private final GameProperties gameProperties;//游戏信息
       
    /*gameProperties
     *  smallBlind;
	 *	 bigBlind;
	 *	 initialMoney;
	 *	 numberOfHands;
	 *	  players = new ArrayList<Player>();
     */ 
    private final GameHandController gameHandController;//进行游戏的类，里面有每个玩家的下注

    @Inject
    public PokerController(final GameHandController gameHandController,
                           final Logger logger, final GameProperties gameProperties) {
        this.gameHandController = gameHandController;
        this.logger = logger;
        this.gameProperties = gameProperties;
        game = new Game(gameProperties.getPlayers());//获得参与游戏的所有玩家 这里只有两个
    }
//
    public void play() {
    	//100手
        for (int i = 0; i < gameProperties.getNumberOfHands(); i++) {
            gameHandController.play(game);
            game.setNextDealer();
        }
        printFinalStats();
    }
  //***********************自己调用的决策函数××××××××××××××××××××××××××××××××××××××××××××××××  
    public BettingDecision aiDecide() {
            return gameHandController.play2(game);
    }

    private void printFinalStats() {
        logger.log("-----------------------------------------");
        logger.log("Statistics");
        logger.log("-----------------------------------------");
        logger.log("Number of hands played: " + game.gameHandsCount());
        for (Player player : game.getPlayers()) {
            logger.log(player.toString() + "(" + player.getPlayerController().toString() + ")" + ": " + player
                    .getMoney() + "$");
        }
    }
}
