package org.example.service;

import org.example.model.SalesReturn;
import org.example.patterns.observer.Observer;
import org.example.repository.SalesReturnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Menggunakan ekstensi Mockito untuk JUnit 5
class SalesReturnServiceTest {

    @Mock // Membuat mock object untuk SalesReturnRepository
    private SalesReturnRepository mockRepository;

    @Mock // Membuat mock object untuk Observer (bisa lebih dari satu jika perlu diuji)
    private Observer mockObserver1;
    
    @Mock
    private Observer mockObserver2;

    @InjectMocks // Membuat instance SalesReturnServiceImpl dan menginject mockRepository ke dalamnya
    private SalesReturnServiceImpl salesReturnService; // Gunakan implementasi konkret untuk pengujian

    private SalesReturn sampleReturn;

    @BeforeEach
    void setUp() {
        // Tidak perlu manual instantiation jika menggunakan @InjectMocks
        // salesReturnService = new SalesReturnServiceImpl(mockRepository);
        
        // Daftarkan mock observers ke service
        salesReturnService.registerObserver(mockObserver1);
        salesReturnService.registerObserver(mockObserver2);

        sampleReturn = new SalesReturn("RMA_TEST001", "NOTA_TEST001", "Pelanggan Test", "ITEM_TEST01", "Deskripsi Test", 1, "Rusak", "Kredit Toko", "Komentar Test");
        sampleReturn.setId(1); // Asumsikan ID sudah ada untuk beberapa skenario
    }

    @Test
    void createReturn_shouldSaveAndNotifyObservers_whenValidReturn() {
        // Arrange
        SalesReturn newReturnInput = new SalesReturn("RMA001", "N001", "CustA", "ITEM01", "DescA", 1, "ReasonA", "MethodA", "CommentA");
        SalesReturn savedReturnFromRepo = new SalesReturn("RMA001", "N001", "CustA", "ITEM01", "DescA", 1, "ReasonA", "MethodA", "CommentA");
        savedReturnFromRepo.setId(1); // Repository akan mengembalikan objek dengan ID

        when(mockRepository.save(any(SalesReturn.class))).thenReturn(savedReturnFromRepo);

        // Act
        SalesReturn result = salesReturnService.createReturn(newReturnInput);

        // Assert
        assertNotNull(result);
        assertEquals(savedReturnFromRepo.getId(), result.getId());
        assertEquals(newReturnInput.getRmaNumber(), result.getRmaNumber());

        // Verifikasi bahwa repository.save dipanggil sekali
        ArgumentCaptor<SalesReturn> salesReturnCaptor = ArgumentCaptor.forClass(SalesReturn.class);
        verify(mockRepository, times(1)).save(salesReturnCaptor.capture());
        assertEquals("RMA001", salesReturnCaptor.getValue().getRmaNumber());


        // Verifikasi bahwa semua observer diberitahu dengan aksi "CREATE"
        ArgumentCaptor<SalesReturn> observerSalesReturnCaptor = ArgumentCaptor.forClass(SalesReturn.class);
        ArgumentCaptor<String> observerActionCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockObserver1, times(1)).update(observerSalesReturnCaptor.capture(), observerActionCaptor.capture());
        assertEquals(savedReturnFromRepo.getRmaNumber(), observerSalesReturnCaptor.getValue().getRmaNumber());
        assertEquals("CREATE", observerActionCaptor.getValue());
        
        // Reset captor jika digunakan untuk observer kedua, atau gunakan captor terpisah
        // Atau cukup verifikasi pemanggilan dengan parameter yang sama
        verify(mockObserver2, times(1)).update(observerSalesReturnCaptor.getValue(), observerActionCaptor.getValue());

    }

    @Test
    void createReturn_shouldThrowException_whenQuantityIsZeroOrLess() {
        // Arrange
        SalesReturn invalidReturn = new SalesReturn("RMA002", "N002", "CustB", "ITEM02", "DescB", 0, "ReasonB", "MethodB", "CommentB");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salesReturnService.createReturn(invalidReturn);
        });
        assertEquals("Kuantitas harus lebih besar dari 0.", exception.getMessage());

        // Verifikasi bahwa repository.save() dan notifyObservers() tidak pernah dipanggil
        verify(mockRepository, never()).save(any(SalesReturn.class));
        verify(mockObserver1, never()).update(any(SalesReturn.class), anyString());
        verify(mockObserver2, never()).update(any(SalesReturn.class), anyString());
    }
    
    @Test
    void createReturn_shouldThrowException_whenSalesReturnIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salesReturnService.createReturn(null);
        });
        assertEquals("Objek SalesReturn tidak boleh null.", exception.getMessage());
        verify(mockRepository, never()).save(any(SalesReturn.class));
    }


    @Test
    void getReturnById_shouldReturnSalesReturn_whenFound() {
        // Arrange
        when(mockRepository.findById(sampleReturn.getId())).thenReturn(Optional.of(sampleReturn));

        // Act
        Optional<SalesReturn> result = salesReturnService.getReturnById(sampleReturn.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleReturn.getRmaNumber(), result.get().getRmaNumber());
        verify(mockRepository, times(1)).findById(sampleReturn.getId());
    }

     @Test
    void getReturnById_shouldReturnEmptyOptional_whenNotFound() {
        // Arrange
        int nonExistentId = 999;
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<SalesReturn> result = salesReturnService.getReturnById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());
        verify(mockRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void getAllReturns_shouldReturnListOfSalesReturns() {
        // Arrange
        List<SalesReturn> expectedList = new ArrayList<>();
        expectedList.add(sampleReturn);
        expectedList.add(new SalesReturn("RMA002", "N002", "CustB", "ITEM02", "DescB", 2, "ReasonB", "MethodB", "CommentB"));
        when(mockRepository.findAll()).thenReturn(expectedList);

        // Act
        List<SalesReturn> resultList = salesReturnService.getAllReturns();

        // Assert
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(sampleReturn.getRmaNumber(), resultList.get(0).getRmaNumber());
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void updateReturn_shouldCallRepositoryUpdateAndNotifyObservers_whenReturnExists() {
        // Arrange
        SalesReturn updatedInfo = new SalesReturn(sampleReturn.getRmaNumber(), sampleReturn.getReceiptNumber(), "Pelanggan Test Updated", sampleReturn.getItemId(), "Deskripsi Updated", 2, sampleReturn.getReturnReason(), sampleReturn.getRefundMethod(), "Komentar Updated");
        updatedInfo.setId(sampleReturn.getId()); // ID harus sama

        when(mockRepository.findById(sampleReturn.getId())).thenReturn(Optional.of(sampleReturn)); // Pastikan findById mengembalikan data
        // void method, tidak perlu when(...).thenReturn(...) untuk mockRepository.update()
        // doNothing().when(mockRepository).update(any(SalesReturn.class)); // jika eksplisit diperlukan

        // Act
        salesReturnService.updateReturn(updatedInfo);

        // Assert
        ArgumentCaptor<SalesReturn> salesReturnCaptor = ArgumentCaptor.forClass(SalesReturn.class);
        verify(mockRepository, times(1)).update(salesReturnCaptor.capture());
        assertEquals("Pelanggan Test Updated", salesReturnCaptor.getValue().getCustomerName());
        assertEquals(2, salesReturnCaptor.getValue().getQuantity());

        // Verifikasi notifikasi ke observer
        ArgumentCaptor<SalesReturn> observerSalesReturnCaptor = ArgumentCaptor.forClass(SalesReturn.class);
        ArgumentCaptor<String> observerActionCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockObserver1, times(1)).update(observerSalesReturnCaptor.capture(), observerActionCaptor.capture());
        assertEquals(updatedInfo.getCustomerName(), observerSalesReturnCaptor.getValue().getCustomerName());
        assertEquals("UPDATE", observerActionCaptor.getValue());
        verify(mockObserver2, times(1)).update(observerSalesReturnCaptor.getValue(), observerActionCaptor.getValue());
    }

    @Test
    void updateReturn_shouldThrowException_whenReturnDoesNotExist() {
        // Arrange
        SalesReturn nonExistentReturn = new SalesReturn();
        nonExistentReturn.setId(999); // ID tidak ada
        nonExistentReturn.setRmaNumber("RMA_NON_EXISTENT");
        
        when(mockRepository.findById(nonExistentReturn.getId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            salesReturnService.updateReturn(nonExistentReturn);
        });
        assertTrue(exception.getMessage().contains("tidak ditemukan, tidak dapat diperbarui"));

        verify(mockRepository, never()).update(any(SalesReturn.class));
        verify(mockObserver1, never()).update(any(SalesReturn.class), anyString());
    }
    
     @Test
    void updateReturn_shouldThrowException_whenSalesReturnIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salesReturnService.updateReturn(null);
        });
        assertEquals("Objek SalesReturn untuk update tidak boleh null.", exception.getMessage());
        verify(mockRepository, never()).update(any(SalesReturn.class));
    }

    @Test
    void deleteReturnById_shouldCallRepositoryDeleteAndNotifyObservers_whenReturnExists() {
        // Arrange
        when(mockRepository.findById(sampleReturn.getId())).thenReturn(Optional.of(sampleReturn));
        // doNothing().when(mockRepository).deleteById(sampleReturn.getId()); // Tidak perlu untuk void method

        // Act
        salesReturnService.deleteReturnById(sampleReturn.getId());

        // Assert
        verify(mockRepository, times(1)).findById(sampleReturn.getId()); // Pastikan pengecekan dilakukan
        verify(mockRepository, times(1)).deleteById(sampleReturn.getId());

        // Verifikasi notifikasi ke observer
        ArgumentCaptor<SalesReturn> observerSalesReturnCaptor = ArgumentCaptor.forClass(SalesReturn.class);
        ArgumentCaptor<String> observerActionCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockObserver1, times(1)).update(observerSalesReturnCaptor.capture(), observerActionCaptor.capture());
        assertEquals(sampleReturn.getRmaNumber(), observerSalesReturnCaptor.getValue().getRmaNumber());
        assertEquals("DELETE", observerActionCaptor.getValue());
        verify(mockObserver2, times(1)).update(observerSalesReturnCaptor.getValue(), observerActionCaptor.getValue());
    }

     @Test
    void deleteReturnById_shouldNotCallRepositoryDeleteOrNotify_whenReturnDoesNotExist() {
        // Arrange
        int nonExistentId = 999;
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        salesReturnService.deleteReturnById(nonExistentId);

        // Assert
        verify(mockRepository, times(1)).findById(nonExistentId);
        verify(mockRepository, never()).deleteById(nonExistentId);
        verify(mockObserver1, never()).update(any(SalesReturn.class), anyString());
        verify(mockObserver2, never()).update(any(SalesReturn.class), anyString());
    }
    
    @Test
    void registerObserver_shouldAddObserverIfNotPresent() {
        Observer newObserver = mock(Observer.class);
        salesReturnService.registerObserver(newObserver);
        
        // Untuk memverifikasi, kita bisa coba notify dan cek apakah newObserver dipanggil
        // Atau, jika memungkinkan, cek internal list (tapi ini white-box testing)
        // Cukup panggil lagi createReturn dan verifikasi pemanggilan ke newObserver
        SalesReturn testReturn = new SalesReturn("RMA_REG", "N_REG", "CustReg", "ITEM_REG", "DescReg", 1, "ReasonReg", "MethodReg", "CommentReg");
        when(mockRepository.save(any(SalesReturn.class))).thenReturn(testReturn);
        salesReturnService.createReturn(testReturn);
        
        verify(newObserver, times(1)).update(any(SalesReturn.class), eq("CREATE"));
    }

    @Test
    void removeObserver_shouldRemoveObserverIfPresent() {
        salesReturnService.removeObserver(mockObserver1);
        
        // Setelah remove, mockObserver1 tidak boleh lagi menerima notifikasi
        SalesReturn testReturn = new SalesReturn("RMA_REM", "N_REM", "CustRem", "ITEM_REM", "DescRem", 1, "ReasonRem", "MethodRem", "CommentRem");
        when(mockRepository.save(any(SalesReturn.class))).thenReturn(testReturn);
        salesReturnService.createReturn(testReturn);
        
        verify(mockObserver1, never()).update(any(SalesReturn.class), anyString()); // mockObserver1 sudah di-remove
        verify(mockObserver2, times(1)).update(any(SalesReturn.class), eq("CREATE")); // mockObserver2 masih ada
    }
}
