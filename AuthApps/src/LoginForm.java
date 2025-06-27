import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passField;
    private JCheckBox showPassword;

    public LoginForm() {
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Off-white color
        Color offWhite = new Color(245, 245, 245);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(offWhite);
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(offWhite);
        formPanel.setPreferredSize(new Dimension(700, 600));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Login", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        formPanel.add(title, gbc);

        gbc.gridy = 1;
        emailField = createTextField("Email Address");
        formPanel.add(emailField, gbc);

        gbc.gridy = 2;
        passField = createPasswordField("Password");
        formPanel.add(passField, gbc);

        gbc.gridy = 3;
        showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(offWhite);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        showPassword.addActionListener(e -> passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•'));
        formPanel.add(showPassword, gbc);

        gbc.gridy = 4;
        JButton loginBtn = createButton("Login");
        loginBtn.addActionListener(this::loginAction);
        formPanel.add(loginBtn, gbc);

        gbc.gridy = 5;
        JButton goToRegister = new JButton("New user? Register");
        goToRegister.setFocusPainted(false);
        goToRegister.setBorderPainted(false);
        goToRegister.setBackground(offWhite);
        goToRegister.setForeground(Color.BLUE);
        goToRegister.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        goToRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToRegister.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });
        formPanel.add(goToRegister, gbc);

        mainPanel.add(formPanel);
        setVisible(true);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 50));
        field.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                placeholder,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 14)
        ));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 50));
        field.setEchoChar('•');
        field.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                placeholder,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 14)
        ));
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }

    private void loginAction(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPass = PasswordUtils.hashPassword(password);

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
            ps.setString(1, email);
            ps.setString(2, hashedPass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); 
                new AdminDashboard(); 
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
