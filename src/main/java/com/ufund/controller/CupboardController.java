package com.ufund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.persistence.CupboardDAO;
import com.ufund.model.Need;


/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this calls as a REST API
 * method handler to the Spring framework
 * 
 *
 */
@RestController
@RequestMapping("cupboard")
public class CupboardController {
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
    private CupboardDAO cupboardDao;

    /**
     * Creates a REST API controller to respond to requests
     * 
     * @param cupboardDAO The {@link CupboardDAO Cupboard Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CupboardController(CupboardDAO cupboardDao) {
        this.cupboardDao = cupboardDao;
    }

    /**
     * Creates a {@linkplain Need need} with the provided need object
     * 
     * @param need The {@link Need need} to create
     * 
     * @return ResponseEntity with created {@link Need need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Need need} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /cupboard " + need); // logs the input command
        try{
            
            Need newNeed = cupboardDao.createNeed(need); // create a new need with the need request
            if (newNeed == null) { // if cupboarDAO returns null, a need with the same id already exists
                return new ResponseEntity<>(HttpStatus.CONFLICT); // report a conflict
            }
            // if no need with the same name already exists
            return new ResponseEntity<Need>(newNeed, HttpStatus.CREATED); // returns the newNeed and a success status
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<Need>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty) and 
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getCupboard() {
        LOG.info("GET /cupboard");
        try{
            Need[] cupboard = cupboardDao.getNeeds(); // gets the array of all needs
            return new ResponseEntity<Need[]>(cupboard,HttpStatus.OK); // sends it to the API
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Need need} for the given id
     * 
     * @param id The id used to locate the {@link Need need}
     * 
     * @return ResponseEntity with {@link Need need} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        LOG.info("GET /cupboard/" + id);
        try{
            Need need = cupboardDao.getNeed(id); // get the need with the matching id
            if (need != null){
                return new ResponseEntity<Need>(need,HttpStatus.OK); // return it if found
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // report 404 if not
            }
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update the {@linkplain Need need} with the provided {@linkplain Need need} object, if it exists
     * @param need The {@link Need need} to update
     * 
     * @return ResponseEntity with updated {@link Need need} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        LOG.info("PUT /cupboard " + need);
        try{
            Need upNeed = cupboardDao.updateNeed(need);
            if(upNeed != null){ // if updateNeed operation is successful
                return new ResponseEntity<Need>(upNeed, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // report FNF error if updateNeed failed
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<Need>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Need needs}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all needs in the cupboard that contain the text "vo"
     * GET http://localhost:8080/cupboard/?name=vo
     */
    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name) {
        LOG.info("GET /cupboard/?name="+name);
        try{
            Need[] needs = cupboardDao.findNeeds(name);
            return new ResponseEntity<Need[]>(needs,HttpStatus.OK);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<Need[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need} to be deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /cupboard/" + id);
        try{
            boolean delNeed = cupboardDao.deleteNeed(id); // calls delete need
            if(delNeed == true){
                return new ResponseEntity<>(HttpStatus.OK); // gives ok if delete was successful
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // reports FNF on delete failure
        }
        catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
