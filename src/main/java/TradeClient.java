import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import api.TradeApi;

public class TradeClient {

<<<<<<< HEAD
	private static String clientname ;
=======
>>>>>>> 2e3249a6028734662298f27e7cfc68afd62f0d06
   	private static final String REGISTRYHOST = "localhost";
    private static int REGISTRYPORT = 1099;
    private static final String SERVERPATH = "rmi://localhost/tradeserver";
    private static Scanner sc;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
<<<<<<< HEAD
		
		System.out.println("TRADE CLIENT");
		System.out.println("Please enter your username:");
		sc = new Scanner(System.in);
		
		getUsernameEntry();

		System.out.println("Hello " + clientname);

		printOptions();
		
=======
>>>>>>> 2e3249a6028734662298f27e7cfc68afd62f0d06
		try {
			//Registry registry = LocateRegistry.getRegistry(REGISTRYHOST, REGISTRYPORT);
			TradeApi server = (TradeApi) Naming.lookup(SERVERPATH);
			System.out.println(server.query("Foo"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		sc.close();
	}
	
	private static void printOptions() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("| Available options: ");
		System.out.println("|\t query ");
		System.out.println("|\t buy ");
		System.out.println("|\t sell ");
		System.out.println("----------------------------------------------------------------");
	}
	
	private static void getUsernameEntry() {
		if(sc.hasNext())
		{
			clientname = sc.next();
			System.out.println(clientname);
		}
	}

}
