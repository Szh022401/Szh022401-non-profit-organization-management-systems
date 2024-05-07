package com.ufund.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test the Helper model class
 */
@Tag("Model-tier")
public class HelperTest {
    private Helper helper;

    @BeforeEach
    public void setupHelper() {
        String username = "helperman123";
        helper = new Helper(username);
    }

    @Disabled
    @Test
    public void testCtor() {
        // Setup
        String expected_username = "helperman123";
        int expected_helperId = 1;


        // Analyze
        assertEquals(expected_username, helper.getUsername());
        assertEquals(expected_helperId, helper.getHelperId());
    }

    @Disabled
    @Test
    public void testCtorMultiple() {  // this test tests the static helperId incrementation
        // Setup
        String expected_username1 = "helperman123";
        int expected_helperId1 = 3;
        String expected_username2 = "helperwoman123";
        int expected_helperId2 = 4;

        // Invoke
        Helper helper1 = new Helper(expected_username1);
        Helper helper2 = new Helper(expected_username2);

        // Analyze
        assertEquals(expected_helperId1, helper1.getHelperId());
        assertEquals(expected_helperId2, helper2.getHelperId());
    }

    @Test
    public void testResetNextHelperId() {
        Helper.resetNextHelperId();
        Helper newHelper = new Helper("new");
        assertEquals(1, newHelper.getHelperId());
    }
}
