package com.ufund.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ufund.model.Need;
import com.ufund.model.Helper;

/**
 * Implements the functionality for JSON file-based persistence for FundingBasket
 *
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 *
 * @author zehua sun
 *
 */
@Component
public class FundingBasketFileDAO implements FundingBasketDAO {

    private static final Logger LOG = Logger.getLogger(FundingBasketFileDAO.class.getName());
    Map<Integer, Helper> fundingBaskets;
    private final ObjectMapper objectMapper;
    private final String filename;

    Map<Integer, Need> targetFundingBasket; // target helper's funding basket

    private int targetHelper;



    /**
     * Creates a FundingBasket File Data Access Object
     *
     * @param filename The file to read from
     * @param objectMapper Provides JSON Object to/from Java Object
     * @throws IOException when file cannot be accessed or read from
     */
    public FundingBasketFileDAO(@Value("${fundingBasket.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }



    /**
     * Saves the {@linkplain Need needs} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Need needs} were written successfully
     *
     * @throws IOException when the file cannot be accessed or written to
     */
    boolean  save() throws IOException {
        Helper[] helpersArray = fundingBaskets.values().toArray(new Helper[0]);
        objectMapper.writeValue(new File(filename), helpersArray);
        return true;
    }

    /**
     * Loads {@linkplain Helper helper} funding basket from the JSON file into the map
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    boolean load() throws IOException {
        fundingBaskets = new TreeMap<>();

        // deserializes the JSON objects from the file into an array of all of the funding baskets
        // readValue will throw an error for any issues reading from the file
        Helper[] helpersArray = objectMapper.readValue(new File(filename), Helper[].class); // get array of all helpers
        if(helpersArray == null) {
            LOG.warning("The file does not contain any valid data.");
            return false;
        }
        for (Helper helper : helpersArray) {
            fundingBaskets.put(helper.getHelperId(), helper); // put the helpers into the Tree Map
        }
        return true;
    }

    boolean loadTargetFundingBasket() throws IOException {
        if (targetFundingBasket == null) {
            targetFundingBasket = new TreeMap<>();
        }

        Need[] needs = getFundingBasketByHelperId(targetHelper);
        if (needs == null) {
            needs = new Need[0]; // Ensures that the array is never null
        }

        for (Need need : needs) {
            targetFundingBasket.put(need.getId(), need);
        }
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] createFundingBasket(Helper helper) throws IOException {
        Need[] newFundingBasket;
        if(helper == null){
            return null;
        }
        if(getFundingBasketByHelperId(helper.getHelperId()) != null) { // check to see if the helper is already in the file system
            newFundingBasket = getFundingBasketByHelperId(helper.getHelperId()); // if so, return their existing funding basket
        }else {
        helper.setFundingBasket(new Need[0]);
        fundingBaskets.put(helper.getHelperId(), helper); // put the helper into the fundingBaskets map
        newFundingBasket = helper.getFundingBasket();
        }
        save(); // save the change
        targetHelper = helper.getHelperId(); // sets the target helper for the local client
        loadTargetFundingBasket(); // load the target funding basket map
        return newFundingBasket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Helper[] getAllFundingBaskets() throws IOException {
        ArrayList<Helper> fundingBasketsArrayList = new ArrayList<>();
        for(Helper helper : fundingBaskets.values()) {
            fundingBasketsArrayList.add(helper);
        }
        Helper[] helpersArray = new Helper[fundingBasketsArrayList.size()]; // converts and returns Need array of it
        fundingBasketsArrayList.toArray(helpersArray);
        return helpersArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] getFundingBasketByHelperId(int helperId) throws IOException {
        Helper helper = fundingBaskets.get(helperId);
        return helper != null ? helper.getFundingBasket() : null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] addNeedToFundingBasket(Need need) throws IOException {
        if(need == null){
            return   new Need[0];
        }
        loadTargetFundingBasket(); // load the target funding basket map
        targetFundingBasket.put(need.getId(), need); // put the incoming need into the target funding basket map

        Helper helper = fundingBaskets.get(targetHelper);    // gets the targetHelper's fundingbasket
        if (helper == null){
            return null;
        }
        helper.setFundingBasket(targetFundingBasket.values().toArray(new Need[targetFundingBasket.size()])); // sets it to the updated targetFundingBasket

        save(); // save and return the updated fundingBasket
        return helper.getFundingBasket();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] removeNeedFromFundingBasket(int id) throws IOException {
        loadTargetFundingBasket(); // load the target funding basket
        targetFundingBasket.remove(id); // remove the need that matches the id from the target map

        Helper helper = fundingBaskets.get(targetHelper);    // gets the targetHelper's fundingbasket
        helper.setFundingBasket(targetFundingBasket.values().toArray(new Need[targetFundingBasket.size()])); // sets it to the updated arraylist as an array

        save(); // saves and returns the new fundingBasket
        return helper.getFundingBasket();
    }


    /**
     *{@inheritDoc}
     */
    @Override
    public boolean deleteFundingBasket() throws IOException {
        Need[] emptyFundingBasket = new Need[0];  // creates an empty need array
        fundingBaskets.get(targetHelper).setFundingBasket(emptyFundingBasket); // sets the helper's, of helperId, funding basket to the empty Need array
        return save();    // save to files an empty funding
    }
}
