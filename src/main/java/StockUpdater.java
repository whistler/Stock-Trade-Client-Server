import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* Updates stock prices every two minutes */
public class StockUpdater implements Runnable{
	private static final int INITIALSHARES = 1000;
	public void run()
	{
		try {
			updateAllStockPrices();
			Thread.sleep(2*60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateAllStockPrices() throws SQLException
	{
		String stockList = getStockList();
		updateStockPrices(stockList);
	}
	
	public static void updateStockPrices(String stockList) throws SQLException
	{
		String stockPriceList = downloadStocks(stockList);
		String lines[] = stockPriceList.split("\n");
		for(int i=0;i<lines.length;i++)
		{
			updateStockPrice(lines[i]);
		}
	}
	
	public static void updateStockPrice(String csvLine) throws SQLException
	{
		Pattern pattern = Pattern.compile("\"(.*)\",(.*)");
		Matcher matcher = pattern.matcher(csvLine);
		matcher.matches();
		String symbol = matcher.group(1);
		Float price = Float.parseFloat(matcher.group(2));
		Stock stock = TradeServer.stockDao.queryForId(symbol);
		if (stock == null)
		{
			stock = new Stock();
			stock.setSymbol(symbol);
			stock.setPrice(price);
			stock.setShares(INITIALSHARES);
			TradeServer.stockDao.create(stock);
		} else
		{
			stock.setPrice(price);
			TradeServer.stockDao.update(stock);
		}
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
			URL yahooFinance = new URL("http://download.finance.yahoo.com/d/quotes.csv\\?s\\="+ stockList + "&f\\=sl1");
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
