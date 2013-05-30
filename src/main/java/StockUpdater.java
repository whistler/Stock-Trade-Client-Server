import java.lang.*;
/* Updates stock prices every two minutes */
public class StockUpdater implements Runnable{
	public void run()
	{
		//TODO: Fetch new stock prices and update database:
		// https://code.google.com/p/yahoo-finance-managed/wiki/csvQuotesDownload
		// curl http://download.finance.yahoo.com/d/quotes.csv\?s\=T,GOOG\&f\=sl1
		
		try {
			Thread.sleep(2*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
