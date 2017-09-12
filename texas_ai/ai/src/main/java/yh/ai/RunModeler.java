package yh.ai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import yh.ai.controller.PokerController;
import yh.ai.controller.opponentmodeling.OpponentModeler;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;
import yh.ai.model.opponentmodeling.ContextAggregate;
import yh.ai.persistence.OpponentsModelPersistence;

import java.util.List;

public class RunModeler {
	// 主函数
	public static void main(String[] args) {
		// 为已经存在的TexasModule， 产生一个注入器（）
		Injector injector = Guice.createInjector(new TexasModule(LogLevel.IMPORTANT, GamePropertiesParameter.PHASE3));
		OpponentsModelPersistence opponentsModelPersistence = injector.getInstance(OpponentsModelPersistence.class);
		// 通过注入器获得一个实例化
		opponentsModelPersistence.clear();

		PokerController pokerController = injector.getInstance(PokerController.class);
		pokerController.play();

		OpponentModeler opponentModeler = injector.getInstance(OpponentModeler.class);
		persistOpponentModelingData(opponentModeler, opponentsModelPersistence);
	}

	private static void persistOpponentModelingData(OpponentModeler opponentModeler,
			OpponentsModelPersistence opponentsModelPersistence) {
		for (List<ContextAggregate> playerModel : opponentModeler.getPlayerModels().values()) {
			for (ContextAggregate contextAggregate : playerModel) {
				opponentsModelPersistence.persist(contextAggregate);
			}
		}
	}
}
