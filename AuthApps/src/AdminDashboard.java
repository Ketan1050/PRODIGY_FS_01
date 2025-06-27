import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel("Welcome to Admin Dashboard!", JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(label);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
