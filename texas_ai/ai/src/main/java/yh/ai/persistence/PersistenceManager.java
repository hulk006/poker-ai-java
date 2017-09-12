package    yh.ai.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
//链接到一个数据库 data/data.h2.db
/** *//**
 * 使用JDBC连接数据库MySQL的过程
 * DataBase：data， ；
 */
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
            //第一步：加载h2的JDBC的驱动
            Class.forName("org.h2.Driver");//Class.forName方法的作用,就是初始化给定的类.
            //而我们给定的MySQL的Driver类中,它在静态代码块中通过JDBC的DriverManager注册了一下驱动.
            //我们也可以直接使用JDBC的驱动管理器注册mysql驱动.从而代替使用Class.forName. 
          //第二步：创建与MySQL数据库的连接类的实例
            System.out.println(" 连接数据库文件jdbc:h2:data/data "+System.getProperty("user.dir"));
            return DriverManager.getConnection("jdbc:h2:data/data", "sa", ""); //加载数据库文件 数据库，用户名，密码
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
