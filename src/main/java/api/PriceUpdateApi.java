package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PriceUpdateApi extends Remote {
	public String update(String ticker_name, float price)
			throws RemoteException;
}
