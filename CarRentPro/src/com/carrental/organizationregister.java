package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class organizationregister {
    private JFrame frame;
    private JTextField orgNameField, emailField, contactField, addressField;
    private JPasswordField passwordField;

    public organizationregister() {
        frame = new JFrame("Organization Registration");
        frame.setSize(400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

       
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  

        frame.getContentPane().setBackground(new Color(245, 245, 245));

        JLabel headingLabel = new JLabel("Commercial Car Rental System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(50, 65, 85));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  
        frame.add(headingLabel, gbc);

        JLabel subheadingLabel = new JLabel("Organization Register");
        subheadingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subheadingLabel.setForeground(new Color(75, 85, 99));
        subheadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(subheadingLabel, gbc);

        JLabel orgNameLabel = new JLabel("Organization Name:");
        orgNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orgNameLabel.setForeground(new Color(75, 85, 99));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(orgNameLabel, gbc);

        orgNameField = new JTextField(20);
        orgNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(orgNameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(75, 85, 99));
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(emailField, gbc);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contactLabel.setForeground(new Color(75, 85, 99));
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(contactLabel, gbc);

        contactField = new JTextField(20);
        contactField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(contactField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(75, 85, 99));
        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 5;
        frame.add(passwordField, gbc);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setForeground(new Color(75, 85, 99));
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(addressLabel, gbc);

        addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 6;
        frame.add(addressField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(50, 65, 85));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> registerOrganization());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        frame.add(registerButton, gbc);

        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);
    }

    private void registerOrganization() {
        String orgName = orgNameField.getText();
        String email = emailField.getText();
        String contact = contactField.getText();
        String password = new String(passwordField.getPassword());
        String address = addressField.getText();

        if (orgName.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
            String query = "INSERT INTO organization (org_name, email, contact, password, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, orgName);
            pst.setString(2, email);
            pst.setString(3, contact);
            pst.setString(4, password);
            pst.setString(5, address);

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Organization Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                orgNameField.setText("");
                emailField.setText("");
                contactField.setText("");
                passwordField.setText("");
                addressField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new organizationregister();
    }
}
