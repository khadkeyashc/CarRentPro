package com.carrental;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class democarmodule extends JFrame {
   
    private JTextField txtCarName, txtModelName, txtPurchaseDate, txtInsuranceExpiryDate, txtSearchCar;
    private JComboBox<String> cbCondition, cbInsurance, cbCustomerId;
    private JButton btnSubmit, btnUpdate, btnDelete, btnReset, btnBack, btnShowData, btnSearchCar;
    private JLabel lblMessage;
    private JButton btnUploadImage;
    private String imagePath = ""; 

    private Connection conn;
    private PreparedStatement ps;

    public democarmodule() {
        
    	setTitle("Car Registration");
        setSize(500, 450); 
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        txtCarName = new JTextField(15);
        txtModelName = new JTextField(15);
        txtPurchaseDate = new JTextField(15); 
        txtInsuranceExpiryDate = new JTextField(15); 
        txtSearchCar = new JTextField(15); 
        cbCondition = new JComboBox<>(new String[]{"New", "Best", "Good", "Average"});
        cbInsurance = new JComboBox<>(new String[]{"Yes", "No"});
        cbCustomerId = new JComboBox<>(); 
        lblMessage = new JLabel("");
        btnSubmit = new JButton("Submit");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnReset = new JButton("Reset");
        btnBack = new JButton("Back");
        btnUploadImage = new JButton("Upload Car Image");
        btnShowData = new JButton("Show Car Data");
        btnSearchCar = new JButton("Search Car");

        
        btnUploadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "Image uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        loadCustomerIds();

        JLabel lblHeading = new JLabel("Commercial Car Rental System");
        lblHeading.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel lblSubHeading = new JLabel("Car Registration");
        lblSubHeading.setFont(new Font("Arial", Font.PLAIN, 18));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblHeading, gbc);

        gbc.gridy++; 
        add(lblSubHeading, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Customer ID"), gbc);
        gbc.gridx = 1; add(cbCustomerId, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Car Name"), gbc);
        gbc.gridx = 1; add(txtCarName, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Model Name"), gbc);
        gbc.gridx = 1; add(txtModelName, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Condition"), gbc);
        gbc.gridx = 1; add(cbCondition, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Purchase Date"), gbc);
        gbc.gridx = 1; add(txtPurchaseDate, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Insurance"), gbc);
        gbc.gridx = 1; add(cbInsurance, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Insurance Expiry Date"), gbc);
        gbc.gridx = 1; add(txtInsuranceExpiryDate, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        add(btnUploadImage, gbc);

        gbc.gridx = 0; gbc.gridy = 10;
        add(btnSubmit, gbc);
        gbc.gridx = 1; add(btnUpdate, gbc);

        gbc.gridx = 0; gbc.gridy = 11;
        add(btnDelete, gbc);
        gbc.gridx = 1; add(btnReset, gbc);

        gbc.gridx = 0; gbc.gridy = 12;
        add(btnShowData, gbc); // Add Show Data button

        gbc.gridx = 1; add(btnBack, gbc);

        // Search section for Car
        gbc.gridx = 0; gbc.gridy = 13;
        add(new JLabel("Search Car by Name"), gbc);
        gbc.gridx = 1; add(txtSearchCar, gbc);
        gbc.gridx = 2; add(btnSearchCar, gbc);

        gbc.gridx = 0; gbc.gridy = 14; gbc.gridwidth = 2;
        add(lblMessage, gbc);

        
        setVisible(true);

        btnSubmit.addActionListener(e -> submitCarDetails());
        btnUpdate.addActionListener(e -> updateCarDetails());
        btnDelete.addActionListener(e -> deleteCar());
        btnReset.addActionListener(e -> resetForm());
        btnBack.addActionListener(e -> goBack());
        btnShowData.addActionListener(e -> showCarData());
        btnSearchCar.addActionListener(e -> searchCarByName()); 
    }  
    
    private void loadCustomerIds() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
            String sql = "SELECT customer_id FROM customer2";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            cbCustomerId.removeAllItems();
            
            while (rs.next()) {
                cbCustomerId.addItem(rs.getString("customer_id"));
            }
        } catch (SQLException e) {
            lblMessage.setText("Error loading customer data: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    private void searchCarByName() {
        String carName = txtSearchCar.getText().trim(); 
        if (carName.isEmpty()) {
            lblMessage.setText("Please enter a car name to search.");
            return;
        }

        JFrame frame = new JFrame("Search Results");
        frame.setSize(800, 400);

        String[] columnNames = {"Car ID", "Car Name", "Model Name", "Condition", "Purchase Date", "Insurance", "Insurance Expiry", "Image Path"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        try {
            String query = "SELECT car.car_id, car_name, model_name, condition_type, purchase_date, insurance, insurance_expiry_date, image_path " +
                           "FROM car2 AS car LEFT JOIN car_images4 AS img ON car.car_id = img.car_id WHERE car_name LIKE ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + carName + "%"); 
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("car_id"),
                        rs.getString("car_name"),
                        rs.getString("model_name"),
                        rs.getString("condition_type"),
                        rs.getString("purchase_date"),
                        rs.getString("insurance"),
                        rs.getString("insurance_expiry_date"),
                        rs.getString("image_path") != null ? rs.getString("image_path") : "No Image"
                };
                model.addRow(row);
            }

            frame.add(scrollPane);
            frame.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading car data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitCarDetails() {
        if (txtCarName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the car name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtModelName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the model name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtPurchaseDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the purchase date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtInsuranceExpiryDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the insurance expiry date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbCustomerId.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid customer ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String carName = txtCarName.getText();
            String modelName = txtModelName.getText();
            String condition = cbCondition.getSelectedItem().toString();
            String purchaseDate = txtPurchaseDate.getText();
            String insurance = cbInsurance.getSelectedItem().toString();
            String insuranceExpiry = txtInsuranceExpiryDate.getText();
            int customerId = Integer.parseInt(cbCustomerId.getSelectedItem().toString());

            String sql = "INSERT INTO car2 (car_name, model_name, condition_type, purchase_date, insurance, insurance_expiry_date, customer_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, carName);
            ps.setString(2, modelName);
            ps.setString(3, condition);
            ps.setString(4, purchaseDate);
            ps.setString(5, insurance);
            ps.setString(6, insuranceExpiry);
            ps.setInt(7, customerId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int carId = rs.getInt(1);

                    if (!imagePath.isEmpty()) {
                        String sqlImage = "INSERT INTO car_images4 (car_id, image_path) VALUES (?, ?)";
                        ps = conn.prepareStatement(sqlImage);
                        ps.setInt(1, carId);
                        ps.setString(2, imagePath);
                        ps.executeUpdate();
                    }
                }
                JOptionPane.showMessageDialog(this, "Car details submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting car details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error submitting car details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateCarDetails() {
        String carId = JOptionPane.showInputDialog(this, "Enter Car ID to Update:");
        try {
            String carName = txtCarName.getText();
            String modelName = txtModelName.getText();
            String condition = cbCondition.getSelectedItem().toString();
            String purchaseDate = txtPurchaseDate.getText();
            String insurance = cbInsurance.getSelectedItem().toString();
            String insuranceExpiry = txtInsuranceExpiryDate.getText();

            String sql = "UPDATE car2 SET car_name=?, model_name=?, condition_type=?, purchase_date=?, insurance=?, insurance_expiry_date=? WHERE car_id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, carName);
            ps.setString(2, modelName);
            ps.setString(3, condition);
            ps.setString(4, purchaseDate);
            ps.setString(5, insurance);
            ps.setString(6, insuranceExpiry);
            ps.setInt(7, Integer.parseInt(carId));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                lblMessage.setText("Car updated successfully!");
            } else {
                lblMessage.setText("Error in updating car.");
            }
        } catch (SQLException e) {
            lblMessage.setText("Error: " + e.getMessage());
        }
    }

    private void deleteCar() {
        String carId = JOptionPane.showInputDialog(this, "Enter Car ID to Delete:");
        try {
            String sql = "DELETE FROM car2 WHERE car_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(carId));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                lblMessage.setText("Car deleted successfully!");
            } else {
                lblMessage.setText("Error in deleting car.");
            }
        } catch (SQLException e) {
            lblMessage.setText("Error: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtCarName.setText("");
        txtModelName.setText("");
        txtPurchaseDate.setText("");
        txtInsuranceExpiryDate.setText("");
        cbCondition.setSelectedIndex(0);
        cbInsurance.setSelectedIndex(0);
    }

    private void goBack() {
        dispose(); 
        LoginPage loginPage = new LoginPage();
        loginPage.showDashboard();
    }

    private void showCarData() {
        JFrame frame = new JFrame("Show Car Data");
        frame.setSize(800, 400);

        String[] columnNames = {"Car ID", "Car Name", "Model Name", "Condition", "Purchase Date", "Insurance", "Insurance Expiry", "Image Path"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        try {
            String query = "SELECT car.car_id, car_name, model_name, condition_type, purchase_date, insurance, insurance_expiry_date, image_path " +
                           "FROM car2 AS car LEFT JOIN car_images4 AS img ON car.car_id = img.car_id";
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("car_id"),
                        rs.getString("car_name"),
                        rs.getString("model_name"),
                        rs.getString("condition_type"),
                        rs.getString("purchase_date"),
                        rs.getString("insurance"),
                        rs.getString("insurance_expiry_date"),
                        rs.getString("image_path") != null ? rs.getString("image_path") : "No Image"
                };
                model.addRow(row);
            }

            frame.add(scrollPane);
            frame.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading car data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new democarmodule();
    }
}