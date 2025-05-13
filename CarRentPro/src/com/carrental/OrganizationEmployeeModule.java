package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;

public class OrganizationEmployeeModule extends JFrame {
    private JTextField txtOrgName, txtContact, txtEmail, txtPosition, txtPrice, txtDuration;
    private JButton btnSubmitRequest;

    private Connection connection;

    public OrganizationEmployeeModule() {
        setTitle("Organization Module");
        setSize(746, 535);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        

        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBackground(new Color(11, 61, 145)); 

        JLabel mainHeading = new JLabel("Commercial Car Rental System", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Arial", Font.BOLD, 22));
        mainHeading.setForeground(Color.WHITE);

        JLabel subHeading = new JLabel("Organization Module", SwingConstants.CENTER);
        subHeading.setFont(new Font("Arial", Font.PLAIN, 18));
        subHeading.setForeground(Color.WHITE);

        headingPanel.add(mainHeading);
        headingPanel.add(subHeading);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(new Color(224, 224, 224)); 

        addLabelAndField(formPanel, "Organization Name:", txtOrgName = new JTextField());
        addLabelAndField(formPanel, "Contact Number:", txtContact = new JTextField());
        addLabelAndField(formPanel, "Email:", txtEmail = new JTextField());
        addLabelAndField(formPanel, "Position:", txtPosition = new JTextField());
        addLabelAndField(formPanel, "Price Offered (per month):", txtPrice = new JTextField());
        addLabelAndField(formPanel, "Duration (in months):", txtDuration = new JTextField());

        
        btnSubmitRequest = new JButton("Submit Rental Request");
        btnSubmitRequest.setBackground(new Color(11, 61, 145)); 
        btnSubmitRequest.setForeground(Color.WHITE); 
        btnSubmitRequest.setFocusPainted(false);
        btnSubmitRequest.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmitRequest.addActionListener(e -> submitRentalRequest());
        
        formPanel.add(new JLabel()); 
        formPanel.add(btnSubmitRequest);

        
        getContentPane().add(headingPanel, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);

        initializeDBConnection();
        setVisible(true);
    }

    private void addLabelAndField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label);
        
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(textField);
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void submitRentalRequest() {
        try {
           
            if (!validateForm()) {
                return;
            }

            String orgName = txtOrgName.getText();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            String position = txtPosition.getText();
            double price = Double.parseDouble(txtPrice.getText());
            int duration = Integer.parseInt(txtDuration.getText());

            String query = """
                INSERT INTO organization_employee (org_name, contact, email, position, price_per_month, rental_duration, request_status)
                VALUES (?, ?, ?, ?, ?, ?, 'Pending');
            """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, orgName);
            ps.setString(2, contact);
            ps.setString(3, email);
            ps.setString(4, position);
            ps.setDouble(5, price);
            ps.setInt(6, duration);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Rental request submitted successfully.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to submit request.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        String orgName = txtOrgName.getText().trim();
        String contact = txtContact.getText().trim();
        String email = txtEmail.getText().trim();
        String position = txtPosition.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String durationStr = txtDuration.getText().trim();

        if (orgName.isEmpty()) {
            showMessage("Organization Name cannot be empty.");
            return false;
        }

        if (!Pattern.matches("\\d{10}", contact)) {
            showMessage("Contact Number must be exactly 10 digits.");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            showMessage("Invalid Email format.");
            return false;
        }

        if (position.isEmpty()) {
            showMessage("Position cannot be empty.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                showMessage("Price must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Price must be a numeric value.");
            return false;
        }

        try {
            int duration = Integer.parseInt(durationStr);
            if (duration < 3 || duration > 60) {
                showMessage("Rental duration must be between 3 months and 5 years.");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Duration must be a numeric value.");
            return false;
        }

        return true;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void clearForm() {
        txtOrgName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtPosition.setText("");
        txtPrice.setText("");
        txtDuration.setText("");
    }

    public static void main(String[] args) {
        new OrganizationEmployeeModule();
    }
}
