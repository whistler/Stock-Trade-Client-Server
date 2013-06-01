import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import api.TradeApi;

public class TradeClient {

	private static String clientname;
	private static final String SERVERPATH = "rmi://localhost/tradeserver";
	private static Scanner sc;
	private static TradeApi server;

	public static void main(String[] args) {

		try {
			server = (TradeApi) Naming.lookup(SERVERPATH);
		} catch (Exception ex) {
			System.err.println(" Server Error. " + ex.getMessage());
			System.exit(0);
		}

		Boolean quitFlag = false;
		sc = new Scanner(System.in);

		System.out.println("TRADE CLIENT");
		System.out.println("Please enter your username:");

		getUsernameEntry();
		System.out.println("Hello " + clientname);

		while (true) {
			printOptions();

			String inputString = sc.next().toLowerCase().trim();
			
			if (!sc.nextLine().trim().equals("")) {
				System.out.println("Incorrect Command Format.");
				continue;
			} else {

				switch (inputString) {
				case "quit":
					quitFlag = true;
					break;

				case "query":
					queryProcess();
					break;

				case "buy":
					buyProcess();
					break;

				case "sell":
					sellProcess();
					break;

				default:
					System.out
					.println("\nCommand was not recognized, please try again\n");
				}
			}

			if (quitFlag) {
				break;
			}
		}
		sc.close();
		System.exit(0);
	}

	private static void sellProcess() {
		try {
			System.out.println("sell");
			System.out.println(server.sell("Foo", 5, clientname));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		}
	}

	private static void buyProcess() {
		try {
			System.out.println("buy");
			System.out.println(server.buy("Foo", 5, clientname));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		}
	}

	private static void queryProcess() {
		try {
			System.out.println("query");
			System.out.println(server.query("Foo"));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		}
	}


	private static void printOptions() {
		System.out
		.println("----------------------------------------------------------------");
		System.out.println("| Available options: ");
		System.out.println("|\t query ");
		System.out.println("|\t buy ");
		System.out.println("|\t sell ");
		System.out.println("|\t quit ");
		System.out
		.println("----------------------------------------------------------------");
	}

	private static void getUsernameEntry() {
		clientname = sc.next().toLowerCase().trim();
		try {
			System.out.println(server.identify(clientname));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		}
	}
}
