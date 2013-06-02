import java.sql.SQLException;
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

	public static String buy(Stock stock, User user, int qty)
			throws SQLException {
		Owns owns = TradeServer.ownsDao.queryBuilder().where()
				.eq("username", user.getUsername()).and()
				.eq("symbol", stock.getSymbol()).queryForFirst();
		boolean createOwns = false;
		if (owns == null) {
			createOwns = true;
			owns = new Owns();
			owns.setAmount(qty);
			owns.setSymbol(stock.getSymbol());
			owns.setUsername(user.getUsername());
		} else {
			owns.setAmount(owns.getAmount() + qty);
		}
		stock.setShares(stock.getShares() - qty);
		user.setBalance(user.getBalance() - (qty * stock.getPrice()));

		TradeServer.stockDao.update(stock);
		TradeServer.userDao.update(user);
		if (createOwns)
			TradeServer.ownsDao.create(owns);
		else
			TradeServer.ownsDao.updateRaw("UPDATE Own SET amount="
					+ owns.getAmount() + " WHERE username='"
					+ user.getUsername() + "' AND symbol='" + stock.getSymbol()
					+ "';");
		return "You now own " + owns.getAmount() + " shares of "
				+ stock.getSymbol();
	}

	public static String sell(Stock stock, User user, int qty)
			throws SQLException {
		Owns owns = TradeServer.ownsDao.queryBuilder().where()
				.eq("username", user.getUsername()).and()
				.eq("symbol", stock.getSymbol()).queryForFirst();
		if (owns == null)
			return "You do not own " + stock.getSymbol() + " stock";
		if (owns.getAmount() < qty)
			return "You can not sell more than you own. You own "
					+ owns.getAmount() + " shares of " + stock.getSymbol();

		stock.setShares(stock.getShares() + qty);
		user.setBalance(user.getBalance() + (qty * stock.getPrice()));
		owns.setAmount(owns.getAmount() - qty);

		TradeServer.stockDao.update(stock);
		TradeServer.userDao.update(user);
		TradeServer.ownsDao.updateRaw("UPDATE Own SET amount="
				+ owns.getAmount() + " WHERE username='" + user.getUsername()
				+ "' AND symbol='" + stock.getSymbol() + "';");

		return "You now own " + owns.getAmount() + " shares of "
				+ stock.getSymbol();
	}
}
