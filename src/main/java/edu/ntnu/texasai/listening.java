package edu.ntnu.texasai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class listening {
	public static void main(String args[]) throws IOException
	{
	 String c;
	 // 使用 System.in 创建 BufferedReader 
	 BufferedReader br = new BufferedReader(new 
	                    InputStreamReader(System.in));//建立从键盘输入的
	 System.out.println("输入字符, 按下 'q' 键退出。");
	 // 读取字符
	 do {
	    c =  br.readLine();//读取一行返回字符串 read（）读取一个字符
	    System.out.println(c);
	 } while(c != "end");
	}
}
