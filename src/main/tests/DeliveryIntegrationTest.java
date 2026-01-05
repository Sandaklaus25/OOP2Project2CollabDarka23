import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.DeliveryService;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryIntegrationTest {

    private DeliveryService deliveryService;
    private Supplier testSupplier;
    private Product testProduct;
    private Delivery testDelivery;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService();

        testSupplier = new Supplier();
        testSupplier.setSupplierName("Test Supplier Ltd.");
        testSupplier.setPhoneNumber("0888123456");

        testProduct = new Product();
        testProduct.setProductName("Test Item " + System.currentTimeMillis());
        testProduct.setDeliveryPrice(10.0);
        testProduct.setSalePrice(15.0);
        testProduct.setQuantity(0);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(testSupplier);
            session.persist(testProduct);
            session.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            if (testDelivery != null && testDelivery.getId() != null) {
                session.remove(session.merge(testDelivery));
            }
            if (testProduct != null && testProduct.getId() != null) {
                session.remove(session.merge(testProduct));
            }
            if (testSupplier != null && testSupplier.getId() != null) {
                session.remove(session.merge(testSupplier));
            }

            session.getTransaction().commit();
        }
    }

    @Test
    void testCreateDelivery() {
        testDelivery = new Delivery();
        testDelivery.setSupplier(testSupplier);
        testDelivery.setProduct(testProduct);
        testDelivery.setQuantity(100);
        testDelivery.setDeliveryDate(LocalDate.now());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(testDelivery);
            session.getTransaction().commit();
        }

        List<Delivery> deliveries = deliveryService.getDeliveriesByPeriod(LocalDate.now(), LocalDate.now());

        boolean found = deliveries.stream().anyMatch(d -> d.getProduct().getProductName().equals(testProduct.getProductName()) && d.getQuantity() == 100);

        assertTrue(found, "Доставката не бе намерена в базата!");
    }
}