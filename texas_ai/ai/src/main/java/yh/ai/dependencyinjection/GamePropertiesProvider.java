package    yh.ai.dependencyinjection;

import    yh.ai.model.gameproperties.*;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
//通过provider 注入依赖
public class GamePropertiesProvider implements Provider<GameProperties> {
	//成员变量     
    private final GamePropertiesParameter gamePropertiesParameter;
    private final DemoGameProperties demoGameProperties;
    private final PhaseIGameProperties phaseIGameProperties;
    private final PhaseIIGameProperties phaseIIGameProperties;
    private final PhaseIIIGameProperties phaseIIIGameProperties;
    private  final TestGameProperties testGameProperties;


    @Inject
    public GamePropertiesProvider(final GamePropertiesParameter gamePropertiesParameter, 
    							  final DemoGameProperties demoGameProperties,
                                  final PhaseIGameProperties phaseIGameProperties,
                                  final PhaseIIGameProperties phaseIIGameProperties,
                                  final PhaseIIIGameProperties phaseIIIGameProperties,
                                  final TestGameProperties testGameProperties) {
        this.gamePropertiesParameter = gamePropertiesParameter;
        this.demoGameProperties = demoGameProperties;
        this.phaseIGameProperties = phaseIGameProperties;
        this.phaseIIGameProperties = phaseIIGameProperties;
        this.phaseIIIGameProperties = phaseIIIGameProperties;
        this.testGameProperties = testGameProperties;
    } 
    /*
    @Inject
    public GamePropertiesProvider(final GamePropertiesParameter gamePropertiesParameter, 
    							  final DemoGameProperties demoGameProperties,
                                  final PhaseIGameProperties phaseIGameProperties,
                                  final PhaseIIGameProperties phaseIIGameProperties,
                                  final PhaseIIIGameProperties phaseIIIGameProperties,
                                  final TestGameProperties testGameProperties  ) {
        this.gamePropertiesParameter = gamePropertiesParameter;
        this.demoGameProperties = demoGameProperties;
        this.phaseIGameProperties = phaseIGameProperties;
        this.phaseIIGameProperties = phaseIIGameProperties;
        this.phaseIIIGameProperties = phaseIIIGameProperties;
        this.testGameProperties = testGameProperties;
        this.mapMsg=null;      
    } 
    */

    
public GameProperties get() {
        switch (gamePropertiesParameter){
            case PHASE1:
                return phaseIGameProperties;
            case PHASE2:
                return phaseIIGameProperties;
            case PHASE3:
                return phaseIIIGameProperties;
            case TEST:
            	return testGameProperties;
            default:
                return demoGameProperties;
        }
    }
}
