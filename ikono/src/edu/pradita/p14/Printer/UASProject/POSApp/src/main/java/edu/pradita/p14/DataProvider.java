package edu.pradita.p14;

import java.util.List;

public interface DataProvider {
    List<Transaction> getAllTransactions();
    void saveTransaction(Transaction t);
    void updateTransaction(Transaction t);
    void deleteTransaction(Transaction t);
} 