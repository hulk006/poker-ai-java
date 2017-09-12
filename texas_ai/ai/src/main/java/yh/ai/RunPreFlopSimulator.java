package    yh.ai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import    yh.ai.controller.preflopsim.PreFlopSimulatorController;
import    yh.ai.dependencyinjection.GamePropertiesParameter;
import    yh.ai.dependencyinjection.LogLevel;
import    yh.ai.dependencyinjection.TexasModule;

public class RunPreFlopSimulator {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.IMPORTANT, GamePropertiesParameter.PHASE1));

        PreFlopSimulatorController preFlopSimulatorController = injector
                .getInstance(PreFlopSimulatorController.class);
        preFlopSimulatorController.play();
    }
}
