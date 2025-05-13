package com.carrental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminRentalApproval extends JFrame {
    private JTable tableRequests;
    private JButton btnApprove, btnReject, btnCreateContract, btnBack;
    private Connection connection;

    public AdminRentalApproval() {
        setTitle("Admin Rental Approval");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        tableRequests = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableRequests, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnApprove = new JButton("Approve");
        btnReject = new JButton("Reject");
        btnCreateContract = new JButton("Create Contract");
        btnBack = new JButton("Back");

        btnApprove.addActionListener(e -> updateRequestStatus("Approved"));
        btnReject.addActionListener(e -> updateRequestStatus("Rejected"));
        btnCreateContract.addActionListener(e -> showContractCreationForm());
        btnBack.addActionListener(e -> {
            dispose();
            LoginPage loginPage = new LoginPage();
            loginPage.showDashboard();
        });

        panelButtons.add(btnApprove);
        panelButtons.add(btnReject);
        panelButtons.add(btnCreateContract); 
        panelButtons.add(btnBack);

        add(panelButtons, BorderLayout.SOUTH);

        initializeDBConnection();
        loadPendingRequests();
        setVisible(true);
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void loadPendingRequests() {
        try {
            String query = """
                SELECT employee_id, org_name, contact, email, position, price_per_month, rental_duration, request_status
                FROM organization_employee WHERE request_status = 'Pending';
            """;
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            
            tableRequests.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading requests: " + e.getMessage());
        }
    }

    private void updateRequestStatus(String status) {
        try {
            int selectedRow = tableRequests.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a request.");
                return;
            }

            int employeeId = Integer.parseInt(tableRequests.getValueAt(selectedRow, 0).toString());

            String query = "UPDATE organization_employee SET request_status = ? WHERE employee_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, employeeId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Request " + status.toLowerCase() + " successfully.");
                loadPendingRequests();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update request status.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating status: " + e.getMessage());
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

       
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    private void showContractCreationForm() {
        new ContractCreationForm(); 
    }

    public static void main(String[] args) {
        new AdminRentalApproval();
    }
}
