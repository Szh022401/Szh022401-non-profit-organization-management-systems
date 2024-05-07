package com.ufund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ufund.persistence.FundingBasketDAO;
import com.ufund.model.Need;
import com.ufund.model.Helper;

@RestController
@RequestMapping("fundingBasket")
public class FundingBasketController {

    private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
    private final FundingBasketDAO fundingBasketDao;

    public FundingBasketController(FundingBasketDAO fundingBasketDao) {
        this.fundingBasketDao = fundingBasketDao;
    }

    /**
     * creates a basket for new user; to be used on login
     * 
     * @param helper the helper being assigned a basket
     * @return ResponseEntity with created FundingBasket of {@link Need need} objects and HTTP status of CREATED<br>
     * ResponseEntity with existing FundingBasket and an HTTP status of FOUND if {@link Need need} object FundingBasket already exists for the Helper
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Need[]> createFundingBasket(@RequestBody Helper helper) {
        LOG.info("POST /fundingBasket " + helper.getHelperId());
        try {
            if (fundingBasketDao.getFundingBasketByHelperId(helper.getHelperId()) ==  null) { // if the helper does not already exist
                Need[] newFundingBasket = fundingBasketDao.createFundingBasket(helper); // get a newFundingBasket for the helper
                return new ResponseEntity<Need[]>(newFundingBasket, HttpStatus.CREATED);   
            }else {
                // if a helper with the id already exists,
                // the existing fundingBasket is returned with a status of CONFLICT
                Need[] fundingBasket = fundingBasketDao.createFundingBasket(helper);
                return new ResponseEntity<Need[]>(fundingBasket, HttpStatus.FOUND);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * gets all of the funding baskets of the Helper's within the file system
     * 
     * @return ResponseEntity with the an array of the {@link Need need} arrays stored within the funding_basket file and a status of OK<br>
     * ResponseEntity with an empty array, if no entries into the file, and a status of OK
     */
    @GetMapping("")
    public ResponseEntity<Helper[]> getAllFundingBaskets() {
        LOG.info("GET /fundingBasket");
        try{
            Helper[] fundingBaskets = fundingBasketDao.getAllFundingBaskets();
            return new ResponseEntity<>(fundingBaskets,HttpStatus.OK);
        }catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * gets the fundingBasket of a Helper with a the helper's id
     * 
     * @param helperId the id of the helper
     * @return ResponseEntity with the FundingBasket of {@link Need need} objects of the Helper with the helper id
     * and a status of OK<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no FundingBasket exists for the helperId<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{helperId}")
    public ResponseEntity<Need[]> getFundingBasketByHelperId(@PathVariable int helperId) {
        LOG.info("GET /fundingBasket/" + helperId);
        try {
            Need[] basket = fundingBasketDao.getFundingBasketByHelperId(helperId);
            if (basket == null) {  // if no fundingBasket exists for the user
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Need[]>(basket, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
         * adds a need to the fundingBasket of a {@linkplain Helper helper}
     * 
     * @param helper the helper whose fundingBasket the need is being added to
     * @param need the need to add
     * @return ResponseEntity with the updated FundingBasket of {@link Need need} objects of the Helper and a status of OK<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no Funding Basket exists for the helper<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Need[]> addNeedToFundingBasket(@RequestBody Need need) {

        LOG.info("PUT /fundingBasket/" + "need/" + need.getId());
        try {
            Need[] updatedBasket = fundingBasketDao.addNeedToFundingBasket(need); // add the need to the helper's basket
            if (updatedBasket == null) {
                // a fundingBasket for the helper does not exist
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Need[]>(updatedBasket, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a {@linkplain Need need} from the {@linkplain Helper helper}'s Funding Basket
     * 
     * @param helper the helper whose fundingBasket the need is to be removed from
     * @param need the need to remove
     * @return ResponseEntity with the updated FundingBasket of {@link Need need} objects of the Helper and a status of OK<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no Funding Basket exists for the helper<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need[]> removeNeedFromFundingBasket(@RequestBody int id) {
        LOG.info("DELETE /fundingBasket/" + "need/" + id);
        try {
            Need[] updatedBasket = fundingBasketDao.removeNeedFromFundingBasket(id); // remove the need from the helper's basket
            if (updatedBasket == null) {
                // a fundingBasket for the helper does not exist
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Need[]>(updatedBasket, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * deletes all of the contents of the currently logged in Helper's FundingBasket; this is used to checkout
     * 
     * @param helperId the helperId of the helper
     * @return ResponseEntity with HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{helperId}")
    public ResponseEntity<Boolean> deleteBasket() {
        LOG.info("DELETE /fundingBasket/");
        try {
            boolean deleted = fundingBasketDao.deleteFundingBasket();
            if(deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
