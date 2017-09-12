package    yh.ai.controller;

import com.google.inject.AbstractModule;
import    yh.ai.controller.opponentmodeling.OpponentModeler;
import    yh.ai.controller.phase1.PlayerControllerPhaseIBluff;
import    yh.ai.controller.phase1.PlayerControllerPhaseINormal;
import    yh.ai.controller.phase2.PlayerControllerPhaseIIBluff;
import    yh.ai.controller.phase2.PlayerControllerPhaseIINormal;
import    yh.ai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import    yh.ai.controller.phase3.PlayerControllerPhaseIIIConservative;
import    yh.ai.controller.preflopsim.PreFlopSimulatorModule;
import    yh.ai.controller.phasehuman.PlayerControllerHuman;//add the human 

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PreFlopSimulatorModule());

        bind(PokerController.class).in(Singleton.class);
        bind(GameHandController.class).in(Singleton.class);
        bind(HandPowerRanker.class).in(Singleton.class);
        bind(StatisticsController.class).in(Singleton.class);
        bind(HandStrengthEvaluator.class).in(Singleton.class);
        bind(EquivalenceClassController.class).in(Singleton.class);
        bind(OpponentModeler.class).in(Singleton.class);

        bind(PlayerControllerPhaseINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIAgressive.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIConservative.class).in(Singleton.class);
        bind(PlayerControllerHuman.class).in(Singleton.class);//add the human
    }
}
