package org.example.ui.panels;

import org.example.database.entity.Product;
import org.example.database.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProductPanel extends JPanel {
    private ProductService productService;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField idField, brandnameField, productnameField, priceField, categoryField, featuresField;
    private JButton addButton, updateButton, deleteButton, refreshButton, findButton;

    public ProductPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));

        inputPanel.add(new JLabel("ID (for update/delete):"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Brand Name:"));
        brandnameField = new JTextField();
        inputPanel.add(brandnameField);

        inputPanel.add(new JLabel("Product Name:"));
        productnameField = new JTextField();
        inputPanel.add(productnameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Features (int):"));
        featuresField = new JTextField();
        inputPanel.add(featuresField);


        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        findButton = new JButton("Find Product by ID");
        refreshButton = new JButton("Refresh All Products");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(findButton);
        buttonPanel.add(refreshButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Brand", "Product", "Price", "Category", "Features", "Created At", "Updated At"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        findButton.addActionListener(e -> findProductById());
        refreshButton.addActionListener(e -> loadProducts());

        loadProducts();
    }

    private void addProduct() {
        try {
            String brandname = brandnameField.getText();
            String productname = productnameField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            String category = categoryField.getText();
            Integer features = Integer.valueOf(featuresField.getText());

            if (brandname.isEmpty() || productname.isEmpty() || priceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Brand Name, Product Name, and Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            productService.createProduct(brandname, productname, price, category, features);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearFields();
            loadProducts();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Price and Features.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        try {
            Long id = Long.valueOf(idField.getText());
            String brandname = brandnameField.getText();
            String productname = productnameField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            String category = categoryField.getText();
            Integer features = Integer.valueOf(featuresField.getText());

            if (brandname.isEmpty() || productname.isEmpty() || priceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Brand Name, Product Name, and Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product updatedProduct = productService.updateProduct(id, brandname, productname, price, category, features);
            if (updatedProduct != null) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                clearFields();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID, Price, and Features.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try {
            Long id = Long.valueOf(idField.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete product with ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productService.deleteProduct(id);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearFields();
                loadProducts();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findProductById() {
        try {
            Long id = Long.valueOf(idField.getText());
            Product product = productService.getProductById(id);
            if (product != null) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getBrandname(),
                    product.getProductname(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getFeatures(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
                });
                brandnameField.setText(product.getBrandname());
                productnameField.setText(product.getProductname());
                priceField.setText(product.getPrice().toString());
                categoryField.setText(product.getCategory());
                featuresField.setText(product.getFeatures().toString());
            } else {
                JOptionPane.showMessageDialog(this, "Product not found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadProducts();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error finding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getBrandname(),
                product.getProductname(),
                product.getPrice(),
                product.getCategory(),
                product.getFeatures(),
                product.getCreatedAt(),
                product.getUpdatedAt()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        brandnameField.setText("");
        productnameField.setText("");
        priceField.setText("");
        categoryField.setText("");
        featuresField.setText("");
    }
}