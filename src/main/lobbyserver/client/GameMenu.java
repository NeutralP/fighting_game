package src.main.lobbyserver.client;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.main.monfight.gui.client.ClientMainFrame;

public class GameMenu extends JFrame {
    public GameMenu() {
        JFrame frame = new JFrame("Menu Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Tạo thanh menu
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Tạo menu "File" và thêm nút "Exit"
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        // Tạo menu "Setting" và thêm nút "Volume" và "Game"
        JMenu settingMenu = new JMenu("Setting");
        JMenuItem volumeMenuItem = new JMenuItem("Volume");
        JMenuItem gameMenuItem = new JMenuItem("Game");
        settingMenu.add(volumeMenuItem);
        settingMenu.add(gameMenuItem);

        // Thêm menu vào thanh menu
        menuBar.add(fileMenu);
        menuBar.add(settingMenu);
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Các nút
        JButton matchGameButton = new JButton("Match Game");
        matchGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show an instance of ClientMainFrame
                SwingUtilities.invokeLater(() -> {
                    ClientMainFrame clientMainFrame = new ClientMainFrame();
                    clientMainFrame.setVisible(true);
                });
            }
        });
        JButton anotherButton = new JButton("Another Button");
        JButton yetAnotherButton = new JButton("Yet Another Button");

        // Thiết lập layout constraints để căn chỉnh vào giữa
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        buttonPanel.add(matchGameButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(anotherButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(yetAnotherButton, gbc);

        // Hiển thị cửa sổ
        frame.setVisible(true);
    }
    public static void main(final String[] args) {
        new GameMenu();
    }
}
