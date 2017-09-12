package    yh.ai.model.gameproperties;

import    yh.ai.controller.phase2.PlayerControllerPhaseIIBluff;
import    yh.ai.controller.phase2.PlayerControllerPhaseIINormal;
import    yh.ai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import    yh.ai.controller.phase3.PlayerControllerPhaseIIIConservative;
import    yh.ai.model.Player;
import    yh.ai.controller.phasehuman.PlayerControllerHuman;

import javax.inject.Inject;

public class DemoGameProperties extends GameProperties {
    @Inject
    public DemoGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                              final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                              final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative,
                              PlayerControllerHuman playerControllerHuman ) 
    {
        super(30, 1000, 20, 10);//设置玩多少局，携带金额，大小盲注。//得到消息

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff));//一个ai 需要得到 ai的牌局信息
       // addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIINormal));
       // addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIAgressive));
       // addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIIIConservative));
      //  addPlayer(new Player(5, getInitialMoney(), playerControllerPhaseIIBluff));
        
        addPlayer(new Player(2, getInitialMoney(), playerControllerHuman) );//人类玩家的所有信息都必须来自服务器的信息
       // addPlayer(new Player(4, getInitialMoney(), playerControllerHuman) );//
       // addPlayer(new Player(5, getInitialMoney(), playerControllerHuman) );//加入一个人类玩家
    }
}
