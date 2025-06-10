package org.example.ui.panels;

import org.example.database.entity.Users;
import org.example.database.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, idField;
    private JButton addButton, updateButton, deleteButton, refreshButton, findButton;

    public UserPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("User Information"));

        inputPanel.add(new JLabel("ID (for update/delete):"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        addButton = new JButton("Add User");
        updateButton = new JButton("Update User");
        deleteButton = new JButton("Delete User");
        findButton = new JButton("Find User by ID");
        refreshButton = new JButton("Refresh All Users");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(findButton);
        buttonPanel.add(refreshButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        findButton.addActionListener(e -> findUserById());
        refreshButton.addActionListener(e -> loadUsers());

        loadUsers();
    }

    private void addUser() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userService.createUser(name, email);
            JOptionPane.showMessageDialog(this, "User added successfully!");
            clearFields();
            loadUsers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        try {
            Long id = Long.valueOf(idField.getText());
            String name = nameField.getText();
            String email = emailField.getText();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Users updatedUser = userService.updateUser(id, name, email);
            if (updatedUser != null) {
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                clearFields();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "User not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        try {
            Long id = Long.valueOf(idField.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user with ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userService.deleteUser(id);
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                clearFields();
                loadUsers();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
         catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findUserById() {
        try {
            Long id = Long.valueOf(idField.getText());
            Users user = userService.getUserById(id);
            if (user != null) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{user.getId(), user.getNama(), user.getEmail()});
                nameField.setText(user.getNama());
                emailField.setText(user.getEmail());
            } else {
                JOptionPane.showMessageDialog(this, "User not found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadUsers();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error finding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<Users> users = userService.getAllUsers();
        for (Users user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getNama(), user.getEmail()});
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
    }
}