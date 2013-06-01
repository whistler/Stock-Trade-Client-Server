import java.rmi.Naming;
import java.util.Scanner;

import api.TradeApi;

public class TradeClient {

	private static String clientname ;
    private static final String SERVERPATH = "rmi://localhost/tradeserver";
    private static Scanner sc;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("TRADE CLIENT");
		System.out.println("Please enter your username:");
		sc = new Scanner(System.in);
		
		getUsernameEntry();

		System.out.println("Hello " + clientname);

		printOptions();
		
		try {
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
