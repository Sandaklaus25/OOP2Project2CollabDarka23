import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Sale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SaleEntityTest {

    private Product monitor;
    private Sale sale;

    @BeforeEach
    void setUp() {
        monitor = new Product();
        monitor.setProductName("Monitor Dell");
        monitor.setSalePrice(20.00);

        sale = new Sale();
        sale.setProduct(monitor);
        sale.setQuantity(5);
        sale.setSaleDate(LocalDate.now());
    }

    @AfterEach
    void tearDown() {
        monitor = null;
        sale = null;
    }

    @Test
    void testSaleTotalCalculation() {
        double expectedTotal = 100.00;
        double actualTotal = sale.getQuantity() * sale.getProduct().getSalePrice();

        assertEquals(expectedTotal, actualTotal);
        assertNotNull(sale.getSaleDate());
    }
}