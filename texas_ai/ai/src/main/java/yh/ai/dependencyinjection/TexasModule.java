package    yh.ai.dependencyinjection;

import com.google.inject.AbstractModule;
import    yh.ai.controller.ControllerModule;
import    yh.ai.model.gameproperties.GameProperties;
import    yh.ai.persistence.PersistenceModule;
import    yh.ai.utils.Logger;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
//主要总有是配置 接口与实现
public class TexasModule extends AbstractModule {
	//成员变量
    private final LogLevel  logLevel;
    private final GamePropertiesParameter  gamePropertiesParameter;
    //自己加的一个构造方法
    public TexasModule(LogLevel logLevel, GamePropertiesParameter gamePropertiesParameter  ) 
    {
        this.logLevel = logLevel;//记录的完整性
        this.gamePropertiesParameter = gamePropertiesParameter;
    }

    public TexasModule() {
        logLevel = LogLevel.ALL;//完整的记录
        gamePropertiesParameter = GamePropertiesParameter.DEMO;//参数选中demo
    }

       //AbstractModule构建Guice的object-graph，
      //重载Configure方法实现接口和实现类的绑定。
    @Override
    protected void configure() {
        install(new ControllerModule());
        install(new PersistenceModule());

        bind(LogLevel.class).toInstance(logLevel);//实例记录完整的信息
        bind(GamePropertiesParameter.class).toInstance(gamePropertiesParameter);//实例加入demo的信息
        //将实现与 抽象绑定在一起
        
        bind(GameProperties.class).toProvider(GamePropertiesProvider.class).in(Singleton.class);
        
        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);
    }
}
