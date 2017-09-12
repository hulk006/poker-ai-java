package yh.ai;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
//这个class 用来利用H2的数据库来，在data文件夹下，建立一个student的数据库文件，具有创建数据库和在数据库中查找的功能
public class testJDBC {
	public static Connection getConnection() throws SQLException, java.lang.ClassNotFoundException {
		// 第一步：加载H2的驱动
		Class.forName("org.h2.Driver");

		// 取得连接的url,能访问MySQL数据库的用户名,密码；studentinfo：数据库名
		String url = "jdbc:h2:data/student"; // dbc:h2:+文件夹+数据库的名称
		String username = "root";
		String password = "root";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static void main(String args[])  throws Exception
	{
		
			// 第三步：获取连接类实例con，用con创建Statement对象类实例 sql_statement
			Connection con = getConnection();
			java.sql.Statement sql_statement = con.createStatement();
			// 创建表CREATE TABLE IF NOT EXISTS
			StringBuffer sb = new StringBuffer(" create table  if not exists stuent(");// 创建了一个名字叫 student 的表
			sb.append("id int ,");//表头 的数据类型
			sb.append("name varchar(32),");
			sb.append("age int ");
			sb.append(")");
			String ddl = sb.toString();
			sql_statement.executeUpdate(ddl);// 创建了表头

			for (int i = 10; i < 100; i++)// 在表里面插入数据
			{
				StringBuffer sb1 = new StringBuffer(" insert into stuent(  id, name, age) values(");
				sb1.append(String.valueOf(i ));
				sb1.append(", 'name ' , ");
				sb1.append( String.valueOf(18+i ) );
				sb1.append(" ) ");
				String insert = sb1.toString();
				sql_statement.executeUpdate(insert);// 执行插入语句
			}
			String query = "select  *  from stuent";
			ResultSet result = sql_statement.executeQuery(query);

			// 对获得的查询结果进行处理，对Result类的对象进行操作
			while (result.next()) {
				int id =  result.getInt("id");
				String name = result.getString("name");
				int age = result.getInt("age");
				// 取得数据库中的数据
				System.out.println("=========" + String.valueOf(id) + " " +name + String.valueOf(age) );
			}
			// 关闭连接和声明
			( sql_statement).close();
			con.close();

		} 
	}


