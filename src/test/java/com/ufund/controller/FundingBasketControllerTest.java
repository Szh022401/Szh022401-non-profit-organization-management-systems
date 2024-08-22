package com.ufund.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.ufund.model.Need;
import com.ufund.persistence.FundingBasketDAO;
import com.ufund.model.Helper;

/**

*/
@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController basketController;
    private FundingBasketDAO mockBasketDAO;
    private Need need = new Need(15, "Volunteer Snacks", (double) 19.99, 100,
            "Food", "In Need");



    @BeforeEach
    public void setupFundingBasketController() {
        mockBasketDAO = Mockito.mock(FundingBasketDAO.class);
        basketController = new FundingBasketController(mockBasketDAO);
    }

    @Test
    public void testCreateFundingBasket() throws IOException {
        // Setup
        Helper helper = new Helper("helperman123");
        Need[] emptyFundingBasket = new Need[0];

        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helper.getHelperId())).thenReturn(null);
        Mockito.when(mockBasketDAO.createFundingBasket(helper)).thenReturn(emptyFundingBasket);
        ResponseEntity<Need[]> response = basketController.createFundingBasket(helper);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(emptyFundingBasket, response.getBody());
    }

    @Test
    public void testCreateFundingBasketFound() throws IOException {
        // Setup
        Helper helper = new Helper("helperman123");
        Need[] expectedFundingBasket = new Need[2]; // simulates a helperFundingBasket
        expectedFundingBasket[0] = need;
        expectedFundingBasket[1] = need;

        // Mock the DAO behavior to get a FundingBasket by ID. 
        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helper.getHelperId())).thenReturn(expectedFundingBasket);
        Mockito.when(mockBasketDAO.createFundingBasket(helper)).thenReturn(expectedFundingBasket);
        ResponseEntity<Need[]> response = basketController.createFundingBasket(helper);

        // Analyze
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedFundingBasket, response.getBody());

    }

    @Test
    public void testCreateFundingBasketError() throws IOException {// Setup
        Helper helper = new Helper("helperman123");

        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helper.getHelperId())).thenThrow(new IOException("Simulated error"));
        ResponseEntity<Need[]> response = basketController.createFundingBasket(helper);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetFundingBasketByHelper() throws Exception {
        // Setup
        int helperId = 15;
        Need[] fundingBasket = new Need[2]; // simulates a helperFundingBasket
        fundingBasket[0] = need;
        fundingBasket[1] = need;

        // Mocks the fundingBasket return mechanism of FundingBasketDAO
        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helperId)).thenReturn(fundingBasket);
        ResponseEntity<Need[]> response = basketController.getFundingBasketByHelperId(helperId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fundingBasket, response.getBody());
    }

    @Test
    public void testGetBasketByHelperNotFound() throws IOException {
        // Setup
        int helperId = 15;

        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helperId)).thenReturn(null);
        ResponseEntity<Need[]> response = basketController.getFundingBasketByHelperId(helperId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetBasketByHelperError() throws IOException {
        // Setup
        int helperId = 15;

        Mockito.when(mockBasketDAO.getFundingBasketByHelperId(helperId)).thenThrow(new IOException("Simulated error"));
        ResponseEntity<Need[]> response = basketController.getFundingBasketByHelperId(helperId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddNeedToFundingBasket() throws IOException {
        // Setup
        Need[] fundingBasket = new Need[1];
        fundingBasket[0] = need;

        Mockito.when(mockBasketDAO.addNeedToFundingBasket(need)).thenReturn(fundingBasket);
        ResponseEntity<Need[]> response = basketController.addNeedToFundingBasket(need);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fundingBasket, response.getBody());
    }

    @Test
    public void testAddNeedToFundingBasketNotFound() throws IOException {
        // Setup

        Mockito.when(mockBasketDAO.addNeedToFundingBasket(need)).thenReturn(null);
        ResponseEntity<Need[]> response = basketController.addNeedToFundingBasket(need);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddNeedToFundingBasketError() throws IOException {

        Mockito.when(mockBasketDAO.addNeedToFundingBasket(need)).thenThrow(new IOException("Simulated Error"));
        ResponseEntity<Need[]> response = basketController.addNeedToFundingBasket(need);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromFundingBasket() throws IOException {
        Need[] expectedFundingBasket = new Need[2];
        expectedFundingBasket[0] = need;
        expectedFundingBasket[1] = need;

        Mockito.when(mockBasketDAO.removeNeedFromFundingBasket(need.getId())).thenReturn(expectedFundingBasket);
        ResponseEntity<Need[]> response = basketController.removeNeedFromFundingBasket(need.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFundingBasket, response.getBody());
    }

    @Test
    public void testRemoveNeedFromFundingBasketNotFound() throws IOException {

        Mockito.when(mockBasketDAO.removeNeedFromFundingBasket(need.getId())).thenReturn(null);
        ResponseEntity<Need[]> response = basketController.removeNeedFromFundingBasket(need.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromFundingBasketError() throws IOException {

        Mockito.when(mockBasketDAO.removeNeedFromFundingBasket(need.getId())).thenThrow(new IOException("Simulated Error"));
        ResponseEntity<Need[]> response = basketController.removeNeedFromFundingBasket(need.getId());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteBasket() throws IOException {

        Mockito.when(mockBasketDAO.deleteFundingBasket()).thenReturn(true);
        ResponseEntity<Boolean> response = basketController.deleteBasket();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteBasketNotFound() throws IOException {

        Mockito.when(mockBasketDAO.deleteFundingBasket()).thenReturn(false);
        ResponseEntity<Boolean> response = basketController.deleteBasket();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteBasketError() throws IOException {

        Mockito.when(mockBasketDAO.deleteFundingBasket()).thenThrow(new IOException("Simulated error"));
        ResponseEntity<Boolean> response = basketController.deleteBasket();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
