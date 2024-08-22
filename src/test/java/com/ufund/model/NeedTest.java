package com.ufund.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-tier")
public class NeedTest {

    private Need need;

    @BeforeEach
    public void setUp() {
        // Initialize a Need object for testing
        int id = 1;
        String name = "Test Need";
        Double price = 100.00;
        int quantity = 5;
        String type = "Test Type";
        String status = "Active";

        need = new Need(id, name, price, quantity, type, status);
    }

    @Test
    public void testGetters() {
        // Test getters
        assertEquals(1, need.getId());
        assertEquals("Test Need", need.getName());
        assertEquals(100.00, need.getPrice(), 0.001); // Use delta for double comparison
        assertEquals(5, need.getQuantity());
        assertEquals("Test Type", need.getType());
        assertEquals("Active", need.getStatus());
    }

    @Test
    public void testSetName() {
        // Test setter for name
        need.setName("Updated Name");
        assertEquals("Updated Name", need.getName());
    }

    @Test
    public void testSetPrice() {
        // Test setter for price
        need.setPrice("15.5"); // String representation of price
        assertEquals(15.5, need.getPrice(), 0.001);
    }

    @Test
    public void testSetQuantity() {
        // Test setter for quantity
        need.setQuantity("7"); // String representation of quantity
        assertEquals(7, need.getQuantity());
    }

    @Test
    public void testToString() {
        // Test the toString method
        String expectedString = "Need [id=1, name=Test Need, price=100.00, quantity=5, type=Test Type, status=Active]";
        assertEquals(expectedString, need.toString());
    }
}
