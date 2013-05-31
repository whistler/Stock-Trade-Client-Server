import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TradeClient {

	private String clientname ;
   	private static final String REGISTRYHOST = "localhost";
    private static int REGISTRYPORT = 1099;
    private static final String SERVERPATH = "rmi://localhost/myserver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry registry = LocateRegistry.getRegistry(REGISTRYHOST, REGISTRYPORT);
			System.setSecurityManager(new RMISecurityManager());
			TradeServer server = (TradeServer) Naming.lookup(SERVERPATH);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
