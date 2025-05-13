package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Commercial Car Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        JLabel mainHeadingLabel = new JLabel("Commercial Car Rental System", SwingConstants.CENTER);
        mainHeadingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        mainHeadingLabel.setForeground(Color.BLUE);

        JLabel subHeadingLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        subHeadingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subHeadingLabel.setForeground(Color.DARK_GRAY);

        JLabel userLabel = new JLabel("Admin ID:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton forgetButton = new JButton("Forget Password?");

        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(mainHeadingLabel, gbc);

        gbc.gridy++;
        frame.add(subHeadingLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        frame.add(userLabel, gbc);

        gbc.gridx = 1;
        frame.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        frame.add(passLabel, gbc);

        gbc.gridx = 1;
        frame.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        frame.add(loginButton, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(forgetButton);
        frame.add(buttonPanel, gbc);

        frame.setVisible(true);

        
        loginButton.addActionListener(e -> {
            String adminId = userField.getText();
            String password = new String(passField.getPassword());
            if (authenticateAdmin(adminId, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Admin ID or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            RegisterPage.showRegisterPage();
        });

        forgetButton.addActionListener(e -> {
            frame.dispose();
            ForgetPasswordPage.showForgetPasswordPage();
        });
    }

    private static boolean authenticateAdmin(String adminId, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM admin WHERE admin_id = ? AND password = ?")) {
            stmt.setString(1, adminId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void showDashboard() {
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        dashboardFrame.setLayout(new BorderLayout());

        JLabel dashboardLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardFrame.add(dashboardLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); 
        JButton customerRegButton = new JButton("Customer Registration");
        JButton carRegButton = new JButton("Car Registration");
        JButton rentingProcessButton = new JButton("Renting Process");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(customerRegButton);
        buttonPanel.add(carRegButton);
        buttonPanel.add(rentingProcessButton);
        buttonPanel.add(logoutButton);

        dashboardFrame.add(buttonPanel, BorderLayout.CENTER);

        dashboardFrame.setVisible(true);

        carRegButton.addActionListener(e -> {
            dashboardFrame.dispose();
            democarmodule carModule = new democarmodule();
            carModule.setVisible(true);
        });

        customerRegButton.addActionListener(e -> {
            dashboardFrame.dispose();
            CustomerRegistrationPage.showCustomerRegistrationPage();
        });

        rentingProcessButton.addActionListener(e -> {
            dashboardFrame.dispose();
            AdminRentalApproval rentingPage = new AdminRentalApproval();
            rentingPage.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dashboardFrame.dispose();
            System.exit(0);
        });
    }
}
