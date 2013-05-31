import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;


public class TradeServer {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		ConnectionSource connectionSource = 
				new JdbcConnectionSource("jdbc:sqlite:stock_trade.db");
	 	
		Dao<Stock, String> stockDao =
		  DaoManager.createDao(connectionSource, Stock.class);
		Dao<User, String> userDao =
		  DaoManager.createDao(connectionSource, User.class);
		
		User user = new User();
		user.setBalance(1000);
		user.setUsername("Ibrahim");
		userDao.createIfNotExists(user);
		
	}
	
	public float query(String ticker_name)
	{
		return 0;
	}

	public String buy(String ticker_name, int num_stocks)
	{
		return "SUCCESS";
	}
	
	public String sell(String ticker_name, int num_stocks)
	{
		return "SUCCESS";
	}
	
	public String update(String ticker_name, float price)
	{
		return "SUCCESS";
	}
}
