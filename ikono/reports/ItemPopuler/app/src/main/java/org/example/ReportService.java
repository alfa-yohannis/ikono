// ReportService.java (Versi Hibernate)
package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query; // Gunakan Query dari org.hibernate.query

import java.time.LocalDate;
import java.util.List;

public class ReportService implements ReportServiceInterface {

    // Konstruktor ReportService tidak lagi memerlukan DatabaseConnection
    // karena HibernateUtil yang akan mengelola SessionFactory
    public ReportService() {
        // Inisialisasi HibernateUtil akan dilakukan secara statis
        // Anda mungkin ingin menambahkan logika shutdown di App.stop()
    }
    
    @Override
    public ObservableList<ItemReport> getPopularItems(LocalDate startDate, LocalDate endDate) {
        ObservableList<ItemReport> reports = FXCollections.observableArrayList();
        Session session = null;
        try {
        	session = HibernateUtil.getInstance().getSessionFactory().openSession();
            session.beginTransaction();

            // Gunakan HQL (Hibernate Query Language)
            // HQL beroperasi pada NAMA KELAS ENTITAS (Transaction, Product), bukan nama tabel!
            String hql = "SELECT p.productName, SUM(t.quantity) " +
                         "FROM Transaction t JOIN t.product p " +
                         "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
                         "GROUP BY p.productName " +
                         "ORDER BY SUM(t.quantity) DESC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                String productName = (String) row[0];
                Long totalQuantity = (Long) row[1]; // SUM mengembalikan Long
                reports.add(new ItemReport(productName, totalQuantity.intValue()));
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaksi jika ada error
            }
            e.printStackTrace(); // Penting untuk debugging
            // Throw atau tangani error sesuai kebutuhan aplikasi Anda
            // Misalnya, lemparkan SQLException baru jika ingin tetap konsisten dengan API sebelumnya
            throw new RuntimeException("Gagal memuat laporan populer: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close(); // Pastikan session selalu ditutup
            }
        }
        return reports;
    }
}