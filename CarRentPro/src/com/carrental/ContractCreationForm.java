package com.carrental;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ContractCreationForm extends JFrame {

    private JComboBox<String> carComboBox;
    private JComboBox<String> employeeComboBox;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JButton createContractButton;
    private Connection conn;

    public ContractCreationForm() {
        setTitle("Contract Creation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new GridLayout(6, 2));

        getContentPane().setBackground(new Color(58, 67, 82));

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456"); 
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel carLabel = new JLabel("Select Car:");
        carLabel.setForeground(new Color(255, 215, 0)); 
        getContentPane().add(carLabel);
        
        carComboBox = new JComboBox<>();
        carComboBox.setBackground(new Color(255, 255, 240));  
        loadCars();
        getContentPane().add(carComboBox);

        JLabel employeeLabel = new JLabel("Select Employee (Organization):");
        employeeLabel.setForeground(new Color(255, 215, 0));  
        getContentPane().add(employeeLabel);
        
        employeeComboBox = new JComboBox<>();
        employeeComboBox.setBackground(new Color(255, 255, 240));  
        loadEmployees();
        getContentPane().add(employeeComboBox);

        JLabel startDateLabel = new JLabel("Contract Start Date:");
        startDateLabel.setForeground(new Color(255, 215, 0));  
        getContentPane().add(startDateLabel);

        startDateChooser = new JDateChooser();
        startDateChooser.setBackground(new Color(255, 255, 240));  
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        getContentPane().add(startDateChooser);

        JLabel endDateLabel = new JLabel("Contract End Date:");
        endDateLabel.setForeground(new Color(255, 215, 0));  
        getContentPane().add(endDateLabel);

        endDateChooser = new JDateChooser();
        endDateChooser.setBackground(new Color(255, 255, 240));  
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        getContentPane().add(endDateChooser);

        createContractButton = new JButton("Create Contract");
        createContractButton.setBackground(new Color(255, 215, 0));  
        createContractButton.setForeground(new Color(58, 67, 82));  
        createContractButton.setFont(new Font("Arial", Font.BOLD, 14));
        createContractButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        createContractButton.addActionListener(this::createContract);
        getContentPane().add(createContractButton);

        setVisible(true);
    }

    private void loadCars() {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT car_id, car_name, model_name FROM car2";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String carDetails = rs.getInt("car_id") + " - " + rs.getString("car_name") + " (" + rs.getString("model_name") + ")";
                carComboBox.addItem(carDetails);
            }

            if (carComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No cars available in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmployees() {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT employee_id, org_name FROM organization_employee WHERE request_status = 'Approved'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String employeeDetails = rs.getInt("employee_id") + " - " + rs.getString("org_name");
                employeeComboBox.addItem(employeeDetails);
            }

            if (employeeComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No approved employees available.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createContract(ActionEvent e) {
        try {
            int carIndex = carComboBox.getSelectedIndex();
            int employeeIndex = employeeComboBox.getSelectedIndex();

            if (carIndex == -1 || employeeIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select both a car and an employee.");
                return;
            }
            if (startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
                return;
            }

            if (startDateChooser.getDate().after(endDateChooser.getDate())) {
                JOptionPane.showMessageDialog(this, "End date must be after the start date.");
                return;
            }

            String selectedCar = (String) carComboBox.getSelectedItem();
            String selectedEmployee = (String) employeeComboBox.getSelectedItem();
            int carId = Integer.parseInt(selectedCar.split(" - ")[0]);
            int employeeId = Integer.parseInt(selectedEmployee.split(" - ")[0]);

            String customerQuery = "SELECT customer_id FROM car2 WHERE car_id = ?";
            PreparedStatement psCustomer = conn.prepareStatement(customerQuery);
            psCustomer.setInt(1, carId);
            ResultSet rsCustomer = psCustomer.executeQuery();

            Integer customerId = null;
            if (rsCustomer.next()) {
                customerId = rsCustomer.getInt("customer_id");
            }

            if (customerId == null) {
                JOptionPane.showMessageDialog(this, "No customer found for the selected car.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = sdf.format(startDateChooser.getDate());
            String endDate = sdf.format(endDateChooser.getDate());

            String insertQuery = "INSERT INTO rental_contract2 (car_id, employee_id, customer_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setInt(1, carId);
            ps.setInt(2, employeeId);
            ps.setInt(3, customerId);
            ps.setString(4, startDate);
            ps.setString(5, endDate);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Contract Created Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create the contract.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ContractCreationForm();
    }
}
