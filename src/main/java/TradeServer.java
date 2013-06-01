import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import api.PriceUpdateApi;
import api.TradeApi;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;


public class TradeServer extends UnicastRemoteObject implements TradeApi, PriceUpdateApi {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PORT = 1099;
	private static final String REGISTRYNAME = "tradeserver";
	private static final String DATASOURCE = "jdbc:sqlite:stock_trade.db";
	private static Registry registry;
	public static Dao<Stock, String> stockDao;
	public static Dao<User, String> userDao;

	/**
	 * @param args
	 * @throws SQLException 
	 */
	
	public TradeServer() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) throws SQLException {
		
		createDataAccessObjects();

		try{
			findOrCreateRegistry();
			bindRegistry();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		StockUpdater stockUpdater = new StockUpdater();
		Thread stockUpdaterThread = new Thread(stockUpdater);
		stockUpdaterThread.run();
	}
	
	private static void createDataAccessObjects() throws SQLException
	{
		  ConnectionSource connectionSource = 
				new JdbcConnectionSource(DATASOURCE);
		  stockDao = DaoManager.createDao(connectionSource, Stock.class);
		  userDao = DaoManager.createDao(connectionSource, User.class);
	}
	
	private static void findOrCreateRegistry() throws RemoteException
	{
		try{
			registry = LocateRegistry.createRegistry(PORT);
		} catch (ExportException ex) {
			registry = LocateRegistry.getRegistry();
		}
	}
	
	private static void bindRegistry() throws AccessException, RemoteException, AlreadyBoundException
	{
		try{
			// if already registry bound unbound and rebind 
			registry.lookup(REGISTRYNAME);
			registry.unbind(REGISTRYNAME);
			registry.bind(REGISTRYNAME, new TradeServer());
		} catch (NotBoundException ex)
		{
			// if not bound previous then bind
			registry.bind(REGISTRYNAME, new TradeServer());
		}
	}
	
	public String query(String ticker_name) throws RemoteException
	{
		ticker_name = ticker_name.toUpperCase();
		try {
			Stock stock = stockDao.queryForId(ticker_name);
			if (stock != null)
			{
				return stock.print();
			}
			else {
				StockUpdater.updateStockPrices(ticker_name);
				stock = stockDao.queryForId(ticker_name);
				return stock.print();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR: There was a problem trying to find the stock price";
		}
	}

	public String buy(String ticker_name, int num_stocks, String user) throws RemoteException
	{
		return "SUCCESS";
	}

	public String sell(String ticker_name, int num_stocks, String user) throws RemoteException
	{
		return "SUCCESS";
	}
	
	public String update(String ticker_name, float price) throws RemoteException
	{
		return "SUCCESS";
	}

	public String indentify(String username) throws RemoteException {
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
