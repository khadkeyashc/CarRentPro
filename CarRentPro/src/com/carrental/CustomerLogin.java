package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private Connection connection;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel panelHeading = new JPanel();
        panelHeading.setLayout(new BorderLayout());
        JLabel lblMainHeading = new JLabel("Commercial Car Rental System", SwingConstants.CENTER);
        lblMainHeading.setFont(new Font("Arial", Font.BOLD, 20));
        panelHeading.add(lblMainHeading, BorderLayout.CENTER);
        add(panelHeading, BorderLayout.NORTH);

        JLabel lblSubHeading = new JLabel("Customer Login", SwingConstants.CENTER);
        lblSubHeading.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lblSubHeading, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblUsername, gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelForm.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelForm.add(txtPassword, gbc);

        btnLogin = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelForm.add(btnLogin, gbc);

        add(panelForm, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> authenticateCustomer());

        initializeDBConnection();
        setVisible(true);
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void authenticateCustomer() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            String query = "SELECT * FROM customer2 WHERE name = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                new CustomerDashboard2(customerId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Login error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CustomerLogin();
    }
}
