package com.ufund.persistence;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.model.Need;

@Tag("Persistence-tier")
public class CupboardFileDAOTest {

    private CupboardFileDAO cupboardFileDAO;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws StreamReadException, DatabindException, IOException {
        objectMapper = mock(ObjectMapper.class);

        Need[] needs = new Need[]{new Need(1, "Item1", 10.00, 5, "Type1", "Available")};

        try {
            when(objectMapper.readValue(Mockito.any(File.class), 
            eq(Need[].class)))
                .thenReturn(needs);
            cupboardFileDAO = new CupboardFileDAO("data/cupboard.json", objectMapper);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing test", e);
        }
    }

    @Test
    public void testGetNeeds() {
        Need[] needs = cupboardFileDAO.getNeeds();
        assertEquals(1, needs.length);
        assertEquals("Item1", needs[0].getName());
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need newNeed = new Need(2, "Item2", 15.0, 3, "Type2", "Available");
        Need createdNeed = cupboardFileDAO.createNeed(newNeed);

        Need[] needs = cupboardFileDAO.getNeeds();
        assertEquals(2, needs.length);
        assertEquals("Item2", createdNeed.getName());
    }

    @Test
    public void testUpdateNeed() throws IOException {
        Need updatedNeed = new Need(1, "UpdatedItem1", 12.0, 2, "Type1", "Unavailable");
        Need result = cupboardFileDAO.updateNeed(updatedNeed);
        Need nonexistentNeed = new Need(2, "non", 0.0, 0, "Nonexistent", "Nonexistent");
        Need nullResult = cupboardFileDAO.updateNeed(nonexistentNeed);
        assertNull(nullResult);

        assertEquals("UpdatedItem1", result.getName());

        Need[] needs = cupboardFileDAO.getNeeds();
        assertEquals("UpdatedItem1", needs[0].getName());
    }

    @Test
    public void testDeleteNeed() throws IOException {
        boolean deleted = cupboardFileDAO.deleteNeed(1);
        assertTrue(deleted);
        boolean falseDelete = cupboardFileDAO.deleteNeed(2);
        assertFalse(falseDelete);

        Need[] needs = cupboardFileDAO.getNeeds();
        assertEquals(0, needs.length);
    }

    @Test
    public void testFindNeeds() {
        Need need1 = new Need(1, "Item 1", 10.0, 5, "Type 1", "Available");
        Need need2 = new Need(2, "Item 2", 15.0, 10, "Type 2", "Available");
        Need need3 = new Need(3, "Item 3", 20.0, 15, "Type 1", "Available");

        cupboardFileDAO.cupboard.put(1, need1);
        cupboardFileDAO.cupboard.put(2, need2);
        cupboardFileDAO.cupboard.put(3, need3);

        Need[] result = cupboardFileDAO.findNeeds("Item");

        assertEquals(3, result.length);
    }

    @Test
    public void testGetNeed() {
        Need need1 = new Need(1, "Item 1", 10.0, 5, "Type 1", "Available");
        Need need2 = new Need(2, "Item 2", 15.0, 10, "Type 2", "Available");

        cupboardFileDAO.cupboard.put(1, need1);
        cupboardFileDAO.cupboard.put(2, need2);

        Need result1 = cupboardFileDAO.getNeed(1);
        Need result2 = cupboardFileDAO.getNeed(2);
        Need nonexistentNeed = cupboardFileDAO.getNeed(3);

        assertEquals(need1, result1);
        assertEquals(need2, result2);
        assertNull(nonexistentNeed);
    }
}
