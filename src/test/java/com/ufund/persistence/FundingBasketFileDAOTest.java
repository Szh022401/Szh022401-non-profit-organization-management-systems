package com.ufund.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.model.Helper;
import com.ufund.model.Need;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;



import java.io.File;
import java.io.IOException;
import java.util.*;


@Tag("Persistence-tier")
public class FundingBasketFileDAOTest  {

    private FundingBasketFileDAO fundingBasketFileDAO;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = Mockito.mock(ObjectMapper.class);

        // Mock helper objects for the test
        Helper helper = new Helper("helperUsername");
        Helper helper1 = new Helper("helperUsername2");

        try {
            // Mock the behavior of the ObjectMapper
            Mockito.when(objectMapper.readValue(Mockito.any(File.class), Mockito.eq(Helper[].class)))
                    .thenReturn(new Helper[]{helper, helper1});

            fundingBasketFileDAO = new FundingBasketFileDAO("funding_basket.json", objectMapper);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing test", e);
        }
    }
    @Test
    public void testCreateFundingBasketNewUser() throws IOException {
        // 1. Setup: Create a new Helper without a fundingBasket
        Helper helper = new Helper("helperUsername");
        


        Need[] createdBasket = fundingBasketFileDAO.createFundingBasket(helper);

        assertTrue(fundingBasketFileDAO.fundingBaskets.containsKey(helper.getHelperId()));

        assertTrue(Arrays.equals(new Need[0], createdBasket));


        Need[] nullBasket = fundingBasketFileDAO.createFundingBasket(null);
        assertNull(nullBasket);
    }

    @Test
    public void testCreateFundingBasketExistingUser() throws IOException {
        Helper helper = new Helper("helperUsername");
        Need need1 = new Need(1, "Item1", 10.00, 5, "Type1", "Available");
        Need need2 = new Need(2, "Item2", 15.00, 3, "Type2", "Available");
        helper.setFundingBasket(new Need[]{need1, need2});

        fundingBasketFileDAO.fundingBaskets.put(helper.getHelperId(),helper);
        Need[] existingFundingBasket = fundingBasketFileDAO.createFundingBasket(helper);
        assertEquals(helper.getFundingBasket(), existingFundingBasket);
    }

    // Test case for successfully loading funding baskets from the file
    @Test
    public void testLoad() throws IOException {
        assertTrue(fundingBasketFileDAO.load()); // Assuming load method returns true if successful
    }

    // Test case for successfully saving funding baskets to the file
    @Test
    public void testSave() throws IOException {
        assertTrue(fundingBasketFileDAO.save()); // Assuming save method returns true if successful
    }




    // Test case for adding a new funding basket
    @Test
    public void testGetFundingBasketByHelperId() throws IOException {

        Helper helper = new Helper("helperUsername");
        Need need1 = new Need(1, "Item1", 10.00, 5, "Type1", "Available");
        Need need2 = new Need(2, "Item2", 15.00, 3, "Type2", "Available");
        helper.setFundingBasket(new Need[]{need1, need2});
        fundingBasketFileDAO.fundingBaskets.put(helper.getHelperId(), helper);


        Need[] fetchedBasket = fundingBasketFileDAO.getFundingBasketByHelperId(helper.getHelperId());


        assertNotNull(fetchedBasket);
        assertTrue(Arrays.equals(new Need[]{need1, need2}, fetchedBasket));


        Need[] nonExistentHelperBasket = fundingBasketFileDAO.getFundingBasketByHelperId(-1);
        assertNull(nonExistentHelperBasket);
    }

    @Test
    public void testAddNeedToFundingBasket() throws IOException {

        Helper helper = new Helper("helperUsername");


        helper.setFundingBasket(new Need[]{});
        fundingBasketFileDAO.createFundingBasket(helper);
        fundingBasketFileDAO.fundingBaskets.put(helper.getHelperId(), helper);

        Need need1 = new Need(1, "Item1", 10.00, 5, "Type1", "Available");
        Need need2 = new Need(2, "Item2", 15.00, 3, "Type2", "Available");

        fundingBasketFileDAO.addNeedToFundingBasket(need1);
        fundingBasketFileDAO.addNeedToFundingBasket(need2);

        Need[] updatedBasket = fundingBasketFileDAO.fundingBaskets.get(helper.getHelperId()).getFundingBasket();

        assertEquals(2, updatedBasket.length);

        List<Need> basketList = Arrays.asList(updatedBasket);
        assertTrue(basketList.contains(need1));
        assertTrue(basketList.contains(need2));
    }

    @Test
    public void testRemoveNeedFromFundingBasket() throws IOException {
        Helper helper = new Helper("helperUsername");

        fundingBasketFileDAO.createFundingBasket(helper);
        Need need1 = new Need(1, "Item1", 10.00, 5, "Type1", "Available");
        Need need2 = new Need(2, "Item2", 15.00, 3, "Type2", "Available");
        helper.setFundingBasket(new Need[]{need1, need2});
        


        fundingBasketFileDAO.fundingBaskets.put(helper.getHelperId(), helper);
        fundingBasketFileDAO.loadTargetFundingBasket();

        Need[] updatedBasket = fundingBasketFileDAO.removeNeedFromFundingBasket(need1.getId());
        assertNotNull(updatedBasket);
        assertEquals(1, updatedBasket.length);
        assertFalse(Arrays.asList(updatedBasket).contains(need1));
        assertTrue(Arrays.asList(updatedBasket).contains(need2));
    }

    // Test case for deleting a funding basket by ID
    @Test
    public void testDeleteNeed() throws IOException {
        Helper helper = new Helper("helperUsername");
        fundingBasketFileDAO.createFundingBasket(helper);

        helper.setFundingBasket(new Need[]{});
        fundingBasketFileDAO.fundingBaskets.put(helper.getHelperId(), helper);

        Need need1 = new Need(1, "Item1", 10.00, 5, "Type1", "Available");
        Need need2 = new Need(2, "Item2", 15.00, 3, "Type2", "Available");

        fundingBasketFileDAO.addNeedToFundingBasket(need1);
        fundingBasketFileDAO.addNeedToFundingBasket(need2);


        boolean deleted = fundingBasketFileDAO.deleteFundingBasket();
        assertTrue(deleted);
        Need[] needs = fundingBasketFileDAO.getFundingBasketByHelperId(helper.getHelperId());
        assertEquals(0, needs.length);
    }

}
