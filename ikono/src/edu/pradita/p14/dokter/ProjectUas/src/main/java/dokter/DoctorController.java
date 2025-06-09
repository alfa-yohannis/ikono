package dokter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dokter.dao.DoctorDAO;
import dokter.dao.PatientDAO;
import dokter.dao.ScheduleDAO;
import dokter.dao.SpecializationDAO;
import dokter.dao.impl.DoctorDAOImpl;
import dokter.dao.impl.PatientDAOImpl;
import dokter.dao.impl.ScheduleDAOImpl;
import dokter.dao.impl.SpecializationDAOImpl;
import dokter.model.Doctor;
import dokter.model.Patient;
import dokter.model.Schedule;
import dokter.model.Specialization; 
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorController {
    private BorderPane root;
    private TableView<Doctor> tableView;
    private ObservableList<Doctor> doctorList;
    private ObservableList<Specialization> specializationList; // Store Specialization objects

    private TextField txtFirstName, txtLastName, txtEmail, txtPhoneNumber;
    private TextArea txtAddress;
    private DatePicker dpHireDate;
    private ComboBox<Specialization> cbSpecialization; // ComboBox of Specialization objects
    private TextField txtSearch;

    // DAOs
    private final DoctorDAO doctorDAO;
    private final SpecializationDAO specializationDAO;
    private final ScheduleDAO scheduleDAO;
    private final PatientDAO patientDAO;


    public DoctorController() {
        this.doctorDAO = new DoctorDAOImpl();
        this.specializationDAO = new SpecializationDAOImpl();
        this.scheduleDAO = new ScheduleDAOImpl();
        this.patientDAO = new PatientDAOImpl();

        root = new BorderPane();
        doctorList = FXCollections.observableArrayList();
        specializationList = FXCollections.observableArrayList();


        initUI();
        loadSpecializations(); // Load specializations first
        loadDoctors();         // Then doctors
    }

    private void initUI() {
        tableView = new TableView<>();
        TableColumn<Doctor, String> colFullName = new TableColumn<>("Nama Lengkap");
        // Using the fx property getters or direct properties
        colFullName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFxFirstName() + " " + data.getValue().getFxLastName()));

        TableColumn<Doctor, String> colSpecialization = new TableColumn<>("Spesialisasi");
        colSpecialization.setCellValueFactory(data -> data.getValue().specializationNameProperty()); // Bind to property

        TableColumn<Doctor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<Doctor, String> colPhoneNumber = new TableColumn<>("Telepon");
        colPhoneNumber.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        
        TableColumn<Doctor, String> colAddress = new TableColumn<>("Alamat");
        colAddress.setCellValueFactory(data -> data.getValue().addressProperty());
       
        TableColumn<Doctor, LocalDate> colHireDate = new TableColumn<>("Tanggal Diterima"); // Use LocalDate
        colHireDate.setCellValueFactory(data -> data.getValue().hireDateProperty()); // Bind to property
        
        tableView.getColumns().addAll(colFullName, colSpecialization, colEmail, colPhoneNumber, colAddress, colHireDate);
        tableView.setItems(doctorList);
        tableView.setOnMouseClicked(this::onTableClick);

        txtFirstName = new TextField();
        txtFirstName.setPromptText("Nama Depan");
        txtLastName = new TextField();
        txtLastName.setPromptText("Nama Belakang");

        cbSpecialization = new ComboBox<>(); // Will hold Specialization objects
        cbSpecialization.setPromptText("Pilih Spesialisasi");
        cbSpecialization.setItems(specializationList); // Set items from observable list of Specialization
        // toString() in Specialization will be used for display by default

        txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtPhoneNumber = new TextField();
        txtPhoneNumber.setPromptText("Telepon");
        txtAddress = new TextArea();
        txtAddress.setPromptText("Alamat");
        txtAddress.setPrefRowCount(3);
        
        txtSearch = new TextField(); // Initialize the class member
        txtSearch.setPromptText("Cari Nama Dokter");

        dpHireDate = new DatePicker();
        dpHireDate.setPromptText("Tanggal Diterima");

        Button btnSave = new Button("Simpan");
        btnSave.setOnAction(e -> saveOrUpdateDoctor());

        Button btnDelete = new Button("Hapus");
        btnDelete.setOnAction(e -> hapusDoctor());
        
        Button btnSchedule = new Button("Jadwal");
        btnSchedule.setOnAction(e -> showDoctorSchedule());
        
        Button btnSearch = new Button("Cari");
        btnSearch.setOnAction(e -> cariDokter(txtSearch.getText()));

        Button btnReset = new Button("Reset");
        btnReset.setOnAction(e -> {
            txtSearch.clear();
            loadDoctors(); // Reload all doctors
            resetForm();
        });
   
        Button btnShowPatients = new Button("Tampilkan Pasien");
        btnShowPatients.setOnAction(e -> showPatientData());  

        HBox searchBox = new HBox(10, txtSearch, btnSearch, btnReset, btnShowPatients);
        searchBox.setPadding(new Insets(10));
        
        VBox topLayout = new VBox(10, searchBox);
        root.setTop(topLayout);
        
        VBox form = new VBox(10, txtFirstName, txtLastName, cbSpecialization, txtEmail, txtPhoneNumber, txtAddress, dpHireDate, btnSave, btnDelete, btnSchedule);
        form.setPadding(new Insets(10));

        root.setCenter(tableView);
        root.setRight(form);
    }

    private void loadDoctors() {
        doctorList.clear();
        List<Doctor> doctorsFromDB = doctorDAO.getAll();
        if (doctorsFromDB != null) {
            doctorList.addAll(doctorsFromDB);
        } else {
            showErrorAlert("Gagal Memuat Data Dokter", "Terjadi kesalahan saat mengambil data dokter dari database.");
        }
    }

    private void loadSpecializations() {
        specializationList.clear();
        List<Specialization> specs = specializationDAO.getAll();
        if (specs != null) {
            specializationList.addAll(specs);
             // Optionally, pre-populate if some specializations don't exist
            if (specs.isEmpty()) {
                 Specialization general = new Specialization("Umum");
                 specializationDAO.save(general);
                 specializationList.add(general);
            }
        } else {
             showErrorAlert("Gagal Memuat Spesialisasi", "Terjadi kesalahan saat mengambil data spesialisasi.");
        }
    }

    private void saveOrUpdateDoctor() {
        if (dpHireDate.getValue() == null || cbSpecialization.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "Peringatan", "Input Tidak Valid", "Silakan pilih tanggal diterima dan spesialisasi.");
            return;
        }

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        Specialization selectedSpecialization = cbSpecialization.getSelectionModel().getSelectedItem();
        String email = txtEmail.getText();
        String phone = txtPhoneNumber.getText();
        String address = txtAddress.getText();
        LocalDate hireDate = dpHireDate.getValue();

        Doctor selectedDoctor = tableView.getSelectionModel().getSelectedItem();
        Doctor doctorToSave;

        if (selectedDoctor != null) { // Update existing doctor
            doctorToSave = selectedDoctor; // Get the persistent entity
            doctorToSave.setFirstName(firstName);
            doctorToSave.setLastName(lastName);
            doctorToSave.setSpecialization(selectedSpecialization);
            doctorToSave.setEmail(email);
            doctorToSave.setPhoneNumber(phone);
            doctorToSave.setAddress(address);
            doctorToSave.setHireDate(hireDate);
        } else { // Create new doctor
            doctorToSave = new Doctor(firstName, lastName, selectedSpecialization, email, phone, address, hireDate);
        }
        
        doctorDAO.saveOrUpdate(doctorToSave);
        doctorToSave.updateProperties(); // Crucial for JavaFX UI update after save/update
        
        loadDoctors(); // Reload to reflect changes (especially if new ID was generated)
        resetForm();
    }
    
    private void hapusDoctor() {
        Doctor selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Konfirmasi Hapus");
            confirmationAlert.setHeaderText("Anda yakin ingin menghapus data dokter ini?");
            confirmationAlert.setContentText("Nama: " + selected.getFxFirstName() + " " + selected.getFxLastName());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                doctorDAO.delete(selected);
                loadDoctors();
                resetForm();
            }
        } else {
            showAlert(AlertType.WARNING, "Peringatan", "Tidak Ada Dokter yang Dipilih", "Silakan pilih dokter di tabel sebelum menghapus.");
        }
    }

    private void resetForm() {
        txtFirstName.clear();
        txtLastName.clear();
        cbSpecialization.getSelectionModel().clearSelection();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtAddress.clear();
        dpHireDate.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void onTableClick(MouseEvent event) {
        Doctor selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtFirstName.setText(selected.getFxFirstName());
            txtLastName.setText(selected.getFxLastName());
            // Find and set the Specialization object in ComboBox
            cbSpecialization.setValue(selected.getSpecialization());
            txtEmail.setText(selected.getFxEmail());
            txtPhoneNumber.setText(selected.getFxPhoneNumber());
            txtAddress.setText(selected.getFxAddress());
            dpHireDate.setValue(selected.getFxHireDate());
        }
    }
    
    private void showDoctorSchedule() {
        Doctor selectedDoctor = tableView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            Stage scheduleStage = new Stage();
            BorderPane scheduleRoot = new BorderPane();
            TableView<Schedule> scheduleTable = new TableView<>();
            ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

            TableColumn<Schedule, String> colDay = new TableColumn<>("Hari");
            colDay.setCellValueFactory(data -> data.getValue().dayOfWeekProperty());

            TableColumn<Schedule, String> colStartTime = new TableColumn<>("Jam Mulai");
            colStartTime.setCellValueFactory(data -> data.getValue().startTimeProperty());

            TableColumn<Schedule, String> colEndTime = new TableColumn<>("Jam Selesai");
            colEndTime.setCellValueFactory(data -> data.getValue().endTimeProperty());

            TableColumn<Schedule, String> colLocation = new TableColumn<>("Lokasi");
            colLocation.setCellValueFactory(data -> data.getValue().locationProperty());

            TableColumn<Schedule, String> colDescription = new TableColumn<>("Keterangan");
            colDescription.setCellValueFactory(data -> data.getValue().descriptionProperty());
            
            scheduleTable.getColumns().addAll(colDay, colStartTime, colEndTime, colLocation, colDescription);
            scheduleTable.setItems(scheduleList);

            loadScheduleForDoctor(selectedDoctor.getDoctorId(), scheduleList);

            scheduleRoot.setCenter(scheduleTable);
            Scene scheduleScene = new Scene(scheduleRoot, 600, 400);
            scheduleStage.setScene(scheduleScene);
            scheduleStage.setTitle("Jadwal Praktek Dokter: " + selectedDoctor.getFxFirstName() + " " + selectedDoctor.getFxLastName());
            scheduleStage.show();
        } else {
            showAlert(AlertType.WARNING, "Peringatan", "Pilih Dokter Terlebih Dahulu", "Silakan pilih dokter di tabel untuk melihat jadwal praktek.");
        }
    }

    private void loadScheduleForDoctor(int doctorId, ObservableList<Schedule> uiScheduleList) {
        uiScheduleList.clear();
        List<Schedule> schedulesFromDB = scheduleDAO.getByDoctorId(doctorId);
        if (schedulesFromDB != null) {
            uiScheduleList.addAll(schedulesFromDB);
        } else {
            showErrorAlert("Gagal Memuat Jadwal", "Terjadi kesalahan saat mengambil data jadwal dari database.");
        }
    }
    
    private void cariDokter(String keyword) {
        doctorList.clear(); 
        List<Doctor> foundDoctors = doctorDAO.searchByName(keyword);
        if (foundDoctors != null) {
            doctorList.addAll(foundDoctors);
        } else {
            showErrorAlert("Gagal Pencarian Dokter", "Terjadi kesalahan saat mencari data dokter.");
        }
        // tableView.setItems(doctorList); // Already bound
    }

    private void loadPatients(ObservableList<Patient> uiPatientList) {
        uiPatientList.clear();
        List<Patient> patientsFromDB = patientDAO.getAll();
        if (patientsFromDB != null) {
            uiPatientList.addAll(patientsFromDB);
        } else {
            showErrorAlert("Gagal Memuat Data Pasien", "Terjadi kesalahan saat mengambil data pasien.");
        }
    }
    
    private void showPatientData() {
        Stage patientStage = new Stage();
        BorderPane patientRoot = new BorderPane();
        TableView<Patient> patientTable = new TableView<>();
        ObservableList<Patient> patientListForUI = FXCollections.observableArrayList();
      
        // Using JavaFX properties from Patient model, or direct field access if not using properties there
        TableColumn<Patient, String> colNamaLengkap = new TableColumn<>("Nama Lengkap");
        colNamaLengkap.setCellValueFactory(data -> data.getValue().namaLengkapProperty()); // Assuming you added this property
      
        TableColumn<Patient, LocalDate> colTanggalLahir = new TableColumn<>("Tanggal Lahir");
        colTanggalLahir.setCellValueFactory(data -> data.getValue().tanggalLahirProperty()); // Assuming you added this
       
        TableColumn<Patient, String> colJenisKelamin = new TableColumn<>("Jenis Kelamin");
        colJenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin")); // Direct field if no property
      
        // ... (Setup other columns similarly. Use PropertyValueFactory for direct field access,
        // or cellValueFactory with lambda for properties like data.getValue().alamatProperty() )
        TableColumn<Patient, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        TableColumn<Patient, String> colNomorTelepon = new TableColumn<>("Nomor Telepon");
        colNomorTelepon.setCellValueFactory(new PropertyValueFactory<>("nomorTelepon"));
        TableColumn<Patient, String> colEmailPatient = new TableColumn<>("Email"); // Renamed to avoid clash with doctor email column
        colEmailPatient.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Patient, String> colGolonganDarah = new TableColumn<>("Golongan Darah");
        colGolonganDarah.setCellValueFactory(new PropertyValueFactory<>("golonganDarah"));
        TableColumn<Patient, String> colStatusPernikahan = new TableColumn<>("Status Pernikahan");
        colStatusPernikahan.setCellValueFactory(new PropertyValueFactory<>("statusPernikahan"));
        TableColumn<Patient, String> colRiwayatPenyakit = new TableColumn<>("Riwayat Penyakit");
        colRiwayatPenyakit.setCellValueFactory(new PropertyValueFactory<>("riwayatPenyakit"));
        TableColumn<Patient, String> colAlergi = new TableColumn<>("Alergi");
        colAlergi.setCellValueFactory(new PropertyValueFactory<>("alergi"));
        TableColumn<Patient, LocalDate> colTanggalRegistrasi = new TableColumn<>("Tanggal Registrasi");
        colTanggalRegistrasi.setCellValueFactory(new PropertyValueFactory<>("tanggalRegistrasi"));


        patientTable.getColumns().addAll(colNamaLengkap, colTanggalLahir, colJenisKelamin, colAlamat, colNomorTelepon,
                colEmailPatient, colGolonganDarah, colStatusPernikahan, colRiwayatPenyakit, colAlergi, colTanggalRegistrasi);

        patientTable.setItems(patientListForUI);
        loadPatients(patientListForUI);

        patientRoot.setCenter(patientTable);
        Scene patientScene = new Scene(patientRoot, 1000, 600); // Increased width
        patientStage.setScene(patientScene);
        patientStage.setTitle("Data Pasien");
        patientStage.show();
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String header, String content) {
        showAlert(AlertType.ERROR, "Error", header, content);
    }
    
    public BorderPane getView() {
        return root;
    }
} 