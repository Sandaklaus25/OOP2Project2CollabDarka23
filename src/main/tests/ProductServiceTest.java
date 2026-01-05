import com.example.storageinventory.model.Product;
import com.example.storageinventory.service.ProductService;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductServiceTest {

    private ProductService service;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        service = new ProductService();
        String uniqueName = "TestProduct_" + System.currentTimeMillis();
        testProduct = new Product(uniqueName, 10, 5.50, 8.5, 5);
    }

    @AfterEach
    void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            if (testProduct != null && testProduct.getId() != null) {
                session.remove(session.merge(testProduct));
            }
            session.getTransaction().commit();
        }
        service = null;
        testProduct = null;
    }

    @Test
    void testCreateAndFetchProduct() {
        service.saveProduct(testProduct);

        List<Product> newList = service.getAllProducts();

        boolean found = newList.stream().anyMatch(p -> p.getProductName().equals(testProduct.getProductName()));

        assertTrue(found);
    }
}