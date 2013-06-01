import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;
import api.PriceUpdateApi;

public class PriceUpdateClient {

	private static final String SERVERPATH = "rmi://localhost/tradeserver";
	private static Scanner usc;
	private static PriceUpdateApi server;

	public static void main(String[] args) {
		try {
			server = (PriceUpdateApi) Naming.lookup(SERVERPATH);
		} catch (Exception ex) {
			System.err.println("Server Error.\n" + ex.getMessage());
			System.exit(0);
		}

		Boolean quitFlag = false;
		usc = new Scanner(System.in);

		System.out.println("PRICE UPDATE CLIENT");

		while (true) {
			printOptions();

			String inputString = usc.next().toLowerCase().trim();

			if (!usc.nextLine().trim().equals("")) {
				System.out.println("Incorrect Command Format.");
				continue;
			} else {

				switch (inputString) {
				case "quit":
					quitFlag = true;
					break;

				case "update":
					updateProcess();
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
		
		System.out.println("Logged out");
		usc.close();
		System.exit(0);

	}

	private static void updateProcess() {
		try {
			System.out.println("<Stock Symbol> <Price> ");
			String stockSymbol = usc.next();
			float quantity = usc.nextFloat();
			System.out.println(server.update(stockSymbol, quantity));
		} catch (RemoteException re) {
			System.err.println("Server returned error. " + re.getMessage());
		} catch (InputMismatchException ime) {
			System.err.println("Input mismatch. " + ime.getMessage());
		} catch (Exception otherException) {
			System.out.println("Could not process request"
					+ otherException.getMessage());
		}
	}

	private static void printOptions() {
		System.out.println();
		System.out
				.println("----------------------------------------------------------------");
		System.out.println("| Available options: ");
		System.out.println("|\t update ");
		System.out.println("|\t quit ");
		System.out
				.println("----------------------------------------------------------------");
	}

}
