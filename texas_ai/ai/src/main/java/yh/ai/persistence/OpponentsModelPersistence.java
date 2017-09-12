package    yh.ai.persistence;

import    yh.ai.model.opponentmodeling.ContextAction;
import    yh.ai.model.opponentmodeling.ContextAggregate;
import    yh.ai.model.opponentmodeling.ModelResult;
import    yh.ai.utils.Logger;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
//主要使用来查找对手的模型的主类
public class OpponentsModelPersistence {
    private static final String TABLE_OPPONENTS_MODEL = "Opponents";

    private final PersistenceManager persistenceManager;
    private final Logger logger;

    @Inject
    public OpponentsModelPersistence(final PersistenceManager persistenceManager, final Logger logger) {
        this.persistenceManager = persistenceManager;
        this.logger = logger;

        init();
    }
//persist 是 save 的意思
    public void persist(ContextAggregate contextAggregate) {
    	//Java的异常处理机制(try…catch…finally) try 后面是主代码
        try {
        	//contextAction 中的信息替换？
            String insert = "INSERT INTO " + TABLE_OPPONENTS_MODEL + " VALUES(?,?,?,?,?,?,?,?,?)";
          //第三步：获取连接类实例persistenceManager.getConnection()，用persistenceManager.getConnection()创建Statement对象类实例
            PreparedStatement statement = persistenceManager.getConnection().prepareStatement(insert);
            ContextAction contextAction = contextAggregate.getContextAction();
            //设置？ = 的值
            statement.setInt(1, contextAction.getPlayer().getNumber());
            statement.setString(2, contextAction.getBettingDecision().toString());
            statement.setString(3, contextAction.getBettingRoundName().toString());
            statement.setString(4, contextAction.getContextRaises().toString());
            statement.setString(5, contextAction.getContextPlayers().toString());
            statement.setString(6, contextAction.getContextPotOdds().toString());
            statement.setInt(7, contextAggregate.getNumberOfOccurrences());
            statement.setDouble(8, contextAggregate.getHandStrengthAverage());
            statement.setDouble(9, contextAggregate.getDeviation());
            //5 打印SQL语句
             //getPreparedSQL();
  
            
            
            statement.executeUpdate(); //更新与关闭
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
    
//检索得到模型结果信息
    public ModelResult retrieve(ContextAction contextAction) {
        String query = "SELECT * FROM " + TABLE_OPPONENTS_MODEL + " WHERE player = ? AND decision = ? AND " +
                "roundname = ? AND raises = ? AND playercount = ? AND potodds = ?";

        try {
            PreparedStatement statement = persistenceManager.getConnection().prepareStatement(query);
            statement.setInt(1, contextAction.getPlayer().getNumber());
            statement.setString(2, contextAction.getBettingDecision().toString());
            statement.setString(3, contextAction.getBettingRoundName().toString());
            statement.setString(4, contextAction.getContextRaises().toString());
            statement.setString(5, contextAction.getContextPlayers().toString());
            statement.setString(6, contextAction.getContextPotOdds().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new ModelResult(result.getDouble("handstrength"), result.getDouble("deviation"),
                        result.getInt("occurences"));
            } else {
                return new ModelResult(0, 0, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void print() {
        String query = "SELECT * FROM " + TABLE_OPPONENTS_MODEL + " ORDER BY player, decision, roundname, " +
                "raises, playercount, potodds";
        try {
            PreparedStatement statement = persistenceManager.getConnection().prepareStatement(query);//查询
            ResultSet result = statement.executeQuery();//执行查询
            logger.log("P\tDecision\tRound\tRaises\t#Players\tPO\tOccurences\tHS\tDeviation");//打印表头信息
            DecimalFormat f = new DecimalFormat("##.0000");//格式化
            while (result.next()) //遍历查询结果
            {
                String handstrength = f.format(result.getDouble("handstrength"));
                String deviation = f.format(result.getDouble("deviation"));
                //查找信息
                logger.log(result.getInt("player") + "\t" + result.getString("decision") + "\t" + result.getString
                        ("roundname") + "\t" + result.getString("raises") + "\t" + result.getString("playercount") +
                        "\t" + result.getString("potodds") + "\t" + result.getString("occurences") + "\t" +
                        handstrength + "\t" + deviation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void clear() {
        String query = "TRUNCATE TABLE " + TABLE_OPPONENTS_MODEL;
        try {
            Statement statement = persistenceManager.getConnection().createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    private void init() {
        try {
            Statement statement = persistenceManager.getConnection().createStatement();
            //statement.execute("DROP TABLE "+TABLE_OPPONENTS_MODEL);
            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_OPPONENTS_MODEL + "(player integer," +
                    "decision VARCHAR_IGNORECASE,roundname VARCHAR_IGNORECASE, raises VARCHAR_IGNORECASE, " +
                    "playercount VARCHAR_IGNORECASE, potodds VARCHAR_IGNORECASE, occurences integer, " +
                    "handstrength double, deviation double)";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
