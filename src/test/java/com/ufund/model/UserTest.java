package com.ufund.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the User model class.
 * This class contains methods that test each individual function of the User model.
 * @author zehua sun
 */
@Tag("Model-tier")
public class UserTest {

    /**
     * Test the constructor of the User class.
     * Ensures that a new User object's 'manager' and 'helper' fields are false upon creation.
     */
    @Test
    public void testCtor() {
        // Create a new user instance
        User user = new User("testUser");

        // Assert that the new user is neither a manager nor a helper by default
        assertFalse(user.isManager());
    }

    /**
     * Test to ensure that the default user is neither a manager nor a helper.
     */
    @Test
    public void testIsNotManagerOrHelper() {
        // Define a regular username
        String username = "regularUser";

        // Create a new user instance with the regular username
        User user = new User(username);

        // Assert that a regular user is neither a manager nor a helper
        assertFalse(user.isManager());
    }

    /**
     * Test to confirm that a user with the 'admin' username is considered a manager.
     */
    @Test
    public void testIsManager() {
        // Define an admin username
        String username = "admin";

        // Create a new user instance with the admin username
        User user = new User(username);

        // Assert that the user with an admin username is a manager but not a helper
        assertTrue(user.isManager());
    }

    /**
     * Test to verify that a user with the 'helper' username is recognized as a helper.
     */
    @Test
    public void testIsHelper() {
        // Define a helper username
        String username = "helper";

        // Create a new user instance with the helper username
        User user = new User(username);

        // Assert that the user with a helper username is a helper but not a manager
        assertFalse(user.isManager());  // Ensure manager flag is not mistakenly set
    }

    /**
     * Test the toString method of the User class.
     * Ensures the method returns a string that matches the expected format.
     */
    @Test
    public void testToString() {
        // Create a new user instance with a test username
        String username = "testUser";
        User user = new User(username);

        // Define the expected string format, assuming the user is neither a manager nor a helper
        String expectedString = String.format(User.STRING_FORMAT, username, false);

        // Assert that the returned string matches the expected format and content
        assertEquals(expectedString, user.toString());
    }
}
