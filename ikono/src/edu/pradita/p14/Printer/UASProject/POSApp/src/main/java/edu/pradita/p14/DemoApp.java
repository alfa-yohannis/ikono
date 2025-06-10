package edu.pradita.p14;

public class DemoApp {
    public static void main(String[] args) {
        // 1. Singleton: Database Connection
        DatabaseConnection dbConn = DatabaseConnection.getInstance();
        dbConn.connect();

        // 2. SOLID: Proses transaksi dengan service
        Transaction transaction = new Transaction(1, 2, 3, 4, 5000.0);
        TransactionService service = new SimpleTransactionService();
        service.processTransaction(transaction);

        // 3. Observer: Notifikasi setelah transaksi
        TransactionNotifier notifier = new TransactionNotifier();
        EmailNotifier emailNotifier = new EmailNotifier();
        notifier.addObserver(emailNotifier);
        notifier.notifyObservers(transaction);

        // 4. Adapter: Pembayaran dengan sistem lama melalui interface baru
        OldPaymentSystem oldPayment = new OldPaymentSystemImpl();
        NewPaymentSystem paymentAdapter = new PaymentAdapter(oldPayment);
        paymentAdapter.makePayment(10000.0);

        // Output akan menunjukkan urutan proses dan pemanggilan class-class design pattern
    }
}
