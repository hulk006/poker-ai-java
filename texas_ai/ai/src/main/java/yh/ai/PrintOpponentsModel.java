package    yh.ai;
//打印对手的模型的主类,只有4个玩家的信息
import com.google.inject.Guice;
import com.google.inject.Injector;
import    yh.ai.dependencyinjection.GamePropertiesParameter;
import    yh.ai.dependencyinjection.LogLevel;
import    yh.ai.dependencyinjection.TexasModule;
import    yh.ai.persistence.OpponentsModelPersistence;

public class PrintOpponentsModel {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.DEMO));

        OpponentsModelPersistence opponentsModelPersistence = injector.getInstance(OpponentsModelPersistence.class);
        opponentsModelPersistence.print();
    }
}
