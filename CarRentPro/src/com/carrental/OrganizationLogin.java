package com.carrental;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class OrganizationLogin {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public OrganizationLogin() {
        frame = new JFrame("Organization Login");
        frame.setSize(450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        frame.getContentPane().setBackground(new Color(243, 243, 243)); 
        frame.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));

        JLabel headingLabel = new JLabel("Commercial Car Rental System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(34, 34, 34));  
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(headingLabel, gbc);

        JLabel subheadingLabel = new JLabel("Organization Login");
        subheadingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subheadingLabel.setForeground(new Color(102, 102, 102)); 
        subheadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(subheadingLabel, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(34, 34, 34)); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBackground(new Color(255, 255, 255)); 
        emailField.setForeground(new Color(34, 34, 34)); 
        emailField.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        emailField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(emailField, gbc);

        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(34, 34, 34)); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(new Color(34, 34, 34));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1)); 
        passwordField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setBackground(new Color(34, 34, 34)); 
        loginButton.setForeground(new Color(255, 255, 255));
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(34, 34, 34), 1));
        loginButton.addActionListener(e -> loginOrganization());
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(51, 51, 51)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(34, 34, 34)); 
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.add(loginButton, gbc);

        
        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 14));
        forgotPasswordButton.setForeground(new Color(34, 34, 34)); 
        forgotPasswordButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        forgotPasswordButton.setBackground(new Color(243, 243, 243));
        forgotPasswordButton.addActionListener(e -> forgotPassword());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(forgotPasswordButton, gbc);

        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);
    }

    private void loginOrganization() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
            String query = "SELECT * FROM organization WHERE email = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
               
                new OrganizationEmployeeModule();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!");
            }

            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(frame, "Enter your registered email:");
        if (email != null) {
            resetPassword(email);
        }
    }

    private void resetPassword(String email) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "");
            String query = "SELECT * FROM organization WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String newPassword = JOptionPane.showInputDialog(frame, "Enter new password:");
                String updateQuery = "UPDATE organization SET password = ? WHERE email = ?";
                PreparedStatement updatePst = con.prepareStatement(updateQuery);
                updatePst.setString(1, newPassword);
                updatePst.setString(2, email);
                updatePst.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Password updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Email not registered!");
            }

            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new OrganizationLogin();
    }
}
