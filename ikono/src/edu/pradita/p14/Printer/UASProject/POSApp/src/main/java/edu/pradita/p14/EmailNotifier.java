package edu.pradita.p14;

public class EmailNotifier implements TransactionObserver {
    public void onTransactionProcessed(Transaction transaction) {
        System.out.println("Email sent for transaction: " + transaction.getTransactionId());
    }
} 