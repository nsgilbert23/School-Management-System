package sms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkRemember;
    private JLabel lblMessage;
    private JProgressBar progressBar;

    public LoginFrame() {
        setTitle("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        add(txtUsername, gbc);

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

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton btnLogin = new JButton("Login");
        JButton btnReset = new JButton("Reset");
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnReset);
        gbc.gridx = 1; gbc.gridy = 3;
        add(buttonPanel, gbc);

        // Message label
        gbc.gridx = 1; gbc.gridy = 4;
        lblMessage = new JLabel(" ");
        lblMessage.setForeground(Color.RED);
        add(lblMessage, gbc);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        gbc.gridx = 1; gbc.gridy = 5;
        add(progressBar, gbc);

        // Button actions
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtUsername.setText("");
                txtPassword.setText("");
                chkRemember.setSelected(false);
                lblMessage.setText("");
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.equals("admin") && password.equals("admin")) {
            lblMessage.setForeground(Color.GREEN);
            lblMessage.setText("Login successful! Loading...");
            progressBar.setVisible(true);

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(20);
                        publish(i);
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int value = chunks.get(chunks.size() - 1);
                    progressBar.setValue(value);
                }

                @Override
                protected void done() {
                    new MainFrame().setVisible(true);
                    dispose();
                }
            };
            worker.execute();
        } else {
            lblMessage.setText("Invalid username or password");
            progressBar.setVisible(false);
        }
    }
}