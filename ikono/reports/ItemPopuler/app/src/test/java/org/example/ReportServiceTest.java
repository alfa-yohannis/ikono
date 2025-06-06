package org.example;

import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction; // Pastikan import Transaction dari org.hibernate
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections; // Import untuk Collections.emptyList() jika digunakan
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Transaction mockTransaction; // org.hibernate.Transaction
    @Mock
    private Query<Object[]> mockQuery;
    @Mock
    private HibernateUtil mockHibernateUtilInstance; // Mock untuk instance HibernateUtil

    private ReportService reportService; // Ini adalah instance ReportService asli, bukan interface/decorator
    private MockedStatic<HibernateUtil> mockedHibernateUtil;

    @BeforeEach
    void setUp() {
        // Penting: ReportService di sini adalah implementasi konkret, bukan yang didekorasi,
        // karena kita menguji logika ReportService itu sendiri.
        // Jika Anda ingin menguji decorator, Anda akan membuat instance decorator di sini.
        reportService = new ReportService();

        // Mulai mock static HibernateUtil
        mockedHibernateUtil = Mockito.mockStatic(HibernateUtil.class);

        // Atur mock untuk HibernateUtil.getInstance() mengembalikan mock instance kita
        mockedHibernateUtil.when(HibernateUtil::getInstance).thenReturn(mockHibernateUtilInstance);

        // Atur mock untuk mockHibernateUtilInstance.getSessionFactory() mengembalikan mockSessionFactory
        when(mockHibernateUtilInstance.getSessionFactory()).thenReturn(mockSessionFactory);

        // Perilaku mock lainnya
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        when(mockSession.getTransaction()).thenReturn(mockTransaction); // Penting untuk commit/rollback
        when(mockSession.createQuery(anyString(), eq(Object[].class))).thenReturn(mockQuery);
    }

    @AfterEach
    void tearDown() {
        // Tutup mock static setelah setiap tes
        if (mockedHibernateUtil != null) {
            mockedHibernateUtil.close();
        }
    }

    @Test
    void getPopularItems_shouldReturnItemReports_whenDataExists() {
        // 1. Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{"Product A", 100L});
        mockResults.add(new Object[]{"Product B", 75L});

        when(mockQuery.setParameter("startDate", startDate)).thenReturn(mockQuery);
        when(mockQuery.setParameter("endDate", endDate)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockResults);

        // 2. Act
        ObservableList<ItemReport> result = reportService.getPopularItems(startDate, endDate);

        // 3. Assert
        assertNotNull(result, "Hasil tidak boleh null");
        assertEquals(2, result.size(), "Harusnya ada 2 item report");

        assertEquals("Product A", result.get(0).productNameProperty().get());
        assertEquals(100, result.get(0).quantityProperty().get());

        assertEquals("Product B", result.get(1).productNameProperty().get());
        assertEquals(75, result.get(1).quantityProperty().get());

        // Verifikasi
        mockedHibernateUtil.verify(HibernateUtil::getInstance);
        verify(mockHibernateUtilInstance).getSessionFactory();
        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString(), eq(Object[].class));
        verify(mockQuery).setParameter("startDate", startDate);
        verify(mockQuery).setParameter("endDate", endDate);
        verify(mockQuery).getResultList();
        verify(mockTransaction).commit();
        verify(mockSession).close();
        verify(mockTransaction, never()).rollback();
    }

    @Test
    void getPopularItems_shouldReturnEmptyList_whenNoDataExists() {
        // 1. Arrange
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 28);

        List<Object[]> mockResults = Collections.emptyList(); // atau new ArrayList<>();

        when(mockQuery.setParameter("startDate", startDate)).thenReturn(mockQuery);
        when(mockQuery.setParameter("endDate", endDate)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockResults);

        // 2. Act
        ObservableList<ItemReport> result = reportService.getPopularItems(startDate, endDate);

        // 3. Assert
        assertNotNull(result, "Hasil tidak boleh null");
        assertTrue(result.isEmpty(), "Daftar item report harusnya kosong");
        assertEquals(0, result.size(), "Ukuran daftar item report harusnya 0");

        // Verifikasi
        mockedHibernateUtil.verify(HibernateUtil::getInstance);
        verify(mockHibernateUtilInstance).getSessionFactory();
        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString(), eq(Object[].class));
        verify(mockQuery).setParameter("startDate", startDate);
        verify(mockQuery).setParameter("endDate", endDate);
        verify(mockQuery).getResultList();
        verify(mockTransaction).commit();
        verify(mockSession).close();
        verify(mockTransaction, never()).rollback();
    }
}