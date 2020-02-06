import java.sql.*;
import java.util.List;
import java.util.ArrayList;

class Main {

	private static final String TABLE_NAME = "users";
	private static final String DATABASE_FILENAME = "database.db";

	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
			String databaseURL = String.format("jdbc:sqlite:%s", DATABASE_FILENAME);
			Connection connection = DriverManager.getConnection(databaseURL);
			Statement statement = connection.createStatement();
			String query = String.format("SELECT * FROM %s", TABLE_NAME);
			try (ResultSet result = statement.executeQuery(query)) {
				List<User> users = new ArrayList<User>();
				while (result.next()) {
					User user = new User(result.getInt("id"), result.getString("name"), result.getInt("age"));
					users.add(user);
				}
				System.out.println(String.format("%-10s%-15s%-10s", "ID", "NAME", "AGE"));
				for (User user : users) {
					System.out.println(String.format("%-10d%-15s%-10d", user.getId(), user.getName(), user.getAge()));
				}
			} catch (Exception e) {
				System.out.println("Unable to execute query.");
			} finally {
				statement.close();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("Unable to open database.");
		}
	}

	private final static class User {

		private int id, age;
		private String name;

		User(int id, String name, int age) {
			this.id = id;
			this.name = name;
			this.age = age;
		}

		int getId() { return id; }

		String getName() { return name; }

		int getAge() { return age; }
	}
}