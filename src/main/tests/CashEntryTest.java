import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

record CashEntry(String date, String type, String info, Double amount, String operator) {
}

public class CashEntryTest {

    CashEntry entry;

    @BeforeEach
    void setUp() {
        entry = new CashEntry("2026-01-05", "ПРИХОД", "Тест продажба", 123.123, "Ivan");
    }

    @AfterEach
    void tearDown() {
        entry = null;
    }

    @Test
    void testCashEntryCreation() {
        assertEquals("Ivan", entry.operator());
        assertEquals(123.123, entry.amount());
        assertEquals("ПРИХОД", entry.type());
    }

    @Test
    void testStringFormatting() {
        String formatted = String.format("%.2f", entry.amount());
        assertEquals("123.12", formatted);
    }
}