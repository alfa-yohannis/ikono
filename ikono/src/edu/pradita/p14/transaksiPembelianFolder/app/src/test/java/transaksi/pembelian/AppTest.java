package transaksi.pembelian;

import org.junit.jupiter.api.Test;
import com.project.model.PurchaseTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTransactionTest {

    @Test
    void purchaseTransactionCanBeCreated() {
        PurchaseTransaction classUnderTest = new PurchaseTransaction();
        assertNotNull(classUnderTest);
    }
}
