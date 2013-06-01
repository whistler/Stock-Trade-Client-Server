import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Stocks")
public class Owns {
	@DatabaseField(id = true)
	private String symbol;
	@DatabaseField(id = true)
	private String username;
	@DatabaseField
	private int amount;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public static String buy(String symbol, String user, int qty)
	{
		return "BEGIN TRANSACTION;\n" +
				"UPDATE Stocks SET shares=shares-" + qty + "WHERE symbol='MSFT';\n" +
				"INSERT INTO Own (username,symbol) values ('Ibrahim','MSFT');\n" +
				"UPDATE Own SET amount=0 WHERE amount IS NULL;\n" +
				"UPDATE Own SET amount = amount + 50 WHERE username = 'Ibrahim' AND symbol = 'MSFT';\n" +
				"COMMIT;";
	}
	
	public static String sell(String symbol, String user, int qty)
	{
		return "BEGIN TRANSACTION;" +
				"update Own set amount=amount-qty where username=user AND symbol=symbol " +
				"COMMIT;";		
	}
}
