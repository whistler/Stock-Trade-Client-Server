import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
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
			System.err.println("Server Error.\n" + ex.getMessage());
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
			System.out.println("<Stock Symbol> <Quantity> ");
			String stockSymbol = sc.next();
			int quantity = sc.nextInt();
			System.out.println(server.sell(stockSymbol, quantity, clientname));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		} catch (InputMismatchException ime) {
			System.err.println("Input mismatch. " + ime.getMessage());
		} catch (Exception otherException) {
			System.out.println("Could not process request"
					+ otherException.getMessage());
		}
	}

	private static void buyProcess() {
		try {
			System.out.println("<Stock Symbol> <Quantity> ");
			String stockSymbol = sc.next();
			int quantity = sc.nextInt();
			System.out.println(server.buy(stockSymbol, quantity, clientname));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		} catch (InputMismatchException ime) {
			System.err.println("Input mismatch. " + ime.getMessage());
		} catch (Exception otherException) {
			System.out.println("Could not process request"
					+ otherException.getMessage());
		}
	}

	private static void queryProcess() {
		try {
			System.out.println("<Stock Symbol>");
			String stockSymbol = sc.next();
			System.out.println(server.query(stockSymbol));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		}
	}

	private static void printOptions() {
		System.out.println();
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
		if (!sc.nextLine().trim().equals("")) {
			System.out.println("Please enter single word username.");
		} else {
			try {
				System.out.println(server.identify(clientname));
			} catch (RemoteException re) {
				System.err.println("Server returned error. " + re.getMessage());
			}
		}
	}
}
