import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DB_Manager {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String DB_USER = "inka";
    private static final String DB_PASSWORD = "testPW";

    // klappt irgendwie nicht :(
    public static Connection ConnectDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Verbindung erfolgreich!");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void AddCustomer(Scanner scanner) {
        System.out.println("Name:");
        String name = scanner.nextLine();
        System.out.println("contact:");
        String contact = scanner.nextLine();
        System.out.println("city:");
        String city = scanner.nextLine();
        String sqlString = "INSERT INTO Customers (name, contact, city) values (?, ? , ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Verbindung erfolgreich!");
            if (con == null) {
                return;
            }

            try (PreparedStatement sqlQuerry = con.prepareStatement(sqlString)) {
                sqlQuerry.setString(1, name);
                sqlQuerry.setString(2, contact);
                sqlQuerry.setString(3, city);
                sqlQuerry.executeQuery();
            } catch (SQLException e) {
                System.out.println("Add Customer failed with :");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("User added.");
    }

    public static void DisplayCustomers() {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (con == null) {
                return;
            }

            String sqlString = "SELECT * FROM Customers";
            try (PreparedStatement sqlQuerry = con.prepareStatement(sqlString)) {
                ResultSet results = sqlQuerry.executeQuery();
                System.out.println("Customers: ");
                int i = 0;
                while (results.next()) {
                    i++;
                    System.out.printf("Id: %d, name: %s, contact: %s, city: %s%n",
                            results.getInt("id"),
                            results.getString("name"),
                            results.getString("contact"),
                            results.getString("city"));
                }
                System.out.printf("%d Rows found", i);
            } catch (SQLException e) {
                System.out.println("Show Customer failed with :");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ChangeCustomer(Scanner scanner) {
        System.out.println("Id:");
        int id = scanner.nextInt();        
        scanner.nextLine();

        System.out.println("Name:");
        String name = scanner.nextLine();
        System.out.println("contact:");
        String contact = scanner.nextLine();
        System.out.println("city:");
        String city = scanner.nextLine();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (con == null) {
                return;
            }

            String sqlString = "UPDATE Customers SET name=?, contact=?, city=? WHERE id=?";
            try (PreparedStatement sqlQuerry = con.prepareStatement(sqlString)) {
                sqlQuerry.setString(1, name);
                sqlQuerry.setString(2, contact);
                sqlQuerry.setString(3, city);
                sqlQuerry.setInt(4, id);
                sqlQuerry.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("Verbindung erfolgreich!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteCostumer(Scanner scanner) {
        System.out.println("Id:");
        int id = scanner.nextInt();        
        scanner.nextLine();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (con == null) {
                return;
            }

            String sqlString = "SELECT name FROM Customers WHERE id=?";
            try (PreparedStatement sqlQuery = con.prepareStatement(sqlString)) {
                sqlQuery.setInt(1, id);
                ResultSet results = sqlQuery.executeQuery();
                ArrayList<String> names = new ArrayList<>();
                while (results.next()) {
                    names.add(results.getString("name"));
                }

                if (names.size() < 0) {
                    System.out.println("No Customer with the ID found");
                    return;
                }
                System.out.printf("Do you really want to delete the Customers: %s ?", String.join(",", names));
                System.out.println("[Y]: yes, else: no");
                String answer = scanner.nextLine();
                if ("Y".equals(answer) == false) {
                    System.out.println("deleting aborted");
                    return;
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
            sqlString = "delete FROM Customers Where id = ?";
            try (PreparedStatement sqlQuery = con.prepareStatement(sqlString)) {
                sqlQuery.setInt(1, id);
                sqlQuery.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
