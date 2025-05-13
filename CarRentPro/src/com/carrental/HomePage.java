package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class HomePage {

    private JFrame frame;

    public HomePage() {
        frame = new JFrame("Welcome to Commercial Car Rental System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLocationRelativeTo(null);  
        frame.getContentPane().setBackground(new Color(245, 245, 245));
        
        frame.getContentPane().setLayout(new BorderLayout());
        
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(50, 65, 85));
        headingPanel.setPreferredSize(new Dimension(800, 100));

        JLabel headingLabel = new JLabel("Commercial Car Rental System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);
        headingPanel.add(headingLabel);

        frame.getContentPane().add(headingPanel, BorderLayout.NORTH);

        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        
        
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(15, 15, 15, 15);

       
        JTextArea projectDetails = new JTextArea(5, 30);
        projectDetails.setEditable(false);
        projectDetails.setFont(new Font("Arial", Font.PLAIN, 16));
        projectDetails.setText("Welcome to the Commercial Car Rental System. This system allows you to manage car rentals efficiently for individual customers and organizations. The system includes the following features:\n\n"
                + "1. Customer Registration and Login\n"
                + "2. Admin Dashboard\n"
                + "3. Organization Registration\n"
                + "4. Car Management\n"
                + "5. Rental Management\n");
        projectDetails.setWrapStyleWord(true);
        projectDetails.setLineWrap(true);
        projectDetails.setBackground(new Color(245, 245, 245));
        projectDetails.setCaretPosition(0);
        projectDetails.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        gbc1.gridx = 0;
        gbc1.gridy = 0;
        contentPanel.add(projectDetails, gbc1);

        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(15, 15, 15, 15);
        
        
        ImageIcon carImageIcon = new ImageIcon("/com.carrental/Img/Login.png");  
        JLabel carImageLabel = new JLabel(carImageIcon);
        gbc2.gridy = 1;
        contentPanel.add(carImageLabel, gbc2);

        
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(15, 15, 15, 15);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); 
        buttonPanel.setBackground(new Color(50, 65, 85));  

        
        ImageIcon loginIcon = new ImageIcon("D:\\login.png");  
        Image iconImage = loginIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);  
        JLabel iconLabel = new JLabel(new ImageIcon(iconImage));  

        
        JLabel textLabel = new JLabel("Login/Register");
        textLabel.setForeground(Color.WHITE);  
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));

       
        buttonPanel.add(iconLabel);
        buttonPanel.add(textLabel);

        
        JButton loginButton = new JButton();
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(50, 65, 85));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(250, 100));
        loginButton.setIcon(null);  
        loginButton.add(buttonPanel); 

        
        loginButton.addActionListener(e -> openLoginRegisterOptions());
        gbc3.gridx = 0;
        gbc3.gridy = 2;
        contentPanel.add(loginButton, gbc3);

        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(50, 65, 85));
        footerPanel.setPreferredSize(new Dimension(800, 50));
        JLabel footerLabel = new JLabel("Developed by Yash Chandrashekhar Khadke & Prathamesh Kailas Pote");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    
    private void openLoginRegisterOptions() {
        String[] options = {"Customer", "Admin", "Organization"};
        int choice = JOptionPane.showOptionDialog(frame, "Select the role to proceed", 
                "Login/Register", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        System.out.println("Selected option: " + choice); 

        if (choice == 0) {
            
            new CustomerLogin();
        } else if (choice == 1) {
           
            System.out.println("Opening RegisterPage..."); 
            RegisterPage.showRegisterPage(); 
        } else if (choice == 2) {
            
            new OrganizationLogin();
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
