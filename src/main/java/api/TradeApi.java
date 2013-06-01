package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TradeApi extends Remote {
	public String indentify(String user) throws RemoteException;
	public String query(String ticker_name) throws RemoteException;
	public String buy(String ticker_name, int num_stocks, String user) throws RemoteException;
	public String sell(String ticker_name, int num_stocks, String user) throws RemoteException;
}
