package server;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Users")
public class User {
	@DatabaseField(id = true)
	private String username;
	@DatabaseField
	private float balance;

	String getUsername() {
		return username;
	}

	void setUsername(String username) {
		this.username = username;
	}

	float getBalance() {
		return balance;
	}

	void setBalance(float balance) {
		this.balance = balance;
	}

}
