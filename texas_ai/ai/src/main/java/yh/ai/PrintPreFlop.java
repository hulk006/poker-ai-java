package yh.ai;

//打印每一个手牌的概率

import com.google.inject.Guice;
import com.google.inject.Injector;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;
import yh.ai.persistence.PreFlopPersistence;

public class PrintPreFlop {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.DEMO));

		PreFlopPersistence preFlopPersistence = injector.getInstance(PreFlopPersistence.class);
		preFlopPersistence.print();
	}
}
