package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class App extends Application {
    private TableView<ItemReport> reportTable;
    private ObservableList<ItemReport> itemReports; // Sudah ada, akan digunakan untuk print
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    // Menggunakan ReportServiceInterface untuk Decorator
    private ReportServiceInterface reportService;
    private ReportPrinter reportPrinter;
    private AlertHandler alertHandler;

    // Opsi untuk strategi cetak yang berbeda
    private PrintStrategy<ItemReport> javaFxStrategy;
    private PrintStrategy<ItemReport> consoleStrategy;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize services
        this.alertHandler = new AlertHandler(); // Inisialisasi AlertHandler sekali

        // Inisialisasi ReportService dengan Decorator (jika diterapkan)
        ReportServiceInterface realReportService = new ReportService();
        this.reportService = new LoggingReportServiceDecorator(realReportService); // Contoh dengan logging decorator

        // Inisialisasi strategi-strategi pencetakan
        this.javaFxStrategy = new JavaFxPrinterStrategy<>(this.alertHandler);
        this.consoleStrategy = new ConsolePrintStrategy<>();

        // Inisialisasi ReportPrinter dengan strategi default (misalnya, JavaFX Printer)
        this.reportPrinter = new ReportPrinter(this.javaFxStrategy, this.alertHandler);


        // --- UI Setup ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox topLayout = new VBox(10);
        topLayout.setAlignment(Pos.CENTER);
        topLayout.setPadding(new Insets(10));

        HBox filterLayout = new HBox(10);
        filterLayout.setAlignment(Pos.CENTER);

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button loadButton = new Button("Load Report");
        loadButton.setOnAction(e -> handleLoadReport());

        Button printButton = new Button("Print Report (UI)");
        printButton.setOnAction(e -> {
            reportPrinter.setPrintStrategy(javaFxStrategy); // Pastikan strategi UI yang aktif
            handlePrintReport();
        });

        Button printToConsoleButton = new Button("Print to Console");
        printToConsoleButton.setOnAction(e -> {
            reportPrinter.setPrintStrategy(consoleStrategy); // Ganti ke strategi konsol
            handlePrintReport();
            // Mungkin tampilkan alert bahwa sudah dicetak ke konsol
            alertHandler.showAlert(Alert.AlertType.INFORMATION, "Print to Console", "Laporan telah dicetak ke konsol IDE Anda.");
        });

        filterLayout.getChildren().addAll(
            new Label("Start Date:"), startDatePicker,
            new Label("End Date:"), endDatePicker,
            loadButton, printButton, printToConsoleButton // Tambahkan tombol baru
        );
        topLayout.getChildren().add(filterLayout);
        root.setTop(topLayout);

        reportTable = new TableView<>();
        itemReports = FXCollections.observableArrayList(); // Inisialisasi itemReports
        reportTable.setItems(itemReports);

        TableColumn<ItemReport, String> productNameCol = new TableColumn<>("Nama Produk");
        productNameCol.setCellValueFactory(data -> data.getValue().productNameProperty());
        productNameCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<ItemReport, Integer> quantityCol = new TableColumn<>("Jumlah Terjual");
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        quantityCol.setStyle("-fx-alignment: CENTER;");

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.getColumns().addAll(productNameCol, quantityCol);
        root.setCenter(reportTable);

        Scene scene = new Scene(root, 900, 500); // Lebarkan scene untuk tombol baru
        primaryStage.setTitle("Laporan Item Paling Populer (Hibernate & Design Patterns)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLoadReport() {
        itemReports.clear(); // Pastikan itemReports sudah diinisialisasi

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            alertHandler.showAlert(Alert.AlertType.WARNING, "Input Error", "Please select both start and end dates.");
            return;
        }

        try {
            ObservableList<ItemReport> loadedReports = reportService.getPopularItems(startDate, endDate);
            itemReports.addAll(loadedReports);
        } catch (RuntimeException e) {
            e.printStackTrace();
            alertHandler.showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal Memuat Laporan Item Paling Populer: " + e.getMessage());
        }
    }

    private void handlePrintReport() {
        // Panggil metode print dari ReportPrinter dengan TableView dan datanya
        // Strategi yang aktif akan menentukan bagaimana ini dicetak
        if (itemReports == null || itemReports.isEmpty()) {
             alertHandler.showAlert(Alert.AlertType.INFORMATION, "Print Info", "Tidak ada data laporan untuk dicetak.");
             return;
        }
        reportPrinter.print(reportTable, itemReports);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Menggunakan metode shutdown dari HibernateUtil yang sudah di-refactor (Singleton)
        HibernateUtil.closeSessionFactory(); // Atau HibernateUtil.getInstance().shutdown();
    }
}