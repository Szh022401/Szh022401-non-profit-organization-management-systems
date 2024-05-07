package com.ufund.persistence;

import java.io.IOException;
import com.ufund.model.Need;
import com.ufund.model.Helper;

/**
 * Defines the interface for the Funding Basket persistence.
 *
 * @author ZehuaSun
 *
 */
public interface FundingBasketDAO {

    /**
     * Creates a fundingBasket for a new helper. When a helper logs in, this happens
     * 
     * @param helper The helper who a funding basket is being created for
     * 
     * @return The empty array created, if the helper is new to the system; the existing basket for returning helpers
     * 
     * @throws IOException if an issue with underlying storage occured
     */
    Need[] createFundingBasket(Helper helper) throws IOException; 


    /**
     * Retrieves all of the funding baskets within the file system
     * 
     * @return The array of helper data saved to the file; this can be empty
     * @throws IOException if an issue with underlying storage occured
     */
    Helper[] getAllFundingBaskets() throws IOException;

    /**
     * Retrieves a funding basket by the given helper ID.
     *
     * @param helperId The ID of the helper.
     *
     * @return The funding basket associated with the helper ID.
     *
     * @throws IOException if an issue with underlying storage.
     */
    Need[] getFundingBasketByHelperId(int helperId) throws IOException;

    /**
     * Adds a need to the target helper's funding basket.
     *
     * @param need The need being added
     *
     * @return The need added to the funding basket.
     *
     * @throws IOException if an issue with underlying storage.
     */
    Need[] addNeedToFundingBasket(Need need) throws IOException;

    /**
     * Removes a need from the target helper's funding basket
     * @param id the id of the need to be removed
     * @return The need removed from the funding basket
     * @throws IOException
     */
    Need[] removeNeedFromFundingBasket(int id) throws IOException;

    /**
     * Deletes the contents of the fundingBasket of the currently logged in user.
     *
     *
     * @return true if the funding basket was successfully deleted, false otherwise.
     *
     * @throws IOException if an issue with underlying storage.
     */
    boolean deleteFundingBasket() throws IOException;
}
