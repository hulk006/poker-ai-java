package edu.ntnu.texasai.model.gameproperties;

import edu.ntnu.texasai.controller.phase2.PlayerControllerPhaseIIBluff;
import edu.ntnu.texasai.controller.phase2.PlayerControllerPhaseIINormal;
import edu.ntnu.texasai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import edu.ntnu.texasai.controller.phase3.PlayerControllerPhaseIIIConservative;
import edu.ntnu.texasai.model.Player;
import edu.ntnu.texasai.controller.phasehuman.PlayerControllerHuman;

import javax.inject.Inject;

public class DemoGameProperties extends GameProperties {
    @Inject
    public DemoGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                              final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                              final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative,
                              PlayerControllerHuman playerControllerHuman ) 
    {
        super(30, 1000, 20, 10);//设置玩多少局，携带金额，大小盲注。

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff));//加入玩家
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIINormal));
     
       // addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIAgressive));
       // addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIIIConservative));
      //  addPlayer(new Player(5, getInitialMoney(), playerControllerPhaseIIBluff));
        addPlayer(new Player(3, getInitialMoney(), playerControllerHuman) );
    }
}
