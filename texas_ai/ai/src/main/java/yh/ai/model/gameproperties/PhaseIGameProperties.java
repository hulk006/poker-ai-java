package    yh.ai.model.gameproperties;

import    yh.ai.controller.phase1.PlayerControllerPhaseIBluff;
import    yh.ai.controller.phase1.PlayerControllerPhaseINormal;
import    yh.ai.model.Player;

import javax.inject.Inject;
//继承GAME的类 来定义I 的4个都为PhaseI的玩家
public class PhaseIGameProperties extends GameProperties {
    @Inject
    public PhaseIGameProperties(final PlayerControllerPhaseINormal playerControllerPhaseINormal, final PlayerControllerPhaseIBluff playerControllerPhaseIBluff) {
        super(100, 1000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIBluff));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIBluff));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseINormal));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseINormal));
    }
}
