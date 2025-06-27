import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passField;
    private JCheckBox showPassword;

    public RegisterForm() {
        setTitle("Register");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Off-white color
        Color offWhite = new Color(245, 245, 245);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(offWhite);
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(offWhite);
        formPanel.setPreferredSize(new Dimension(700, 1000));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel title = new JLabel("Create Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        formPanel.add(title, gbc);

        // Name Field
        gbc.gridy = 1;
        nameField = createTextField("Full Name");
        formPanel.add(nameField, gbc);

        // Email Field
        gbc.gridy = 2;
        emailField = createTextField("Email Address");
        formPanel.add(emailField, gbc);

        // Password Field
        gbc.gridy = 3;
        passField = createPasswordField("Password");
        formPanel.add(passField, gbc);

        // Show Password
        gbc.gridy = 4;
        showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(offWhite);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        showPassword.addActionListener(e -> passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•'));
        formPanel.add(showPassword, gbc);

        // Register Button
        gbc.gridy = 5;
        JButton registerBtn = createButton("Register");
        registerBtn.addActionListener(this::registerAction);
        formPanel.add(registerBtn, gbc);

        // Toggle to Login
        gbc.gridy = 6;
        JButton goToLogin = new JButton("Already have an account? Login");
        goToLogin.setFocusPainted(false);
        goToLogin.setBorderPainted(false);
        goToLogin.setBackground(offWhite);
        goToLogin.setForeground(Color.BLUE);
        goToLogin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        goToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToLogin.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        formPanel.add(goToLogin, gbc);

        mainPanel.add(formPanel);
        setVisible(true);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        field.setPreferredSize(new Dimension(700, 60));
        field.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                placeholder,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 16)
        ));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        field.setPreferredSize(new Dimension(700, 60));
        field.setEchoChar('•');
        field.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                placeholder,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 16)
        ));
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(400, 55));
        return btn;
    }

    private void registerAction(ActionEvent e) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPass = PasswordUtils.hashPassword(password);

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPass);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterForm::new);
    }
}
