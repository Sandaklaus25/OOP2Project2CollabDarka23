import com.example.storageinventory.model.User;
import com.example.storageinventory.util.UserSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    private User user;

    @BeforeEach
    void setUp() {
        UserSession.cleanSession(); // Гарантираме чисто начало

        user = new User();
        user.setUsername("ivan_test");
        user.setPassword("1234");
    }

    @AfterEach
    void tearDown() {
        UserSession.cleanSession(); // Гарантираме чисто излизане
        user = null;
    }

    @Test
    void testLoginLogout() {
        UserSession.setCurrentUser(user);

        assertNotNull(UserSession.getCurrentUser());
        assertEquals("ivan_test", UserSession.getCurrentUser().getUsername());

        UserSession.cleanSession();

        assertNull(UserSession.getCurrentUser());
    }
}