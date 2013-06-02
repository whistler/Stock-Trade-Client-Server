import java.sql.SQLException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Own")
public class Owns {
	@DatabaseField
	private String symbol;
	@DatabaseField
	private String username;
	@DatabaseField
	private int amount;
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public static String buy(Stock stock, User user, int qty) throws SQLException
	{
//		return  "UPDATE Stocks SET shares=shares-" + qty + " WHERE symbol='" + symbol + "';\n" +
//				"UPDATE Users SET balance=balance-"+ qty*price + " WHERE username='" + user + "';\n" +
//				"INSERT INTO Own (username,symbol,amount) values ('" + user +"','" + symbol + "', " +
//						"(SELECT amount FROM Own WHERE username='" + user + "' AND symbol='" + symbol + "'));\n" +
//				"UPDATE Own SET amount=0 WHERE amount IS NULL;\n" +
//				"UPDATE Own SET amount = amount + " + qty + " WHERE username = '" + user + "' AND symbol = '" + symbol + "';\n";
//		return  "UPDATE Stocks SET shares=shares-" + qty + " WHERE symbol='" + symbol + "';\n" +
//		"UPDATE Users SET balance=balance-"+ qty*price + " WHERE username='" + user + "';\n" +
//		"INSERT INTO Own (username,symbol,amount) values ('" + user +"','" + symbol + "', 0) ON DUPLICATE KEY UPDATE amount = amount + " + qty +"\n"";
		Dao<Stock, String> stockDao = TradeServer.stockDao;
		Dao<User, String> userDao = TradeServer.userDao;
		Dao<Owns, String> ownsDao = TradeServer.ownsDao;
		
		Owns owns = ownsDao.queryBuilder().where().eq("username", user.getUsername()).and().eq("symbol", stock.getSymbol()).queryForFirst();
		boolean createOwns = false;
		if(owns == null)
		{
			createOwns = true;
			owns = new Owns();
			owns.setAmount(qty);
			owns.setSymbol(stock.getSymbol());
			owns.setUsername(user.getUsername());
		} else {
			owns.setAmount(owns.getAmount()+qty);
		}
		stock.setShares(stock.getShares()-qty);
		user.setBalance(user.getBalance()-(qty*stock.getPrice()));
		
		stockDao.update(stock);
		userDao.update(user);
		if(createOwns) ownsDao.create(owns);
		else ownsDao.updateRaw("UPDATE Own SET amount=" + owns.getAmount() + " WHERE username='" + user.getUsername() + "' AND symbol='" + stock.getSymbol() + "';");
		return "You now own " + owns.getAmount() + " shares of " + stock.getSymbol();
	}
	
	public static String sell(String symbol, String user, int qty)
	{
		return "BEGIN TRANSACTION;" +
				"update Own set amount=amount-qty where username=user AND symbol=symbol " +
				"COMMIT;";
	}
}
