package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CustomerDashboard2 extends JFrame {
    private JTable tableDetails;
    private Connection connection;

    public CustomerDashboard2(int customerId) {
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableDetails = new JTable();
        add(new JScrollPane(tableDetails));

        initializeDBConnection();
        fetchRentalDetails(customerId);
        setVisible(true);
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
    }

    private void fetchRentalDetails(int customerId) {
        try {
            String query = """
                SELECT c.car_name, e.org_name, e.position, e.price_per_month, e.rental_duration
                FROM rental_contract2 rc
                INNER JOIN car2 c ON rc.car_id = c.car_id
                INNER JOIN organization_employee e ON rc.employee_id = e.employee_id
                WHERE rc.customer_id = ?;
            """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No rental details found for this customer.");
            }
            tableDetails.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching rental details: " + e.getMessage());
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = rs.getObject(i);
            }
            model.addRow(rowData);
        }

        return model;
    }

}
