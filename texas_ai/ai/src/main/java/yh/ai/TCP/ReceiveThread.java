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
import com.google.inject.Guice;
import com.google.inject.Injector;

import yh.ai.controller.PokerController;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;
import yh.ai.model.BettingDecision;
import yh.ai.model.Player;

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
		
			
			dealMsg dealmsg = new dealMsg(info);// 将接收到的消息处理成一个信息的map
			long startTime = System.currentTimeMillis();//获取当前时间
			if( formateDetetection(dealmsg))//如果通过格式检测
			{
				System.out.println("**************info*********************");
				List<Player> players = dealMsg.getPlayers();

				System.out.println("其他玩家数量" + players.size());
				// 玩游戏*********************************************************************
				// 做决策
				String gameP = "test"; // 游戏模式，一个demo
				// TexasModule 绑定了接口与实现
				Injector injector = Guice
						.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.fromString(gameP)));// 设置记录器版本，和运行的版本，可以去掉的啊
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
			}
			else {
				socket.shutdownInput();// 关闭输入流
				socket.shutdownOutput();
			}
			long endTime = System.currentTimeMillis();//获取当前时间
			System.out.println("做出决策时间："+(endTime - startTime)+"ms");
			
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
	
	private boolean formateDetetection(dealMsg dealMsg)
	{
		String [] ss=new String[]{   "aiLevel" , "blind"  , "initialMoney" , "aiSeat"  ,    "gameHandNum",    "betRound" };
		String [] importants = new String[]{  "gameId","blind", "highBet", "aiChip" ,  "aiHoleCards"  ,"comCards"  };
		//String defaultString = new String("aiLevel:3;gameId:1111;blind:10,20;initialMoney:5000;players:1+1000+raise,2+2000+call;bettingRoundName:preflop");
		for(String important:importants)
		{
			if( yh.ai.TCP.dealMsg.mapMsg.get(important) == null )
			{
				System.err.println("lack of " + important);
				return false;
			}
		}
		
		if(yh.ai.TCP.dealMsg.mapMsg.get("aiHoleCards").size()!=2)
		{
			System.err.println("aiHoleCards error!!! ");
			return false;
		}

		if((yh.ai.TCP.dealMsg.mapMsg.get("comCards").size()>0&&yh.ai.TCP.dealMsg.mapMsg.get("comCards").size() < 3) ||yh.ai.TCP.dealMsg.mapMsg.get("comCards").size() >5 )
		{
			System.err.println("comCards error!!! ");
			return false;
		}
					
		//默认2个人在玩+1个ai
		if( yh.ai.TCP.dealMsg.mapMsg.get("players") == null )
		{
			List<String> values = new ArrayList<String>(); // 初始化为空
			values.add("1+1000+raise");
			values.add("2+2000+call");
			yh.ai.TCP.dealMsg.mapMsg.put("players", values);
		}
		
		if( yh.ai.TCP.dealMsg.mapMsg.get("bettingRoundName") == null )
		{
			List<String> values = new ArrayList<String>(); // 初始化为空
			values.add("preflop");
			yh.ai.TCP.dealMsg.mapMsg.put("bettingRoundName", values);
		}
		
		for(String s:ss)
		{
			if( yh.ai.TCP.dealMsg.mapMsg.get(s) == null )
			{
				List<String> values = new ArrayList<String>(); // 初始化为空
				values.add("1");
				yh.ai.TCP.dealMsg.mapMsg.put(s, values);
			}
		}
		return true;
	}
}
