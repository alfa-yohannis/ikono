package org.example.ui;

import org.example.database.dao.ProductDao;
import org.example.database.dao.UserDao;
import org.example.database.service.ProductService;
import org.example.database.service.UserService;
import org.example.util.observer.EventPublisher;
import org.example.util.observer.UserUpdateListener;
import org.example.ui.panels.UserPanel;
import org.example.ui.panels.ProductPanel;
import org.example.config.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private UserService userService;
    private ProductService productService;
    private EventPublisher eventPublisher;

    public MainFrame() {
        setTitle("Gradle Project UI - User & Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        eventPublisher = new EventPublisher();
        UserDao userDao = new UserDao();
        ProductDao productDao = new ProductDao();
        userService = new UserService(userDao, eventPublisher);
        productService = new ProductService(productDao, eventPublisher);

        eventPublisher.subscribe("userCreated", new UserUpdateListener("Logger"));
        eventPublisher.subscribe("userUpdated", new UserUpdateListener("Notifier"));
        eventPublisher.subscribe("productCreated", (eventType, data) -> System.out.println("Product Created: " + data));

        JTabbedPane tabbedPane = new JTabbedPane();

        UserPanel userPanel = new UserPanel(userService);
        ProductPanel productPanel = new ProductPanel(productService);

        tabbedPane.addTab("Users", userPanel);
        tabbedPane.addTab("Products", productPanel);

        add(tabbedPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HibernateUtil.shutdown();
                System.out.println("Hibernate SessionFactory closed.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}