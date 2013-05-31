import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import api.TradeApi;

public class TradeClient {

	private String clientname ;
   	private static final String REGISTRYHOST = "localhost";
    private static int REGISTRYPORT = 1099;
    private static final String SERVERPATH = "rmi://localhost/tradeserver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry registry = LocateRegistry.getRegistry(REGISTRYHOST, REGISTRYPORT);
			TradeApi server = (TradeApi) Naming.lookup(SERVERPATH);
			System.out.println(server.query("Foo"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
