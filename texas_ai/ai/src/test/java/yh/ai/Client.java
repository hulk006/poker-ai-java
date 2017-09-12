package yh.ai;

import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) throws Exception
	{

		final int length = 100;
		String host = "localhost";// 本机的ip地址
		int port = 2024;// 端口号
		Socket socket = null;
		try
		{
		socket = new Socket(host, port);// 建立一个socket
		BufferedReader in = new BufferedReader(   new InputStreamReader(socket.getInputStream() )   );//读取服务器的信息
		BufferedWriter bw = new  BufferedWriter( new OutputStreamWriter(socket.getOutputStream())  );
		//向服务器端发送信息
		bw.write("nihao , i am client");
		bw.newLine();
		bw.flush();//强制发送
		
		String str = in.readLine();
		System.out.println(str);
	   } finally {
		   socket.close();
	   }
	
		
		// Socket[] socket = new Socket[length];
		// for(int i = 0;i<length;i++){
		// socket[i] = new Socket(host,port);
		// System.out.println("第"+(i+1)+"次连接成功！");
		// }
		// Thread.sleep(3000);
		// for(int i=0;i<length;i++){
		// socket[i].close();
		// }
	}
}