

import java.rmi.Remote;
import java.rmi.RemoteException;

// API to update price for stock on the server
public interface PriceUpdateApi extends Remote {
	public String update(String ticker_name, float price)
			throws RemoteException;
}
