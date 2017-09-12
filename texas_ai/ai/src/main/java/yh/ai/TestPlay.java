package yh.ai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import yh.ai.controller.PokerController;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;
import yh.ai.TCP.ReceiveMsg;;
//测试接口的类
public class TestPlay {
	//public ReceiveMsg receiveMsg = new ReceiveMsg();
	
	public static void main(String[] args) {
		//String gameP = "demo";
		
		String gameP = "test";//加入一个test完全是为了调试如何让ai从外界接受消息来进行游戏
		if (args.length == 1) { // 带有参数的
			gameP = args[0];
		}

		
		Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.fromString(gameP)));
		PokerController pokerController = injector.getInstance(PokerController.class);
		pokerController.play();
	}

}
