package yh.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private int port = 1122; // 端口号
	private ServerSocket serverSocket;// 服务器的

	public Server() throws Exception {
		serverSocket = new ServerSocket(port, 3);// 建立一个服务器的套字节
		System.out.println("服务器启动!");
	}

	public void service() {
		while (true) {
			// 服务器一直保持运行
			Socket socket = null; // 一个socket 代表一个服务器与客户端的 TCP/IP 链接
			try {
				socket = serverSocket.accept();// 等待客户端的连接 返回一个socket
				if(socket!=null)
				{
					System.out.println("accept success "  );
					System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 读取服务器的信息
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					int a = in.read();
					in.read();
					System.out.println(a);
					String str = null;
					while( (str = in.readLine())!=null)
					{
						System.out.println( str );
					}


					System.out.println("客户端输入： " +  str );

					bw.write("ok I got it!");
					bw.flush();
				}
				else {
					System.out.println("accept failed "  );
					continue;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			finally {
				if (socket != null) {// 如果连接成功
					try {
						socket.close();// 关闭socket的链接
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		//Thread.sleep(60000 * 10);
		server.service();
	}
}