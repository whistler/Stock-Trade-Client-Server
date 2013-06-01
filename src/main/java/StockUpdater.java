import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
/* Updates stock prices every two minutes */
public class StockUpdater implements Runnable{
	public void run()
	{
		try {
			updateStockPrices();
			Thread.sleep(2*60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void updateStockPrices() throws SQLException
	{
		String stockList = getStockList();
		String stockPriceList = downloadStocks(stockList);
		String lines[] = stockPriceList.split("\n");
		for(int i=0;i<lines.length;i++)
		{
			updateStockPrice(lines[i]);
		}
	}
	
	private static void updateStockPrice(String csvLine)
	{
		//String matches[] = csvLine.matches("\"(.*)\",(.*)");
		System.out.println(csvLine);
	}
	
	private static String getStockList() throws SQLException
	{
		String stockList = "";
		List<Stock> stocks = TradeServer.stockDao.queryForAll();
		stockList = stocks.get(0).getSymbol();
		for(int i = 1; i<stocks.size();i++)
		{
			stockList = stockList + "," + stocks.get(i).getSymbol();
		}
		return stockList;
	}
	
	// Fetch new stock prices and update database:
	// https://code.google.com/p/yahoo-finance-managed/wiki/csvQuotesDownload
	// curl http://download.finance.yahoo.com/d/quotes.csv\?s\=T,GOOG\&f\=sl1
	private static String downloadStocks(String stockList)
	{
		String output = "";
		try{
			URL yahooFinance = new URL("http://download.finance.yahoo.com/d/quotes.csv\\?s\\=T,GOOG&f\\=sl1");
			URLConnection connection = yahooFinance.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
                                    connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				output = output + inputLine + "\n";
			in.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return output;
	}
}
