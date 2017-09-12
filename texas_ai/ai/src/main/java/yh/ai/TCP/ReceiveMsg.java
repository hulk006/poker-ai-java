package yh.ai.TCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.inject.Guice;
import com.google.inject.Injector;

import yh.ai.controller.PokerController;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.dependencyinjection.TexasModule;

public class ReceiveMsg {
	
	final static int port = 2024;
    public static void main(String[] args) {
        try {
            //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            ServerSocket serverSocket=new ServerSocket(port);
            Socket socket=null;
            //记录客户端的数量
            int count = 0;
            System.out.println("***服务器即将启动，等待客户端的连接***");
            //循环监听等待客户端的连接
            while(true)
            {
                //调用accept()方法开始监听，等待客户端的连接
                socket = serverSocket.accept();
                //创建一个新的线程
                ReceiveThread ReceiveThread=new ReceiveThread(socket);
               //启动线程
                ReceiveThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
