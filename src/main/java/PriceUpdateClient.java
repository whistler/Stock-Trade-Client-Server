import java.rmi.Naming;
import java.rmi.RMISecurityManager;


public class PriceUpdateClient {


	public static void main(String[] args) {
		try {
			System.setSecurityManager(new RMISecurityManager());
			TradeServer server = (TradeServer) Naming.lookup("rmi://localhost/myserver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
