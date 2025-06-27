import javax.swing.*;

public class UserDashboard extends JFrame {
    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome, User!", SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
