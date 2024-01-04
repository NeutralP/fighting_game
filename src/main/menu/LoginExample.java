package src.main.menu;
import javax.swing.*;

import src.main.menu.GameMenu;
import src.main.monfight.gui.client.ClientMainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Login Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        frame.add(loginPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        loginPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        loginPanel.add(loginButton, gbc);

        // Xử lý sự kiện đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // Kiểm tra đăng nhập ở đây (ví dụ: so sánh với thông tin đăng nhập đã định trước)
                if ("admin".equals(username) && "admin".equals(new String(password))) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    openMenuGame();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed. Please try again.");
                }
                // Xóa thông tin trường đăng nhập và mật khẩu sau khi kiểm tra
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        frame.setVisible(true);
    }
    private static void openMenuGame() {
		GameMenu gameMenu = new GameMenu();
		gameMenu.setVisible(true);
    }
}