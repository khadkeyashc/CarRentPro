package com.carrental;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private JButton btnApproveRequests;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        btnApproveRequests = new JButton("Approve Rental Requests");
        btnApproveRequests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminRentalApproval(); 
            }
        });

        add(btnApproveRequests);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
