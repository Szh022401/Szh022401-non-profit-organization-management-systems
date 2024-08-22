package com.ufund.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ufund.persistence.FundingBasketDAO;
import com.ufund.model.Need;
import com.ufund.model.Helper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("fundingBasket")
@CrossOrigin(origins = "http://localhost:4200")
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
    @PostMapping("/createFundingBasket")
    public ResponseEntity<Need[]> createFundingBasket(@RequestBody Helper helper) {

        ObjectMapper mapper = new ObjectMapper();
        try {

            List<Map<String, Object>> users = mapper.readValue(new File("data/users.json"), new TypeReference<List<Map<String, Object>>>(){});

            for (Map<String, Object> user : users) {
                if (user.get("username").equals(helper.getUsername())) {
                    int existingId = (Integer) user.get("id");
                    helper.setId(existingId);  // Set the helperId from users.json
                    LOG.info("Set helperId from users.json: " + existingId);
                    break;
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error reading users.json", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("POST /fundingBasket " + helper.getHelperId());
        try {
            if (fundingBasketDao.getFundingBasketByHelperId(helper.getHelperId()) ==  null) { // if the helper does not already exist
                Need[] newFundingBasket = fundingBasketDao.createFundingBasket(helper); // get a newFundingBasket for the helper
                return new ResponseEntity<Need[]>(newFundingBasket, HttpStatus.CREATED);
            }else {
                // if a helper with the id already exists,
                // the existing fundingBasket is returned with a status of CONFLICT
                Need[] fundingBasket = fundingBasketDao.createFundingBasket(helper);
                return new ResponseEntity<Need[]>(fundingBasket, HttpStatus.CONFLICT);

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
    @PutMapping("/{id}")
    public ResponseEntity<Need[]> addNeedToFundingBasket(@PathVariable int id, @RequestBody Need need) {
        LOG.info("PUT /fundingBasket/" + id);

        try {
            Need[] updatedBasket = fundingBasketDao.addNeedToFundingBasket(id, need);

            if (updatedBasket == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(updatedBasket, HttpStatus.OK);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unexpected error occurred: " + e.getMessage(), e);
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
    @DeleteMapping("/{helperId}/{id}")
    public ResponseEntity<Need[]> removeNeedFromFundingBasket(@PathVariable int helperId, @PathVariable int id) {
        LOG.info("DELETE /fundingBasket/" + helperId + "/" + id);
        try {
            Need[] updatedBasket = fundingBasketDao.removeNeedFromFundingBasket(helperId,id);
            LOG.info(Arrays.toString(updatedBasket));
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
    @PutMapping("/test")
    public ResponseEntity<String> testPutMethod() {
        return new ResponseEntity<>("PUT method is working!", HttpStatus.OK);
    }


}
