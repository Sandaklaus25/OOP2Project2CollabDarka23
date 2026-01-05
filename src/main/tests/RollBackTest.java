import com.example.storageinventory.model.Product;
import com.example.storageinventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RollBackTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService();
    }

    @Test
    void testRollbackOnSaveFailure() {
        Product invalidProduct = new Product(null, 10, -50.00, 10.0, 5);

        assertThrows(RuntimeException.class, () -> service.saveProduct(invalidProduct));

        boolean exists = service.getAllProducts().stream().anyMatch(p -> p.getDeliveryPrice() == -50.00);

        assertFalse(exists, "Счупеният продукт не трябва да е в базата");
    }
}