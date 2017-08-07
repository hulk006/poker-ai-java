package edu.ntnu.texasai.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
//链接到一个数据库 data/data.h2.db
public class PersistenceManager {
    private final Connection connection;

    public PersistenceManager() {
        connection = createConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection createConnection() {
        try {
            Class.forName("org.h2.Driver");//Class.forName方法的作用,就是初始化给定的类.
            //而我们给定的MySQL的Driver类中,它在静态代码块中通过JDBC的DriverManager注册了一下驱动.
            //我们也可以直接使用JDBC的驱动管理器注册mysql驱动.从而代替使用Class.forName. 
            return DriverManager.getConnection("jdbc:h2:data/data", "sa", ""); //加载数据库文件 数据库，用户名，密码
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
