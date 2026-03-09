package sms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sms.dao.DatabaseConnection;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JCheckBox chkRemember;
    private JLabel lblMessage;
    private JProgressBar progressBar;

    public LoginFrame() {

        setTitle("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setBackground(new Color(126, 174, 86));
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        add(txtEmail, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        add(txtPassword, gbc);

        // Remember me
        gbc.gridx = 1; gbc.gridy = 2;
        chkRemember = new JCheckBox("Remember Me");
        add(chkRemember, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnLogin = new JButton("Login");
        JButton btnReset = new JButton("Reset");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnReset);

        gbc.gridx = 1; gbc.gridy = 3;
        add(buttonPanel, gbc);

        // Message
        gbc.gridx = 1; gbc.gridy = 4;
        lblMessage = new JLabel(" ");
        lblMessage.setForeground(Color.RED);
        add(lblMessage, gbc);

        // Progress bar
        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        gbc.gridx = 1; gbc.gridy = 5;
        add(progressBar, gbc);

        // Login button action
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Reset button
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtEmail.setText("");
                txtPassword.setText("");
                chkRemember.setSelected(false);
                lblMessage.setText("");
                progressBar.setValue(0);
                progressBar.setVisible(false);
            }
        });

    }

    private void performLogin() {

        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        boolean valid = DatabaseConnection.login(email, password);

        if(valid){

            lblMessage.setForeground(Color.GREEN);
            lblMessage.setText("Login successful! Loading...");
            progressBar.setVisible(true);

            SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){

                @Override
                protected Void doInBackground() throws Exception {

                    for(int i=0;i<=100;i++){
                        Thread.sleep(20);
                        publish(i);
                    }

                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks){

                    int value = chunks.get(chunks.size()-1);
                    progressBar.setValue(value);

                }

                @Override
                protected void done(){

                    new MainFrameDesign().setVisible(true);
                    dispose();

                }

            };

            worker.execute();

        }else{

            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Invalid email or password");
            progressBar.setVisible(false);

        }

    }

}