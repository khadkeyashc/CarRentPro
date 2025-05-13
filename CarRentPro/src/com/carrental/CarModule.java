package com.carrental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarModule extends JFrame {
    private JTextField txtCarName, txtModelName, txtSearch;
    private JDateChooser datePurchase, dateInsuranceExpiry;
    private JComboBox<String> cmbCondition, cmbInsurance;
    private JTable tableCars;
    private DefaultTableModel tableModel;
    private JButton btnSubmit, btnUpdate, btnDelete, btnShow, btnSearch, btnUploadImage, backButton;
    private List<String> imagePaths = new ArrayList<>();
    private Connection connection;

    public CarModule() {
        initializeDBConnection();
        setTitle("Car Module");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCarName = createTextFieldWithLabel(formPanel, "Car Name:");
        txtModelName = createTextFieldWithLabel(formPanel, "Model Name:");
        datePurchase = createDateChooserWithLabel(formPanel, "Purchase Date:");
        cmbCondition = createComboBoxWithLabel(formPanel, "Condition:", new String[]{"New", "Good", "Best", "Average"});
        cmbInsurance = createComboBoxWithLabel(formPanel, "Insurance:", new String[]{"Yes", "No"});
        cmbInsurance.addActionListener(e -> toggleInsuranceExpiry());
        dateInsuranceExpiry = createDateChooserWithLabel(formPanel, "Insurance Expiry Date:");
        dateInsuranceExpiry.setEnabled(false);

        btnUploadImage = new JButton("Upload Images");
        btnUploadImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUploadImage.addActionListener(e -> uploadImages());
        formPanel.add(btnUploadImage);

        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> insertCar());
        formPanel.add(btnSubmit);

        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> updateCar());
        formPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> deleteCar());
        formPanel.add(btnDelete);

        btnShow = new JButton("Show Cars");
        btnShow.addActionListener(e -> showCars());
        formPanel.add(btnShow);

        txtSearch = createTextFieldWithLabel(formPanel, "Search Car by Name:");
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchCar());
        formPanel.add(btnSearch);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            LoginPage loginPage = new LoginPage();
            loginPage.showDashboard();
        });
        formPanel.add(backButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Car ID", "Name", "Model", "Condition", "Insurance", "Expiry Date", "Image"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 6 ? ImageIcon.class : Object.class;
            }
        };
        tableCars = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(tableCars);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        getContentPane().add(formPanel, BorderLayout.WEST);
        getContentPane().add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTextField createTextFieldWithLabel(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(15);
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.add(label);
        subPanel.add(textField);
        panel.add(subPanel);
        return textField;
    }

    private JDateChooser createDateChooserWithLabel(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        JDateChooser dateChooser = new JDateChooser();
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.add(label);
        subPanel.add(dateChooser);
        panel.add(subPanel);
        return dateChooser;
    }

    private JComboBox<String> createComboBoxWithLabel(JPanel panel, String labelText, String[] options) {
        JLabel label = new JLabel(labelText);
        JComboBox<String> comboBox = new JComboBox<>(options);
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.add(label);
        subPanel.add(comboBox);
        panel.add(subPanel);
        return comboBox;
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void toggleInsuranceExpiry() {
        dateInsuranceExpiry.setEnabled("Yes".equals(cmbInsurance.getSelectedItem()));
        if (!dateInsuranceExpiry.isEnabled()) {
            dateInsuranceExpiry.setDate(null);
        }
    }

    private void uploadImages() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                imagePaths.add(file.getAbsolutePath());
            }
            JOptionPane.showMessageDialog(this, "Images uploaded successfully!");
        }
    }
    private boolean validateFields() {
        String carName = txtCarName.getText();
        String modelName = txtModelName.getText();
        if (carName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Car Name cannot be empty.");
            return false;
        }
        if (modelName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Model Name cannot be empty.");
            return false;
        }

        if (datePurchase.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Purchase Date must be selected.");
            return false;
        }

        if (cmbCondition.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select the car's condition.");
            return false;
        }

        if (cmbInsurance.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select the insurance status.");
            return false;
        }

        if ("Yes".equals(cmbInsurance.getSelectedItem()) && dateInsuranceExpiry.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select the insurance expiry date.");
            return false;
        }

        if (imagePaths.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please upload at least one image.");
            return false;
        }

        return true;
    }

    private void insertCar() {
    	if (!validateFields()) {
            return; 
        }
        try {
            String carName = txtCarName.getText();
            String modelName = txtModelName.getText();
            Date purchaseDate = new java.sql.Date(datePurchase.getDate().getTime());
            String condition = cmbCondition.getSelectedItem().toString();
            String insurance = cmbInsurance.getSelectedItem().toString();
            Date expiryDate = insurance.equals("Yes") ? new java.sql.Date(dateInsuranceExpiry.getDate().getTime()) : null;

            String insertCarQuery = "INSERT INTO car (car_name, model_name, purchase_date, condition_type, insurance, insurance_expiry_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(insertCarQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, carName);
            ps.setString(2, modelName);
            ps.setDate(3, purchaseDate);
            ps.setString(4, condition);
            ps.setString(5, insurance);
            ps.setDate(6, expiryDate);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int carId = 0;
            if (rs.next()) {
                carId = rs.getInt(1);
            }

            String insertImageQuery = "INSERT INTO car_images (car_id, image_path) VALUES (?, ?)";
            for (String imagePath : imagePaths) {
                PreparedStatement imgPs = connection.prepareStatement(insertImageQuery);
                imgPs.setInt(1, carId);
                imgPs.setString(2, imagePath);
                imgPs.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Car added successfully!");
            imagePaths.clear();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding car: " + e.getMessage());
        }
    }

    private void updateCar() {
    	if (!validateFields()) {
            return; 
        }
        try {
        	
            int selectedRow = tableCars.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a car to update.");
                return;
            }

            int carId = (int) tableModel.getValueAt(selectedRow, 0);
            String carName = txtCarName.getText();
            String modelName = txtModelName.getText();
            Date purchaseDate = new java.sql.Date(datePurchase.getDate().getTime());
            String condition = cmbCondition.getSelectedItem().toString();
            String insurance = cmbInsurance.getSelectedItem().toString();
            Date expiryDate = insurance.equals("Yes") ? new java.sql.Date(dateInsuranceExpiry.getDate().getTime()) : null;

            String updateCarQuery = "UPDATE car SET car_name = ?, model_name = ?, purchase_date = ?, condition_type = ?, insurance = ?, insurance_expiry_date = ? WHERE car_id = ?";
            PreparedStatement ps = connection.prepareStatement(updateCarQuery);
            ps.setString(1, carName);
            ps.setString(2, modelName);
            ps.setDate(3, purchaseDate);
            ps.setString(4, condition);
            ps.setString(5, insurance);
            ps.setDate(6, expiryDate);
            ps.setInt(7, carId);
            ps.executeUpdate();

            if (!imagePaths.isEmpty()) {
                String deleteOldImagesQuery = "DELETE FROM car_images WHERE car_id = ?";
                PreparedStatement deletePs = connection.prepareStatement(deleteOldImagesQuery);
                deletePs.setInt(1, carId);
                deletePs.executeUpdate();

                String insertImageQuery = "INSERT INTO car_images (car_id, image_path) VALUES (?, ?)";
                for (String imagePath : imagePaths) {
                    PreparedStatement imgPs = connection.prepareStatement(insertImageQuery);
                    imgPs.setInt(1, carId);
                    imgPs.setString(2, imagePath);
                    imgPs.executeUpdate();
                }
                imagePaths.clear();
            }

            JOptionPane.showMessageDialog(null, "Car updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating car: " + e.getMessage());
        }
    }
    
    
    private void deleteCar() {
    	if (!validateFields()) {
            return; 
        }
        try {
            int selectedRow = tableCars.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a car to delete.");
                return;
            }

            int carId = (int) tableModel.getValueAt(selectedRow, 0);

            String deleteCarQuery = "DELETE FROM car WHERE car_id = ?";
            PreparedStatement ps = connection.prepareStatement(deleteCarQuery);
            ps.setInt(1, carId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Car deleted successfully!");
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting car: " + e.getMessage());
        }
    }
    private void searchCar() {
        try {
            String carNameSearch = txtSearch.getText().trim(); 
            if (carNameSearch.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a car name to search.");
                return; 
            }

            tableModel.setRowCount(0); 

            String searchQuery = "SELECT * FROM car WHERE car_name LIKE ?";
            PreparedStatement ps = connection.prepareStatement(searchQuery);
            ps.setString(1, "%" + carNameSearch + "%");
            ResultSet rs = ps.executeQuery();

                       if (!rs.isBeforeFirst()) { 
                JOptionPane.showMessageDialog(null, "No cars found with the name: " + carNameSearch);
            } else {
                while (rs.next()) {
                    int carId = rs.getInt("car_id");
                    String carName = rs.getString("car_name");
                    String modelName = rs.getString("model_name");
                    String condition = rs.getString("condition_type");
                    String insurance = rs.getString("insurance");
                    Date expiryDate = rs.getDate("insurance_expiry_date");

                    tableModel.addRow(new Object[] { carId, carName, modelName, condition, insurance, expiryDate });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching car: " + e.getMessage());
        }
    }


    private void showCars() {
        try {
            tableModel.setRowCount(0); 
            String showCarsQuery = "SELECT c.car_id, c.car_name, c.model_name, c.condition_type, c.insurance, c.insurance_expiry_date, ci.image_path " +
                                   "FROM car c " +
                                   "LEFT JOIN car_images ci ON c.car_id = ci.car_id";
            PreparedStatement ps = connection.prepareStatement(showCarsQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String carName = rs.getString("car_name");
                String modelName = rs.getString("model_name");
                String condition = rs.getString("condition_type");
                String insurance = rs.getString("insurance");
                Date expiryDate = rs.getDate("insurance_expiry_date");
                String imagePath = rs.getString("image_path");

                ImageIcon carImage = null;
                if (imagePath != null) {
                    carImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                }
                tableModel.addRow(new Object[] { carId, carName, modelName, condition, insurance, expiryDate, carImage, imagePath });
            }

            tableCars.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = tableCars.rowAtPoint(e.getPoint());
                    int col = tableCars.columnAtPoint(e.getPoint());

                    if (col == 6) {
                        String imagePath = (String) tableCars.getValueAt(row, 7);

                        if (imagePath != null) {
                            ImageIcon fullImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH));
                            JOptionPane.showMessageDialog(null, new JLabel(fullImage), "Car Image", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            });


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error showing cars: " + e.getMessage());
        }
    }

    private void showImageInDialog(String imagePath) {
        JFrame imageFrame = new JFrame("Car Image");
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);

        JScrollPane scrollPane = new JScrollPane(imageLabel);
        imageFrame.add(scrollPane);

        imageFrame.setSize(600, 600); 
        imageFrame.setLocationRelativeTo(null); 
        imageFrame.setVisible(true);
    }



    public static void main(String[] args) {
        new CarModule();
    }
}
