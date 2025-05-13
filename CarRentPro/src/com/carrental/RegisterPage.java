package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage {
    public static void showRegisterPage() {
        JFrame frame = new JFrame("Commercial Car Rental System - Admin Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setSize(500, 400);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel mainHeadingLabel = new JLabel("Commercial Car Rental System", SwingConstants.CENTER);
        mainHeadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainHeadingLabel.setForeground(Color.BLUE);

        JLabel subHeadingLabel = new JLabel("Admin Registration Page", SwingConstants.CENTER);
        subHeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subHeadingLabel.setForeground(Color.DARK_GRAY);

       
        JLabel nameLabel = new JLabel("Admin Name:");
        JTextField nameField = new JTextField();

        JLabel idLabel = new JLabel("Admin ID:");
        JTextField idField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JLabel rePassLabel = new JLabel("Re-type Password:");
        JPasswordField rePassField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Already have an account? Login");

        
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
        frame.add(passLabel, gbc);

        gbc.gridx = 1;
        frame.add(passField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        frame.add(rePassLabel, gbc);

        gbc.gridx = 1;
        frame.add(rePassField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        frame.add(registerButton, gbc);

        gbc.gridy++;
        frame.add(loginButton, gbc);

        frame.setVisible(true);

        
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());
            String rePassword = new String(rePassField.getPassword());
            
            if (name.isEmpty() || id.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(rePassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
                 PreparedStatement checkStmt = conn.prepareStatement("SELECT admin_id FROM admin WHERE admin_id = ?");
                 PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO admin (admin_name, admin_id, password) VALUES (?, ?, ?)")) {

                
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Admin ID already exists!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                insertStmt.setString(1, name);
                insertStmt.setString(2, id);
                insertStmt.setString(3, password);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Registration Successful!");
                frame.dispose();
                LoginPage.main(null); 

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginButton.addActionListener(e -> {
            frame.dispose();
            LoginPage.main(null);
        });
    }
}
