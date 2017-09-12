package yh.ai.persistence;

import yh.ai.model.cards.EquivalenceClass;
import yh.ai.utils.Logger;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//主要是形成一个手牌数据库
public class PreFlopPersistence {
	private static final String TABLE_EQUIVALENCE_NAME = "Equivalences";

	private final PersistenceManager persistenceManager;
	private final Logger logger;

	@Inject
	public PreFlopPersistence(final PersistenceManager persistenceManager, final Logger logger) {
		this.persistenceManager = persistenceManager;
		this.logger = logger;

		init();
	}

	public void persist(int numberOfPlayers, EquivalenceClass equivalenceClass, double percentage) {
		try {
			String insert = "INSERT INTO " + TABLE_EQUIVALENCE_NAME + " VALUES(?,?,?,?,?)";//建立一个插入的语句
			PreparedStatement statement = persistenceManager.getConnection().prepareStatement(insert);
			statement.setInt(1, numberOfPlayers);
			statement.setString(2, equivalenceClass.getNumber1().toString());
			statement.setString(3, equivalenceClass.getNumber2().toString());
			statement.setString(4, equivalenceClass.getType());
			statement.setDouble(5, percentage);//胜利的概率
			
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
	//在已有的表格中查找，输出概率wins
	public double retrieve(int numberOfPlayers, EquivalenceClass equivalenceClass) {
		String number1 = equivalenceClass.getNumber1().toString();
		String number2 = equivalenceClass.getNumber2().toString();
		String type = equivalenceClass.getType();
		String query = "SELECT wins FROM " + TABLE_EQUIVALENCE_NAME + " WHERE players = ? AND type = ? AND "
				+ "((number1 = ? AND number2 = ?) OR (number1 = ? AND number2 = ?))";// 一个字符串
		
		try {
			//PreparedStatement 可以提高sql的效率
			PreparedStatement statement = persistenceManager.getConnection().prepareStatement(query);// 执行sql里面的询问，获取sql的预编译对象
			statement.setInt(1, numberOfPlayers);
			statement.setString(2, type);
			statement.setString(3, number1);
			statement.setString(4, number2);
			statement.setString(5, number2);
			statement.setString(6, number1);
		//	System.out.println(query);
			ResultSet result = statement.executeQuery();//得到查找的结果；
			System.out.println(result);
			if (result.next()) {
				System.out.println("手牌的胜率为：" +result.getDouble("wins") );
				return result.getDouble("wins");
				
			} else {
				throw new RuntimeException("Probability not calculated for these parameters");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public void print() {
		String query = "SELECT * FROM " + TABLE_EQUIVALENCE_NAME;
		try {
			PreparedStatement statement = persistenceManager.getConnection().prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				logger.log(result.getInt("players") + "\t" + result.getString("number1") + "\t"
						+ result.getString("number2") + "\t" + result.getString("type") + "\t"
						+ result.getDouble("wins"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
//数据库中的初始化，如果TABLE_EQUIVALENCE_NAME 不存在就新建一个表格，如果存在就可以查找命令了
	private void init() {
		try
		{
			Statement statement = persistenceManager.getConnection().createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + TABLE_EQUIVALENCE_NAME + "(players integer,"
					+ "number1 VARCHAR_IGNORECASE,number2 VARCHAR_IGNORECASE, type VARCHAR_IGNORECASE, wins double)";
			statement.executeUpdate(query);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
}
