package yh.ai.TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.inject.Guice;
import com.google.inject.Injector;

import yh.ai.*;
import yh.ai.controller.EquivalenceClassController;
import yh.ai.controller.GameHandController;
import yh.ai.controller.HandPowerRanker;
import yh.ai.controller.HandStrengthEvaluator;
import yh.ai.controller.PlayerController;
import yh.ai.controller.PokerController;
import yh.ai.controller.StatisticsController;
import yh.ai.controller.opponentmodeling.OpponentModeler;
import yh.ai.controller.phase2.PlayerControllerPhaseIIBluff;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;
import yh.ai.model.BettingDecision;
import yh.ai.model.Game;
import yh.ai.model.Player;
import yh.ai.model.cards.Card;
import yh.ai.model.gameproperties.GameProperties;
import yh.ai.persistence.PersistenceManager;
import yh.ai.persistence.PreFlopPersistence;
import yh.ai.utils.ConsoleLogger;
import yh.ai.utils.Logger;

public class ReceiveThread extends Thread {
	// 和本线程相关的Socket
		Socket socket = null;
	 
		// 构造方法传入 与客户端的链接
		public ReceiveThread(Socket socket) {
			this.socket = socket;
		}

		// 线程执行的操作，响应客户端的请求
		public void run() {
			InputStream is = null; // 输入流
			InputStreamReader isr = null; // 建立一个输入流的 读取器
			BufferedReader br = null;// InputStream的链接处理为BufferedReader

			OutputStream os = null;// 输出流
			BufferedWriter pw = null;
			try {
				is = socket.getInputStream(); // 获取输入流，并读取输入流
				isr = new InputStreamReader(is); // 建立一个输入流的读取器
				br = new BufferedReader(isr);// 读取器
				String info = null; // 输入的字符串，读取字符串
				info = br.readLine();// 只读取一行数据，消息的末尾必须加上换行符，作为结束标志 \n
				// while( (info=br.readLine()) !=null)
				{
					// 循环读取客户端的信息
					System.out.println("我是服务器，客户端说：" + info);
				}
                 dealMsg dealmsg = new  dealMsg( info);//将接收到的消息处理成一个信息的map   
                 System.out.println();
                 System.out.println("**************info*********************");
                 List<Player> players = dealMsg.getPlayers();
                 
                 System.out.println("其他玩家数量" + players.size()); 
                 List<Card> cards= dealMsg.getAiHoleCards();
                 //玩游戏*********************************************************************
                 //做决策
                 String gameP = "test"; //游戏模式，一个demo 		
              //TexasModule 绑定了接口与实现
         	    Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.fromString(gameP)));//设置记录器版本，和运行的版本，可以去掉的啊
         		PokerController pokerController = injector.getInstance(PokerController.class);    		
         		BettingDecision decision = pokerController.aiDecide();	
         		
				socket.shutdownInput();// 关闭输入流

				// 服务器的输出部分××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
				os = socket.getOutputStream(); // 获取输出流，响应客户端的请求
				pw = new BufferedWriter(new OutputStreamWriter(os));
				pw.write(decision.toString());
				pw.newLine();
				pw.flush();// 调用flush()方法将缓冲输出
				socket.shutdownOutput();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// 关闭资源
				try {
					if (pw != null)
						pw.close();
					if (os != null)
						os.close();
					if (br != null)
						br.close();
					if (isr != null)
						isr.close();
					if (is != null)
						is.close();
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

}








