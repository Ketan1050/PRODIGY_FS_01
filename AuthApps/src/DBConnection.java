import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/auth_db";
        String user = "root";
        String pass = "root@12345";
        return DriverManager.getConnection(url, user, pass);
    }
}
