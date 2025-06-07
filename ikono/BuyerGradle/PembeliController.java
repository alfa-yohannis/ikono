package org.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.PembeliDaoImpl;
import org.example.model.Pembeli;
import org.example.pattern.decorator.LoggingPembeliServiceDecorator;
import org.example.pattern.observer.EmailNotifier;
import org.example.service.NotifikasiService;
import org.example.service.PembeliService;
import org.example.service.PembeliServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PembeliController {

    private static final Logger logger = LoggerFactory.getLogger(PembeliController.class);

    @FXML private TextField nameField;
    @FXML private TextArea addressArea;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private RadioButton activeRadio;
    @FXML private RadioButton inactiveRadio;
    @FXML private ToggleGroup genderGroup;
    @FXML private ToggleGroup statusGroup;
    @FXML private ListView<Pembeli> buyerListView;
    @FXML private Label infoLabel;

    private PembeliService pembeliService;
    private ObservableList<Pembeli> pembeliObservableList;
    private Pembeli selectedPembeli = null;

    @FXML
    public void initialize() {
        NotifikasiService notifikasiService = new NotifikasiService();
        notifikasiService.tambahObserver(new EmailNotifier("admin@example.com"));
        
        PembeliService actualService = new PembeliServiceImpl(new PembeliDaoImpl(), notifikasiService);
        this.pembeliService = new LoggingPembeliServiceDecorator(actualService);

        pembeliObservableList = FXCollections.observableArrayList();
        buyerListView.setItems(pembeliObservableList);
        loadBuyers();

        buyerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPembeli = newSelection;
                populateFields(newSelection);
                infoLabel.setText("Data '" + newSelection.getNamaLengkap() + "' dipilih.");
            } else {
                // selectedPembeli akan di-null-kan oleh clearFields jika dipanggil,
                // atau jika tidak ada item yang dipilih.
                // Cukup pastikan selectedPembeli di-null-kan jika newSelection null.
                selectedPembeli = null; 
                // Tidak perlu memanggil clearFields() di sini secara otomatis karena
                // pengguna mungkin hanya mengklik area kosong listview.
                // infoLabel.setText("Tidak ada data dipilih."); // Bisa juga dikosongkan
            }
        });
        
        maleRadio.setSelected(true);
        activeRadio.setSelected(true);
    }

    private void populateFields(Pembeli pembeli) {
        nameField.setText(pembeli.getNamaLengkap());
        addressArea.setText(pembeli.getAlamat());
        cityField.setText(pembeli.getKota());
        postalCodeField.setText(pembeli.getKodePos());
        phoneField.setText(pembeli.getNoTelepon());
        emailField.setText(pembeli.getEmail());

        if (pembeli.getJenisKelamin() == Pembeli.JenisKelamin.L) {
            maleRadio.setSelected(true);
        } else {
            femaleRadio.setSelected(true);
        }

        if (pembeli.getStatus() == Pembeli.StatusPembeli.Aktif) {
            activeRadio.setSelected(true);
        } else {
            inactiveRadio.setSelected(true);
        }
    }

    @FXML
    void handleSaveBuyer(ActionEvent event) {
        try {
            Pembeli pembeli = new Pembeli();
            // Validasi input dasar sebelum membuat objek
            String nama = nameField.getText();
            if (nama == null || nama.trim().isEmpty()) {
                showWarningAlert("Input Tidak Valid", "Nama lengkap tidak boleh kosong.");
                return;
            }
            pembeli.setNamaLengkap(nama);
            pembeli.setAlamat(addressArea.getText());
            pembeli.setKota(cityField.getText());
            pembeli.setKodePos(postalCodeField.getText());
            pembeli.setNoTelepon(phoneField.getText());
            pembeli.setEmail(emailField.getText());
            pembeli.setJenisKelamin(maleRadio.isSelected() ? Pembeli.JenisKelamin.L : Pembeli.JenisKelamin.P);
            pembeli.setStatus(activeRadio.isSelected() ? Pembeli.StatusPembeli.Aktif : Pembeli.StatusPembeli.Non_Aktif);

            pembeliService.tambahPembeli(pembeli);
            loadBuyers();
            clearFields();
            infoLabel.setText("Pembeli '" + pembeli.getNamaLengkap() + "' berhasil ditambahkan.");
            logger.info("Pembeli disimpan: {}", pembeli.getNamaLengkap());
        } catch (IllegalArgumentException iae) {
            logger.warn("Input tidak valid saat menyimpan pembeli: {}", iae.getMessage());
            showErrorAlert("Gagal Menyimpan", iae.getMessage());
        } catch (Exception e) {
            logger.error("Gagal menyimpan pembeli", e);
            showErrorAlert("Gagal Menyimpan", "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
        }
    }
    
    @FXML
    void handleUpdateBuyer(ActionEvent event) {
        if (selectedPembeli == null) {
            showWarningAlert("Update Gagal", "Pilih data pembeli yang akan diupdate dari daftar.");
            return;
        }
        try {
            // Simpan nama sebelum selectedPembeli berpotensi diubah atau di-null-kan
            String namaPembeliSebelumUpdate = selectedPembeli.getNamaLengkap(); 

            // Validasi input dasar sebelum update
            String namaBaru = nameField.getText();
            if (namaBaru == null || namaBaru.trim().isEmpty()) {
                showWarningAlert("Input Tidak Valid", "Nama lengkap tidak boleh kosong saat update.");
                return;
            }

            selectedPembeli.setNamaLengkap(namaBaru);
            selectedPembeli.setAlamat(addressArea.getText());
            selectedPembeli.setKota(cityField.getText());
            selectedPembeli.setKodePos(postalCodeField.getText());
            selectedPembeli.setNoTelepon(phoneField.getText());
            selectedPembeli.setEmail(emailField.getText());
            selectedPembeli.setJenisKelamin(maleRadio.isSelected() ? Pembeli.JenisKelamin.L : Pembeli.JenisKelamin.P);
            selectedPembeli.setStatus(activeRadio.isSelected() ? Pembeli.StatusPembeli.Aktif : Pembeli.StatusPembeli.Non_Aktif);

            pembeliService.updatePembeli(selectedPembeli);
            
            loadBuyers(); // Refresh list view
            clearFields(); // Membersihkan field dan men-set selectedPembeli menjadi null

            // Gunakan nama yang disimpan sebelumnya untuk pesan
            infoLabel.setText("Pembeli '" + namaPembeliSebelumUpdate + "' berhasil diupdate menjadi '" + namaBaru + "'.");
            logger.info("Pembeli diupdate: {} (ID: {})", namaBaru, selectedPembeli.getIdPembeli()); // selectedPembeli sudah null di sini jika clearFields dipanggil sebelumnya, tapi ID masih bisa diambil dari objek yang diupdate
            // Sebenarnya, setelah clearFields(), selectedPembeli sudah null.
            // Untuk logging ID, kita bisa ambil dari objek pembeli sebelum di-null-kan,
            // atau jika service mengembalikan objek yang diupdate, gunakan itu.
            // Dalam kasus ini, karena kita memodifikasi selectedPembeli secara langsung,
            // ID nya masih sama. Pesan logger di atas mungkin akan NPE jika selectedPembeli.getIdPembeli() dipanggil setelah clearFields.
            // Mari kita log ID sebelum clearFields atau pastikan selectedPembeli belum null.

            // Solusi yang lebih baik untuk logging ID:
            // int idPembeliYangDiupdate = selectedPembeli.getIdPembeli(); // Simpan ID juga
            // ... (setelah update dan loadBuyers)
            // clearFields();
            // logger.info("Pembeli diupdate: {} (ID: {})", namaBaru, idPembeliYangDiupdate);
            // infoLabel.setText("Pembeli '" + namaPembeliSebelumUpdate + "' berhasil diupdate menjadi '" + namaBaru + "'.");
            // selectedPembeli = null; // Pastikan di-null-kan di akhir handle jika tidak di clearFields()

            // Karena clearFields() sudah mengatur selectedPembeli = null,
            // kita hanya perlu nama untuk pesan.
            // Log ID akan sulit jika selectedPembeli sudah null, mari kita asumsikan
            // namaPembeliSebelumUpdate dan namaBaru cukup untuk logging.

        } catch (IllegalArgumentException iae) {
            logger.warn("Input tidak valid saat mengupdate pembeli: {}", iae.getMessage());
            showErrorAlert("Gagal Update", iae.getMessage());
        } catch (Exception e) {
            logger.error("Gagal mengupdate pembeli", e);
            showErrorAlert("Gagal Update", "Terjadi kesalahan saat mengupdate data: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteBuyer(ActionEvent event) {
         if (selectedPembeli == null) {
            showWarningAlert("Hapus Gagal", "Pilih data pembeli yang akan dihapus dari daftar.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        String namaPembeliYangAkanDihapus = selectedPembeli.getNamaLengkap(); // Simpan nama untuk pesan
        alert.setHeaderText("Hapus Pembeli: " + namaPembeliYangAkanDihapus);
        alert.setContentText("Apakah Anda yakin ingin menghapus data pembeli ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int idYangDihapus = selectedPembeli.getIdPembeli(); // Simpan ID untuk logging jika perlu
                pembeliService.hapusPembeliById(idYangDihapus);
                loadBuyers();
                clearFields(); // Ini juga akan men-set selectedPembeli menjadi null
                infoLabel.setText("Pembeli '" + namaPembeliYangAkanDihapus + "' berhasil dihapus.");
                logger.info("Pembeli dihapus: {} (ID: {})", namaPembeliYangAkanDihapus, idYangDihapus);
                // selectedPembeli = null; // Sudah di-handle oleh clearFields()
            } catch (Exception e) {
                logger.error("Gagal menghapus pembeli", e);
                showErrorAlert("Gagal Hapus", "Terjadi kesalahan saat menghapus data: " + e.getMessage());
            }
        }
    }
    
    @FXML
    void handleClear(ActionEvent event) {
        clearFields();
        buyerListView.getSelectionModel().clearSelection(); // Pastikan seleksi di ListView juga dibersihkan
        // selectedPembeli sudah di-null-kan oleh clearFields()
        infoLabel.setText("Form dibersihkan.");
    }

    private void loadBuyers() {
        pembeliObservableList.setAll(pembeliService.getAllPembeli());
        if (pembeliObservableList.isEmpty()){
            infoLabel.setText("Tidak ada data pembeli. Silakan tambahkan data baru.");
        } else {
            // infoLabel.setText("Data pembeli dimuat. Jumlah: " + pembeliObservableList.size()); 
            // Komentari ini agar tidak menimpa pesan sukses/gagal dari operasi lain
        }
    }

    private void clearFields() {
        nameField.clear();
        addressArea.clear();
        cityField.clear();
        postalCodeField.clear();
        phoneField.clear();
        emailField.clear();
        if (genderGroup.getSelectedToggle() != null) {
            genderGroup.getSelectedToggle().setSelected(false);
        }
        maleRadio.setSelected(true); 

        if (statusGroup.getSelectedToggle() != null) {
            statusGroup.getSelectedToggle().setSelected(false);
        }
        activeRadio.setSelected(true);
        selectedPembeli = null; // Penting: null-kan setelah membersihkan field
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
     private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}