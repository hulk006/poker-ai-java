package    yh.ai.controller.preflopsim;

import    yh.ai.controller.EquivalenceClassController;
import    yh.ai.controller.StatisticsController;
import    yh.ai.model.Game;
import    yh.ai.model.Player;
import    yh.ai.model.cards.EquivalenceClass;
import    yh.ai.persistence.PreFlopPersistence;
import    yh.ai.model.gameproperties.GameProperties;
import    yh.ai.utils.Logger;

import javax.inject.Inject;
import java.util.Collection;
//进行手牌的仿真，得到手牌的胜率
public class PreFlopSimulatorController {
    private static final int ROLLOUTS_PER_EQUIV_CLASS = 100;

    private final Game game = new Game();//建立一局游戏
    private final Logger logger;//自己写的记录器
    private final GameProperties gameProperties;//定义牌局信息的类
    private final PlayerControllerPreFlopRoll playerControllerPreFlopRoll; //玩家决策控制
    private final EquivalenceClassController equivalenceClassController; //把牌转化为价值
    private final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll; //游戏对局控制
    private final StatisticsController statisticsController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PreFlopSimulatorController(final Logger logger, final GameProperties gameProperties,
                                      final PlayerControllerPreFlopRoll playerControllerPreFlopRoll,
                                      final EquivalenceClassController equivalenceClassController,
                                      final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll,
                                      final StatisticsController statisticsController,
                                      final PreFlopPersistence preFlopPersistence) {
        this.logger = logger;
        this.gameProperties = gameProperties;
        this.playerControllerPreFlopRoll = playerControllerPreFlopRoll;
        this.equivalenceClassController = equivalenceClassController;
        this.gameHandControllerPreFlopRoll = gameHandControllerPreFlopRoll;
        this.statisticsController = statisticsController;
        this.preFlopPersistence = preFlopPersistence;
    }

    public void play() {
    	//每一个手牌都有对应的hash值，产生一个数据库TABLE
        this.equivalenceClassController.generateAllEquivalenceClass();

        game.addPlayer(new Player(1, gameProperties.getInitialMoney(), playerControllerPreFlopRoll));//在整个游戏中加入一个玩家（1，初始钱，玩家控制器）
        Collection<EquivalenceClass> equivalenceClasses = equivalenceClassController.getEquivalenceClasses();

        for (int numberOfPlayers = 2; numberOfPlayers <= 10; numberOfPlayers++) {
            game.addPlayer(new Player(numberOfPlayers, 0, playerControllerPreFlopRoll));

            for (EquivalenceClass equivalenceClass : equivalenceClasses) {
                statisticsController.initializeStatistics();

                for (int i = 0; i < ROLLOUTS_PER_EQUIV_CLASS; i++) {
                    gameHandControllerPreFlopRoll.play(game, equivalenceClass);
                    game.setNextDealer();
                }

                double percentageWin = (double) statisticsController.getPlayer1Wins() / ROLLOUTS_PER_EQUIV_CLASS;
                preFlopPersistence.persist(numberOfPlayers, equivalenceClass, percentageWin);

                logger.logImportant("=================");
                logger.logImportant("STATISTICS FOR EQUIVALENCE CLASS "
                        + equivalenceClass.toString());
                logger.logImportant("Number of hands played: " + ROLLOUTS_PER_EQUIV_CLASS);
                logger.logImportant("Number players: " + numberOfPlayers);
                logger.logImportant("Percentage of wins is " + percentageWin);
            }
        }
    }
}
