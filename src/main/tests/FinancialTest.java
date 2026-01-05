import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Sale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinancialTest {

    private Product product;
    private Sale sale;

    @BeforeEach
    void setUp() {
        product = new Product();
        sale = new Sale();
    }

    @AfterEach
    void tearDown() {
        product = null;
        sale = null;
    }

    @Test
    void testProfitCalculation() {
        product.setProductName("Laptop HP");
        product.setDeliveryPrice(100.0);
        product.setSalePrice(150.0);

        sale.setProduct(product);
        sale.setQuantity(2);

        double revenue = sale.getQuantity() * product.getSalePrice();
        double cost = sale.getQuantity() * product.getDeliveryPrice();
        double profit = revenue - cost;

        assertEquals(300.0, revenue);
        assertEquals(200.0, cost);
        assertEquals(100.0, profit);
    }

    @Test
    void testLossCalculation() {
        product.setDeliveryPrice(50.0);
        product.setSalePrice(40.0);

        sale.setProduct(product);
        sale.setQuantity(10);

        double revenue = sale.getQuantity() * product.getSalePrice();
        double cost = sale.getQuantity() * product.getDeliveryPrice();
        double profit = revenue - cost;

        assertEquals(-100.0, profit);
    }
}