package com.ufund.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.persistence.FundingBasketFileDAO;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * Represents a Helper
 *
 * @author zehua sun

 */
public class Helper extends User {

    private static final Logger LOG = Logger.getLogger(User.class.getName());
    @JsonProperty("helperId")
    private int helperId;

    @JsonProperty("fundingBasket")
    private Need[] fundingBasket;


    /**
     * Creates a new Helper
     *
     * @param username The username of the helper
     */
    @JsonCreator
    public Helper(@JsonProperty("username") String username,
                  @JsonProperty("fundingBasket") Need[] fundingBasket,
                  @JsonProperty("helperId") int helperId) {
        super(username, helperId);  // This sets the id in User class
        this.helperId = helperId;    // This sets the helperId in Helper class
        this.fundingBasket = fundingBasket;
    }



    /**
     * Alternative constructor for Helper without funding basket
     *
     * @param username The username of the helper
     * @param helperId The ID of the helper
     */
    public Helper(@JsonProperty("username") String username,
                  @JsonProperty("helperId") int helperId) {
        super(username);
        this.helperId = helperId;

    }

    @Override
    public void setId(int id) {


        this.helperId = id; // Sync the helperId with the User ID
    }


    public int getHelperId() {
        return helperId;
    }

    /**
     * Gets the funding basket of the helper
     * @return The {@linkplain Need} array funding basket
     */
    public Need[] getFundingBasket() {
        return fundingBasket;
    }
    public Need[] searchNeeds(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return fundingBasket;
        }
        // Using a list to dynamically add matching needs
        List<Need> matchingNeeds = new ArrayList<>();

        for (Need need : fundingBasket) {
            // Assuming the Need class has a getName method
            if (need.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matchingNeeds.add(need);
            }
        }
        // Convert the list to an array and return
        return matchingNeeds.toArray(new Need[0]);
    }

    /**
     * sets the fundingBasket of a Helper
     * @param fundingBasket The fundingBasket being set
     */
    public void setFundingBasket(Need[] fundingBasket) {
        this.fundingBasket = fundingBasket;
    }

    @Override
    public String toString() {
        return String.format("User [username=%s, id=%d, manager=%b]", getUsername(), getId(), isManager());
    }

}
