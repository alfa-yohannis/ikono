package org.example.service;

import org.example.dao.PembeliDao;
import org.example.model.Pembeli;
import org.example.pattern.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PembeliServiceTest {

    @Mock
    private PembeliDao pembeliDaoMock;

    @Mock
    private NotifikasiService notifikasiServiceMock; // Mock untuk Subject Observer

    @InjectMocks // Ini akan membuat instance PembeliServiceImpl dan menginject mock di atas
    private PembeliServiceImpl pembeliService;

    private Pembeli pembeliContoh;

    @BeforeEach
    void setUp() {
        pembeliContoh = new Pembeli("Budi Darmawan", "Jl. Kenangan", "Jakarta", "12345",
                "081234567890", "budi@example.com", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        pembeliContoh.setIdPembeli(1); // Asumsikan ID sudah ada untuk beberapa test
    }

    @Test
    void tambahPembeli_ValidInput_ShouldSaveAndNotify() {
        // Action
        pembeliService.tambahPembeli(pembeliContoh);

        // Assertions
        // Verifikasi bahwa metode save pada DAO dipanggil sekali dengan objek pembeliContoh
        ArgumentCaptor<Pembeli> pembeliCaptor = ArgumentCaptor.forClass(Pembeli.class);
        verify(pembeliDaoMock, times(1)).save(pembeliCaptor.capture());
        assertEquals("Budi Darmawan", pembeliCaptor.getValue().getNamaLengkap());

        // Verifikasi bahwa NotifikasiService mengirim notifikasi
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notifikasiServiceMock, times(1)).kirimNotifikasi(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().contains("Pembeli baru ditambahkan: Budi Darmawan"));
    }
    
    @Test
    void tambahPembeli_NamaKosong_ShouldThrowException() {
        Pembeli pembeliInvalid = new Pembeli("", "Alamat", "Kota", "123", "080", "email", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pembeliService.tambahPembeli(pembeliInvalid);
        });
        
        assertEquals("Nama lengkap tidak boleh kosong.", exception.getMessage());
        verify(pembeliDaoMock, never()).save(any(Pembeli.class));
        verify(notifikasiServiceMock, never()).kirimNotifikasi(anyString());
    }


    @Test
    void getPembeliById_Exists_ShouldReturnPembeli() {
        when(pembeliDaoMock.findById(1)).thenReturn(Optional.of(pembeliContoh));

        Optional<Pembeli> result = pembeliService.getPembeliById(1);

        assertTrue(result.isPresent());
        assertEquals("Budi Darmawan", result.get().getNamaLengkap());
        verify(pembeliDaoMock, times(1)).findById(1);
    }

    @Test
    void getPembeliById_NotExists_ShouldReturnEmpty() {
        when(pembeliDaoMock.findById(99)).thenReturn(Optional.empty());

        Optional<Pembeli> result = pembeliService.getPembeliById(99);

        assertFalse(result.isPresent());
        verify(pembeliDaoMock, times(1)).findById(99);
    }
    
    @Test
    void getAllPembeli_ShouldReturnList() {
        Pembeli p2 = new Pembeli("Siti Aminah", "Jl. Mawar", "Bandung", "54321", "0821", "siti@example.com", Pembeli.JenisKelamin.P, Pembeli.StatusPembeli.Aktif);
        when(pembeliDaoMock.findAll()).thenReturn(List.of(pembeliContoh, p2));

        List<Pembeli> result = pembeliService.getAllPembeli();

        assertEquals(2, result.size());
        verify(pembeliDaoMock, times(1)).findAll();
    }


    @Test
    void updatePembeli_ValidInput_ShouldUpdateAndNotify() {
        pembeliContoh.setKota("Surabaya"); // Perubahan

        // Tidak perlu when(pembeliDaoMock.update(...)) karena update adalah void
        // Cukup verifikasi bahwa itu dipanggil

        pembeliService.updatePembeli(pembeliContoh);

        ArgumentCaptor<Pembeli> pembeliCaptor = ArgumentCaptor.forClass(Pembeli.class);
        verify(pembeliDaoMock, times(1)).update(pembeliCaptor.capture());
        assertEquals("Surabaya", pembeliCaptor.getValue().getKota());
        assertEquals(1, pembeliCaptor.getValue().getIdPembeli());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notifikasiServiceMock, times(1)).kirimNotifikasi(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().contains("Data pembeli diupdate: Budi Darmawan"));
    }
    
    @Test
    void updatePembeli_InvalidId_ShouldThrowException() {
        Pembeli pembeliInvalidId = new Pembeli("Test", "Alamat", "Kota", "123", "080", "email", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        pembeliInvalidId.setIdPembeli(0); // ID tidak valid
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pembeliService.updatePembeli(pembeliInvalidId);
        });
        
        assertEquals("ID Pembeli tidak valid untuk update.", exception.getMessage());
        verify(pembeliDaoMock, never()).update(any(Pembeli.class));
        verify(notifikasiServiceMock, never()).kirimNotifikasi(anyString());
    }

    @Test
    void hapusPembeliById_Exists_ShouldDeleteAndNotify() {
        when(pembeliDaoMock.findById(1)).thenReturn(Optional.of(pembeliContoh));
        // Tidak perlu when(pembeliDaoMock.deleteById(...)) karena void

        pembeliService.hapusPembeliById(1);

        verify(pembeliDaoMock, times(1)).deleteById(1);
        
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notifikasiServiceMock, times(1)).kirimNotifikasi(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().contains("Pembeli dihapus: Budi Darmawan"));
    }
    
    @Test
    void hapusPembeliById_NotExists_ShouldNotDeleteOrNotify() {
        when(pembeliDaoMock.findById(99)).thenReturn(Optional.empty());

        pembeliService.hapusPembeliById(99); // Seharusnya tidak melempar exception, hanya log warning

        verify(pembeliDaoMock, never()).deleteById(99);
        verify(notifikasiServiceMock, never()).kirimNotifikasi(anyString());
    }
}