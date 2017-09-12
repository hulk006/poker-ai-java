package yh.ai;

/**
 * Created by YangHao on 2017/8/15
 * 服务器线程处理类
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//继承Thread 类的，用于处理多线程
public class ServerThread extends Thread {
	// 和本线程相关的Socket
	Socket socket = null;

	// 构造方法传入 与客户端的链接
	public ServerThread(Socket socket) {
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
			// 循环读取客户端的信息
			System.out.println("客户端的消息为：" + info);
			
			
			
			
			
			socket.shutdownInput();// 关闭输入流
			// 服务器的输出部分××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
			os = socket.getOutputStream(); // 获取输出流，响应客户端的请求
			pw = new BufferedWriter(new OutputStreamWriter(os));
			pw.write("欢迎您！现在时间：" + new java.util.Timer().toString());
			pw.newLine();
			pw.flush();// 调用flush()方法将缓冲输出

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