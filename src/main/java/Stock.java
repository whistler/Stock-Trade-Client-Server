
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Stocks")
public class Stock {
	@DatabaseField(id = true)
	private String symbol;
	@DatabaseField
	private float price;
	@DatabaseField
	private int shares;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public String print() {
		if (getPrice() > 0)
			return getSymbol() + " " + getPrice();
		else
			return "Stock " + getSymbol() + " does not exist";
	}
}
