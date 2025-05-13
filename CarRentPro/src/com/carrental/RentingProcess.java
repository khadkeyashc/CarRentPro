package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class RentingProcess extends JFrame {
    private JTable tableRequests;
    private JButton btnApprove, btnReject;
    private Connection connection;

    public RentingProcess() {
        setTitle("Renting Process");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableRequests = new JTable();
        add(new JScrollPane(tableRequests), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnApprove = new JButton("Approve");
        btnReject = new JButton("Reject");

        btnApprove.addActionListener(e -> approveRequest());
        btnReject.addActionListener(e -> rejectRequest());

        panelButtons.add(btnApprove);
        panelButtons.add(btnReject);

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

            // Populate table
            tableRequests.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading requests: " + e.getMessage());
        }
    }

    private void approveRequest() {
        try {
            int selectedRow = tableRequests.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a request.");
                return;
            }

            int employeeId = (int) tableRequests.getValueAt(selectedRow, 0);
            int rentalDuration = (int) tableRequests.getValueAt(selectedRow, 6); // Rental duration

            // Calculate start and end date
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(rentalDuration);

            // Update database with rental approval and dates
            String query = """
                UPDATE organization_employee 
                SET request_status = ?, rental_start_date = ?, rental_end_date = ? 
                WHERE employee_id = ?
            """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "Approved");
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            ps.setInt(4, employeeId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Request approved successfully.");
                loadPendingRequests(); // Reload the requests
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update request status.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error approving request: " + e.getMessage());
        }
    }

    private void rejectRequest() {
        try {
            int selectedRow = tableRequests.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a request.");
                return;
            }

            int employeeId = (int) tableRequests.getValueAt(selectedRow, 0);

            // Update request status to rejected
            String query = "UPDATE organization_employee SET request_status = ? WHERE employee_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "Rejected");
            ps.setInt(2, employeeId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Request rejected successfully.");
                loadPendingRequests(); // Reload the requests
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update request status.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error rejecting request: " + e.getMessage());
        }
    }

    // Function to build the table model for the requests
    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        // Get column names
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                row.add(rs.getObject(column));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // Main method to run the RentingProcess as a standalone window
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RentingProcess());
    }
}
