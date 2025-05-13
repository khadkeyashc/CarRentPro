package com.carrental;
import javax.swing.*;




import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CustomerRegistrationPage {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    /**
     * @wbp.parser.entryPoint
     */
    public static void showCustomerRegistrationPage() {
    	 JFrame frame = new JFrame("Customer Registration");
         frame.setSize(800, 700);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
         frame.getContentPane().setLayout(null); 
         JLabel searchLabel = new JLabel("Search Customer ID:");
         searchLabel.setBounds(50, 20, 150, 25);
         JTextField searchField = new JTextField();
         searchField.setBounds(200, 20, 200, 25);
         JButton searchButton = new JButton("Search");
         searchButton.setBounds(450, 20, 100, 25);

         JLabel idLabel = new JLabel("Customer ID:");
         idLabel.setBounds(50, 60, 150, 25);
         JTextField idField = new JTextField();
         idField.setBounds(200, 60, 200, 25);

         JLabel nameLabel = new JLabel("Customer Name:");
         nameLabel.setBounds(50, 100, 150, 25);
         JTextField nameField = new JTextField();
         nameField.setBounds(200, 100, 200, 25);

         JLabel passLabel = new JLabel("Password:");
         passLabel.setBounds(50, 140, 150, 25);
         JPasswordField passField = new JPasswordField();
         passField.setBounds(200, 140, 200, 25);

         JLabel phoneLabel = new JLabel("Mobile Number:");
         phoneLabel.setBounds(50, 180, 150, 25);
         JTextField phoneField = new JTextField();
         phoneField.setBounds(200, 180, 200, 25);

         JLabel emailLabel = new JLabel("Email:");
         emailLabel.setBounds(50, 220, 150, 25);
         JTextField emailField = new JTextField();
         emailField.setBounds(200, 220, 200, 25);

         JLabel addressLabel = new JLabel("Residential Address:");
         addressLabel.setBounds(50, 260, 150, 25);
         JTextField addressField = new JTextField();
         addressField.setBounds(200, 260, 200, 25);

         JLabel licenseLabel = new JLabel("Driving License No:");
         licenseLabel.setBounds(50, 300, 150, 25);
         JTextField licenseField = new JTextField();
         licenseField.setBounds(200, 300, 200, 25);

         JLabel genderLabel = new JLabel("Gender:");
         genderLabel.setBounds(50, 340, 150, 25);
         JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
         genderComboBox.setBounds(200, 340, 200, 25);

         JLabel dobLabel = new JLabel("Date of Birth:");
         dobLabel.setBounds(50, 380, 150, 25);
         JDateChooser dobChooser = new JDateChooser();
         dobChooser.setBounds(200, 380, 200, 25);

         JLabel licenseExpiryLabel = new JLabel("License Expiry Date:");
         licenseExpiryLabel.setBounds(50, 420, 150, 25);
         JDateChooser expiryDateChooser = new JDateChooser();
         expiryDateChooser.setBounds(200, 420, 200, 25);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(251, 475, 150, 30);
        JButton showDataButton = new JButton("Show Customer Data");
        showDataButton.setBounds(478, 527, 200, 30);

        JButton updateDataButton = new JButton("Update Customer Data");
        updateDataButton.setBounds(26, 527, 174, 30);

        JButton deleteDataButton = new JButton("Delete Customer Data");
        deleteDataButton.setBounds(234, 527, 200, 30);
        JButton backButton = new JButton("Back");
        backButton.setBounds(165, 582, 100, 30);  
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(369, 582, 100, 30);  

        frame.getContentPane().add(backButton);
        frame.getContentPane().add(resetButton);

       
        backButton.addActionListener(e -> {
            frame.dispose(); 
            LoginPage loginPage = new LoginPage();
            loginPage.showDashboard();
        });

        resetButton.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
            passField.setText("");
            phoneField.setText("");
            emailField.setText("");
            addressField.setText("");
            licenseField.setText("");
            genderComboBox.setSelectedIndex(0);  
            dobChooser.setDate(null);
            expiryDateChooser.setDate(null);
        });

       
        frame.getContentPane().add(searchLabel);
        frame.getContentPane().add(searchField);
        frame.getContentPane().add(searchButton);
        frame.getContentPane().add(idLabel);
        frame.getContentPane().add(idField);
        frame.getContentPane().add(nameLabel);
        frame.getContentPane().add(nameField);
        frame.getContentPane().add(passLabel);
        frame.getContentPane().add(passField);
        frame.getContentPane().add(phoneLabel);
        frame.getContentPane().add(phoneField);
        frame.getContentPane().add(emailLabel);
        frame.getContentPane().add(emailField);
        frame.getContentPane().add(addressLabel);
        frame.getContentPane().add(addressField);
        frame.getContentPane().add(licenseLabel);
        frame.getContentPane().add(licenseField);
        frame.getContentPane().add(genderLabel);
        frame.getContentPane().add(genderComboBox);
        frame.getContentPane().add(dobLabel);
        frame.getContentPane().add(dobChooser);
        frame.getContentPane().add(licenseExpiryLabel);
        frame.getContentPane().add(expiryDateChooser);
        frame.getContentPane().add(submitButton);
        frame.getContentPane().add(showDataButton);
        frame.getContentPane().add(updateDataButton);
        frame.getContentPane().add(deleteDataButton);
   

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {
            String customerId = idField.getText();
            String customerName = nameField.getText();
            String password = new String(passField.getPassword());
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String licenseNo = licenseField.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            Date dob = dobChooser.getDate();
            Date expiryDate = expiryDateChooser.getDate();

            if (validateForm(customerId, customerName, password, phone, email, address, licenseNo, gender, dob, expiryDate)) {
                insertCustomer(customerId, customerName, password, phone, email, address, licenseNo, gender, dob, expiryDate);
            }
        });


        searchButton.addActionListener(e -> {
            String customerId = searchField.getText();
            searchCustomerById(customerId, idField, nameField, passField, phoneField, emailField, addressField, licenseField, genderComboBox, dobChooser, expiryDateChooser);
        });

        updateDataButton.addActionListener(e -> {
            String customerId = idField.getText();
            String customerName = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            updateCustomerData(customerId, customerName, phone, email);
        });

        deleteDataButton.addActionListener(e -> {
            String customerId = idField.getText();
            deleteCustomerById(customerId);
        });

        showDataButton.addActionListener(e -> showAllCustomers());
    }
    private static boolean validateForm(String customerId, String customerName, String password, String phone, String email, String address, String licenseNo, String gender, Date dob, Date expiryDate) {
        if (customerId.isEmpty() || customerName.isEmpty() || password.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || address.isEmpty() || licenseNo.isEmpty() || gender == null || dob == null || expiryDate == null) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return false;
        }

        if (!phone.matches("^[6-9]\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid 10-digit mobile number starting with 6-9.");
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
            return false;
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters, including uppercase, lowercase, digit, and special character.");
            return false;
        }

        if (!customerName.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(null, "Name can only contain letters and spaces.");
            return false;
        }

        if (!licenseNo.matches("^[A-Z0-9]{8,16}$")) {
            JOptionPane.showMessageDialog(null, "License number must be 8-16 alphanumeric characters (uppercase only).");
            return false;
        }

        Date today = new Date();
        if (dob.after(today)) {
            JOptionPane.showMessageDialog(null, "Date of Birth cannot be in the future.");
            return false;
        }

        if (!expiryDate.after(today)) {
            JOptionPane.showMessageDialog(null, "License expiry date must be a future date.");
            return false;
        }

        return true;
    }

    private static void insertCustomer(String customerId, String customerName, String password, String phone, String email, String address, String licenseNo, String gender, Date dob, Date expiryDate) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO customer2 (customer_id, name, password, mobile, email, address, license_no, gender, dob, license_expiry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, customerId);
            stmt.setString(2, customerName);
            stmt.setString(3, password);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, address);
            stmt.setString(7, licenseNo);
            stmt.setString(8, gender);
            stmt.setDate(9, new java.sql.Date(dob.getTime()));
            stmt.setDate(10, new java.sql.Date(expiryDate.getTime()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer registered successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error inserting customer: " + ex.getMessage());
        }
    }

    private static void searchCustomerById(String customerId, JTextField idField, JTextField nameField, JPasswordField passField, JTextField phoneField, JTextField emailField, JTextField addressField, JTextField licenseField, JComboBox<String> genderComboBox, JDateChooser dobChooser, JDateChooser expiryDateChooser) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer2 WHERE customer_id = ?")) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idField.setText(rs.getString("customer_id"));
                nameField.setText(rs.getString("name"));
                passField.setText(rs.getString("password"));
                phoneField.setText(rs.getString("mobile"));
                emailField.setText(rs.getString("email"));
                addressField.setText(rs.getString("address"));
                licenseField.setText(rs.getString("license_no"));
                genderComboBox.setSelectedItem(rs.getString("gender"));
                dobChooser.setDate(rs.getDate("dob"));
                expiryDateChooser.setDate(rs.getDate("license_expiry"));
            } else {
                JOptionPane.showMessageDialog(null, "Customer not found!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error searching customer: " + ex.getMessage());
        }
    }

    private static void updateCustomerData(String customerId, String customerName, String phone, String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE customer2 SET name = ?, mobile = ?, email = ? WHERE customer_id = ?")) {
            stmt.setString(1, customerName);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, customerId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Customer data updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Customer not found!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error updating customer: " + ex.getMessage());
        }
    }

    private static void deleteCustomerById(String customerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM customer2 WHERE customer_id = ?")) {
            stmt.setString(1, customerId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Customer not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error deleting customer: " + ex.getMessage());
        }
    }

    private static void showAllCustomers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer2");
             ResultSet rs = stmt.executeQuery()) {

            JFrame frame = new JFrame("Customer Data");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            String[] columnNames = {"Customer ID", "Name", "Mobile", "Email", "Address", "License No", "Gender", "DOB", "License Expiry"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("license_no"),
                        rs.getString("gender"),
                        rs.getDate("dob"),
                        rs.getDate("license_expiry")
                });
            }

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error displaying customers: " + ex.getMessage());
        }
    }
  


    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(CustomerRegistrationPage::showCustomerRegistrationPage);
    }
}


 