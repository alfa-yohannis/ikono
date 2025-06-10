package com.inventory.warehouseinventorysystem.service;

import com.inventory.warehouseinventorysystem.dao.DaoFactory;
import com.inventory.warehouseinventorysystem.dao.WarehouseDao;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock; // @InjectMocks tidak kita perlukan lagi untuk warehouseService jika diinisialisasi manual
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseDao mockWarehouseDao;

    @Mock
    private DaoFactory mockDaoFactory;

    // warehouseService tidak lagi menggunakan @InjectMocks, akan kita buat manual di setUp
    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        // 1. Atur perilaku mockDaoFactory DULU
        when(mockDaoFactory.getWarehouseDao()).thenReturn(mockWarehouseDao);

        // 2. BARU buat instance WarehouseService menggunakan mockDaoFactory yang sudah dikonfigurasi
        // Ini memastikan bahwa saat constructor WarehouseService memanggil daoFactory.getWarehouseDao(),
        // ia akan mendapatkan mockWarehouseDao kita.
        warehouseService = new WarehouseService(mockDaoFactory);
    }

    // ... (Semua metode @Test Anda tetap sama seperti yang saya berikan sebelumnya) ...
    // Contoh satu metode test:
    @Test
    void getWarehouseById_whenWarehouseExists_shouldReturnWarehouse() {
        // 1. Arrange (Persiapan)
        int warehouseId = 1;
        Warehouse expectedWarehouse = new Warehouse("Gudang Test A", "Lokasi Test A");
        expectedWarehouse.setId(warehouseId);

        when(mockWarehouseDao.findById(warehouseId)).thenReturn(expectedWarehouse);

        // 2. Act (Eksekusi)
        Warehouse actualWarehouse = warehouseService.getWarehouseById(warehouseId);

        // 3. Assert (Verifikasi)
        assertNotNull(actualWarehouse, "Warehouse tidak boleh null");
        assertEquals(expectedWarehouse.getId(), actualWarehouse.getId(), "ID Gudang harus cocok");
        assertEquals(expectedWarehouse.getName(), actualWarehouse.getName(), "Nama Gudang harus cocok");
        // ... dst ...

        verify(mockWarehouseDao, times(1)).findById(warehouseId);
    }

    // Pastikan semua metode test lainnya juga ada di sini...
    @Test
    void getWarehouseById_whenWarehouseDoesNotExist_shouldReturnNull() {
        // 1. Arrange
        int warehouseId = 99;
        when(mockWarehouseDao.findById(warehouseId)).thenReturn(null);
        // 2. Act
        Warehouse actualWarehouse = warehouseService.getWarehouseById(warehouseId);
        // 3. Assert
        assertNull(actualWarehouse, "Warehouse harus null jika tidak ditemukan");
        verify(mockWarehouseDao, times(1)).findById(warehouseId);
    }

    @Test
    void saveWarehouse_whenNewWarehouseValid_shouldCallDaoSave() {
        Warehouse newWarehouse = new Warehouse("Gudang Baru", "Lokasi Baru");
        when(mockWarehouseDao.findByName(newWarehouse.getName())).thenReturn(null);
        warehouseService.saveWarehouse(newWarehouse);
        verify(mockWarehouseDao, times(1)).save(newWarehouse);
        verify(mockWarehouseDao, never()).update(any(Warehouse.class));
    }

    @Test
    void saveWarehouse_whenExistingWarehouseValid_shouldCallDaoUpdate() {
        Warehouse existingWarehouse = new Warehouse("Gudang Lama", "Lokasi Lama");
        existingWarehouse.setId(5);
        when(mockWarehouseDao.findByName(existingWarehouse.getName())).thenReturn(existingWarehouse);
        warehouseService.saveWarehouse(existingWarehouse);
        verify(mockWarehouseDao, times(1)).update(existingWarehouse);
        verify(mockWarehouseDao, never()).save(any(Warehouse.class));
    }

    @Test
    void saveWarehouse_whenNewWarehouseNameExists_shouldThrowIllegalArgumentException() {
        Warehouse newWarehouse = new Warehouse("Nama Duplikat", "Lokasi X");
        Warehouse existingWarehouseWithSameName = new Warehouse("Nama Duplikat", "Lokasi Y");
        existingWarehouseWithSameName.setId(10);
        when(mockWarehouseDao.findByName("Nama Duplikat")).thenReturn(existingWarehouseWithSameName);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.saveWarehouse(newWarehouse);
        });
        assertEquals("Warehouse with name 'Nama Duplikat' already exists.", exception.getMessage());
        verify(mockWarehouseDao, never()).save(any(Warehouse.class));
        verify(mockWarehouseDao, never()).update(any(Warehouse.class));
    }

    @Test
    void saveWarehouse_whenWarehouseNameIsEmpty_shouldThrowIllegalArgumentException() {
        Warehouse warehouseWithEmptyName = new Warehouse("", "Lokasi Kosong");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.saveWarehouse(warehouseWithEmptyName);
        });
        assertEquals("Warehouse name cannot be empty.", exception.getMessage());
        verify(mockWarehouseDao, never()).save(any(Warehouse.class));
        verify(mockWarehouseDao, never()).update(any(Warehouse.class));
    }

    @Test
    void getAllWarehouses_whenWarehousesExist_shouldReturnListOfWarehouses() {
        List<Warehouse> expectedWarehouses = new ArrayList<>();
        expectedWarehouses.add(new Warehouse("Gudang Alpha", "Area A"));
        expectedWarehouses.add(new Warehouse("Gudang Beta", "Area B"));
        when(mockWarehouseDao.findAll()).thenReturn(expectedWarehouses);
        List<Warehouse> actualWarehouses = warehouseService.getAllWarehouses();
        assertNotNull(actualWarehouses);
        assertEquals(2, actualWarehouses.size());
        assertEquals("Gudang Alpha", actualWarehouses.get(0).getName());
        verify(mockWarehouseDao, times(1)).findAll();
    }

    @Test
    void getAllWarehouses_whenNoWarehousesExist_shouldReturnEmptyList() {
        when(mockWarehouseDao.findAll()).thenReturn(new ArrayList<>());
        List<Warehouse> actualWarehouses = warehouseService.getAllWarehouses();
        assertNotNull(actualWarehouses);
        assertTrue(actualWarehouses.isEmpty(), "List gudang seharusnya kosong");
        verify(mockWarehouseDao, times(1)).findAll();
    }

    @Test
    void deleteWarehouse_whenWarehouseExists_shouldCallDaoDeleteById() {
        int warehouseIdToDelete = 1;
        // Untuk metode void, Anda bisa menggunakan doNothing() jika perlu stubbing,
        // tapi untuk verifikasi saja, tidak perlu stubbing khusus.
        // doNothing().when(mockWarehouseDao).deleteById(warehouseIdToDelete);
        warehouseService.deleteWarehouse(warehouseIdToDelete);
        verify(mockWarehouseDao, times(1)).deleteById(warehouseIdToDelete);
    }

    @Test
    void deleteWarehouse_whenDaoThrowsException_shouldPropagateException() {
        int warehouseIdToDelete = 1;
        // Mengatur agar mock DAO melempar RuntimeException saat deleteById dipanggil
        doThrow(new RuntimeException("Database error")).when(mockWarehouseDao).deleteById(warehouseIdToDelete);

        // Memverifikasi bahwa service melempar RuntimeException (atau jenis yang lebih spesifik jika Anda mau)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            warehouseService.deleteWarehouse(warehouseIdToDelete);
        });

        // Anda bisa juga memeriksa pesan exception jika perlu,
        // misalnya jika service Anda membungkusnya dengan pesan tertentu
        assertTrue(exception.getMessage().contains("Failed to delete warehouse") || exception.getMessage().contains("Database error"));
        verify(mockWarehouseDao, times(1)).deleteById(warehouseIdToDelete);
    }
}