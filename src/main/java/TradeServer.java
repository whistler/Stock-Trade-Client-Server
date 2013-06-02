

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class TradeServer extends UnicastRemoteObject implements TradeApi,
		PriceUpdateApi {
	private static final long serialVersionUID = 1L;
	private static final int PORT = 1099;
	private static final String REGISTRYNAME = "tradeserver";
	private static final String DATASOURCE = "jdbc:sqlite:stock_trade.db";
	private static Registry registry;
	public static Dao<Stock, String> stockDao;
	public static Dao<User, String> userDao;
	public static Dao<Owns, String> ownsDao;

	/**
	 * @param args
	 * @throws SQLException
	 */

	public TradeServer() throws RemoteException {
		super();
	}

	public static void main(String[] args) throws SQLException {

		createDataAccessObjects();

		try {
			findOrCreateRegistry();
			bindRegistry();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		StockUpdater stockUpdater = new StockUpdater();
		Thread stockUpdaterThread = new Thread(stockUpdater);
		stockUpdaterThread.run();
	}

	private static void createDataAccessObjects() throws SQLException {
		ConnectionSource connectionSource = new JdbcConnectionSource(DATASOURCE);
		stockDao = DaoManager.createDao(connectionSource, Stock.class);
		userDao = DaoManager.createDao(connectionSource, User.class);
		ownsDao = DaoManager.createDao(connectionSource, Owns.class);
	}

	private static void findOrCreateRegistry() throws RemoteException {
		try {
			registry = LocateRegistry.createRegistry(PORT);
		} catch (ExportException ex) {
			registry = LocateRegistry.getRegistry();
		}
	}

	private static void bindRegistry() throws AccessException, RemoteException,
			AlreadyBoundException {
		try {
			// if already registry bound unbound and rebind
			registry.lookup(REGISTRYNAME);
			registry.unbind(REGISTRYNAME);
			registry.bind(REGISTRYNAME, new TradeServer());
		} catch (NotBoundException ex) {
			// if not bound previous then bind
			registry.bind(REGISTRYNAME, new TradeServer());
		}
	}

	public String query(String ticker_name) throws RemoteException {
		ticker_name = ticker_name.toUpperCase();
		try {
			Stock stock = stockDao.queryForId(ticker_name);
			if (stock != null) {
				return stock.print();
			} else {
				StockUpdater.updateStockPrices(ticker_name);
				stock = stockDao.queryForId(ticker_name);
				return stock.print();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR: There was a problem trying to find the stock price";
		}
	}

	public String buy(String tickerName, int numStocks, String username)
			throws RemoteException {
		try {
			Stock stock = stockDao.queryForId(tickerName);
			if (stock == null)
				return "ERROR: "
						+ tickerName
						+ " does not exist in database, try to query for it first";
			User user = userDao.queryForId(username);
			if (user == null)
				return "ERROR: user " + username + " was not found";
			if (numStocks > stock.getShares())
				return "The maximum shares available for buying are "
						+ stock.getShares();
			float cost = numStocks * stock.getPrice();
			if (cost > user.getBalance())
				return "You do not have enough money in you account";
			return Owns.buy(stock, user, numStocks);
		} catch (SQLException e) {
			return "ERROR: There was a problem buying";
		}
	}

	public String sell(String tickerName, int numStocks, String username)
			throws RemoteException {
		try {
			Stock stock = stockDao.queryForId(tickerName);
			if (stock == null)
				return "ERROR: "
						+ tickerName
						+ " does not exist in database, try to query for it first";
			User user = userDao.queryForId(username);
			if (user == null)
				return "ERROR: user " + username + " was not found";
			return Owns.sell(stock, user, numStocks);
		} catch (SQLException e) {
			return "ERROR: There was a problem buying";
		}
	}

	public String update(String tickerName, float price) throws RemoteException {
		try {
			Stock stock = stockDao.queryForId(tickerName);
			if (stock == null)
				return "Stock symbol " + tickerName + " not found";
			if (price < 0)
				return "Price is not valid";
			stock.setPrice(price);
			stockDao.update(stock);
			return "Price for " + tickerName + " updated to " + price;
		} catch (Exception e) {
			return "Could not update price";
		}
	}

	public String identify(String username) throws RemoteException {
		User user = new User();
		user.setBalance(1000);
		user.setUsername(username);
		try {
			userDao.createIfNotExists(user);
			return "SUCCESS: " + username + " logged in";
		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR: Could not identify";
		}
	}
}
