package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ForgetPasswordPage {
    public static void showForgetPasswordPage() {
        JFrame frame = new JFrame("Commercial Car Rental System - Forget Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel mainHeadingLabel = new JLabel("Commercial Car Rental System", SwingConstants.CENTER);
        mainHeadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainHeadingLabel.setForeground(Color.BLUE);

        JLabel subHeadingLabel = new JLabel("Forget Password Page", SwingConstants.CENTER);
        subHeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subHeadingLabel.setForeground(Color.DARK_GRAY);

        JLabel nameLabel = new JLabel("Enter Admin Name:");
        JTextField nameField = new JTextField();

        JLabel idLabel = new JLabel("Enter Admin ID:");
        JTextField idField = new JTextField();

        JButton showPasswordButton = new JButton("Show Password");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(mainHeadingLabel, gbc);

        gbc.gridy++;
        frame.add(subHeadingLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        frame.add(nameLabel, gbc);

        gbc.gridx = 1;
        frame.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        frame.add(idLabel, gbc);

        gbc.gridx = 1;
        frame.add(idField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        frame.add(showPasswordButton, gbc);

        frame.setVisible(true);

        showPasswordButton.addActionListener(e -> {
            String adminName = nameField.getText().trim();
            String adminId = idField.getText().trim();

            if (adminName.isEmpty() || adminId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String password = retrievePassword(adminName, adminId);
            if (password != null) {
                JOptionPane.showMessageDialog(frame, "Your Password: " + password, "Password Retrieved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Admin Name or ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static String retrievePassword(String adminName, String adminId) {
        final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_db";
        final String DB_USER = "root";
        final String DB_PASSWORD = "123456";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM admin WHERE admin_name = ? AND admin_id = ?")) {
            stmt.setString(1, adminName);
            stmt.setString(2, adminId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
