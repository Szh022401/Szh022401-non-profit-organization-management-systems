package com.ufund.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Terrance Huang
*/
@Tag("Model-tier")
public class ManagerTest {
	Manager validManager = new Manager("admin");
	Manager invalidManager = new Manager("seadoggy");

	@Test
	public void testManagerNameSuccess() {
		assertTrue(validManager.isValidName());
	}

	@Test
	public void testManagerNameFailure() {
		assertFalse(invalidManager.isValidName(), "Invalid name for Manager.");
	}

    @Test
    public void testConstructor() {
        assertEquals("admin", validManager.getUsername());
    }

    @Test
    public void testConstructorEmpty() {
        Manager empty = new Manager("");
        assertEquals("", empty.getUsername());
    }

    @Test
    public void testConstructorNull() {
        Manager nullManager = new Manager(null);
        assertNull(nullManager.getUsername());
    }
}
