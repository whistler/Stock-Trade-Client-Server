import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import api.TradeApi;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;


public class TradeServer extends UnicastRemoteObject implements TradeApi {
	private static final int PORT = 1099;
	private static final String REGISTRYNAME = "tradeserver";
	private static final String DATASOURCE = "jdbc:sqlite:stock_trade.db";
	private static Registry registry;
	/**
	 * @param args
	 * @throws SQLException 
	 */
	
	public TradeServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws SQLException {
		ConnectionSource connectionSource = 
				new JdbcConnectionSource(DATASOURCE);
	 	
		Dao<Stock, String> stockDao =
		  DaoManager.createDao(connectionSource, Stock.class);
		Dao<User, String> userDao =
		  DaoManager.createDao(connectionSource, User.class);
		
		User user = new User();
		user.setBalance(1000);
		user.setUsername("Ibrahim");
		userDao.createIfNotExists(user);
		
		try{
			//Registry registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
			//registry = LocateRegistry.getRegistry();
			try{
			registry = LocateRegistry.createRegistry(PORT);
			} catch (ExportException ex) {
				registry = LocateRegistry.getRegistry();
			}
			try{
				registry.lookup(REGISTRYNAME);
				registry.unbind(REGISTRYNAME);
				registry.bind(REGISTRYNAME, new TradeServer());
			} catch (NotBoundException ex)
			{
				registry.bind(REGISTRYNAME, new TradeServer());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public float query(String ticker_name) throws RemoteException
	{
		return 140;
	}

	public String buy(String ticker_name, int num_stocks) throws RemoteException
	{
		return "SUCCESS";
	}

	public String sell(String ticker_name, int num_stocks) throws RemoteException
	{
		return "SUCCESS";
	}
	
	public String update(String ticker_name, float price) throws RemoteException
	{
		return "SUCCESS";
	}
}
