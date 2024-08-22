package com.ufund.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.persistence.CupboardDAO;
import com.ufund.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**

 */
@Tag("Controller-tier")
public class CupboardControllerTest {
    private CupboardController cupboardController;
    private CupboardDAO mockCupboardDAO;

    /**
     * Before each test, create a new CupboardController object and inject
     * a mock Cupboard DAO
     */
    @BeforeEach
    public void setupCupboardController() {
        mockCupboardDAO = mock(CupboardDAO.class);
        cupboardController = new CupboardController(mockCupboardDAO);
    }

    @Test
    public void testGetNeed() throws IOException { //getNeed may throw IOException
        // Setup
        Need need = new Need(15,"Volunteer Snacks", 19.99, 100, "Food", "In Need");
        // When the same id is passed in, or mock Cupboard DAO will return the Need object
        when(mockCupboardDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // getNeed may throw IOException
        // Setup
        int needId = 15;
        // When the same id is passed in, our mock Cupboard DAO will return null, simulating
        // no need found
        when(mockCupboardDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = cupboardController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // getNeed may throw IOException
        // Setup
        int needId = 15;
        // When getNeed is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = cupboardController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need(15,"Volunteer Snacks", 19.99, 100, "Food", "In Need");
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockCupboardDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need(15,"Trash Bags", 19.99, 100, "Food", "In Need");
        // when createNeed  is called, return false simulating failed
        // creation and save
        when(mockCupboardDAO.createNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException{ // createNeed may throw IOException
        // Setup
        Need need = new Need(15,"Volunteer Snacks", 19.99, 100, "Food", "In Need");

        // When createNeed is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(15,"Volunteer Snacks", 19.99, 100, "Food", "In Need");
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = cupboardController.updateNeed(need);
        need.setName("Trash Bags");

        // Invoke
        response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(15,"Volunteer Snacks", 19.99, 100, "Food", "In Need");
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup 
        Need need = new Need(15,"Trash Bags", 19.99, 100, "Food", "In Need");
        // When updateNeed is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCupboard() throws IOException { // getCupboard may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(12,"Garbage Picker",24.99,50,"Tool","Ongoing");
        needs[1] = new Need(16,"Trash Bags",14.99,200,"Tool","Ongoing");
        // When getCupboard is called return the 
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.getCupboard();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetCupboardHandleException() throws IOException { // getCupboard may throw IOException
        // Setup
        // When getCupboard is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.getCupboard();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchCupboard() throws IOException { // searchCupboard may throw IOException
        // Setup
        String searchString = "bag";
        Need[] needs = new Need[2];
        needs[0] = new Need(12,"Garbage Picker",24.99,50,"Tool","Ongoing");
        needs[1] = new Need(16,"Trash Bags",14.99,200,"Tool","Ongoing"); 
        // When searchCupboard is called with the search string, return the two needs above
        when(mockCupboardDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchCupboardHandleException() throws IOException {
        // Setup
        String searchString = "an";
        // When findNeeds is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException {
        // Setup
        int needId = 15;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockCupboardDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException {
        // Setup
        int needId = 15;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockCupboardDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException {
        // Setup
        int needId = 15;
        // When deleteNeed is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).deleteNeed(needId);

        // Invoke 
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
