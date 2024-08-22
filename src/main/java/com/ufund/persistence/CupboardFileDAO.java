package com.ufund.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.model.Need;

/**
 * Implements the functionality for JSON file-based persistence for Cupboard
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 *
 */
@Component
public class CupboardFileDAO implements CupboardDAO {
    private static final Logger LOG = Logger.getLogger(CupboardFileDAO.class.getName());
    Map<Integer,Need> cupboard;  // Provides a local cache of the need objects
                             // so that we don't need to read from the file
                             // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId; // The next Id to assign to a new need
    private String filename; // Filename to read from and write to

    /**
     * Creates a Cupboard File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object
     * @throws IOException when file cannot be accessed or read from
     */
    public CupboardFileDAO(@Value("${cupboard.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the Cupboard from the file
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree
     * 
     * @return The array of {@link Need needs}, may be empty
     */
    private Need[] getCupboardArray(){
        return getCupboardArray(null);
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map for any 
     * {@linkplain Need needs} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Need needs}
     * 
     * @param containsText the text filter for getting needs
     * 
     * @return The array of {@link Need needs}, may be empty
     */
    private Need[] getCupboardArray(String containsText) { // if containsText == null, no filter; return entire cupboard
        ArrayList<Need> cupboardArrayList = new ArrayList<>();

        for(Need need : cupboard.values()) { // forms arrayList based on matching needs to containsText
            // compares the strings in their lowercase form
            if (containsText == null || need.getName().toLowerCase().contains(containsText.toLowerCase())){
                cupboardArrayList.add(need);
            }
        }

        Need[] cupboardArray = new Need[cupboardArrayList.size()]; // converts and returns Need array of it
        cupboardArrayList.toArray(cupboardArray);
        return cupboardArray;
    }

    /**
     * Saves the {@linkplain Need needs} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Need needs} were written successfully
     * 
     * @throws IOException when the file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Need[] cupboardArray = getCupboardArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will throw an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),cupboardArray);
        // true will not be returned if writeValue throws and exception
        return true;
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the map
     * <br>
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        cupboard = new TreeMap<>();
        nextId = 0;
        File file = new File(filename);
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode cupboardNode = rootNode.get("cupboard");
        if (cupboardNode == null) {
            throw new IOException("The 'cupboard' key is missing in the JSON file.");
        }


        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Need[] cupboardArray = objectMapper.treeToValue(cupboardNode, Need[].class);

        // put each need into the tree map (Integer, Need) and keep track of the greatest id
        for (Need need : cupboardArray){
            cupboard.put(need.getId(),need);
            if (need.getId() > nextId) {
                nextId = need.getId();
            }

        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] getNeeds() {
        synchronized(cupboard) {
            return getCupboardArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] findNeeds(String containsText) {
        synchronized(cupboard) {
            return getCupboardArray(containsText);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need getNeed(int id) {
        synchronized(cupboard) {
            if (cupboard.containsKey(id)){
                return cupboard.get(id); // if need with id exists, return it
            }
            else {
                return null; // otherwise return nothing
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need createNeed(Need need) throws IOException{
        synchronized(cupboard) {
            // We create a new need object because the id field is immutable
            // and we need to assign the next unique id
            if (getNeed(need.getId()) != null) { // if a need with the id already exists
                return null; // return null
            }
            Need newNeed = new Need(nextId(), need.getName(), need.getPrice(), 
            need.getQuantity(), need.getType(), need.getStatus());
            cupboard.put(newNeed.getId(), newNeed);
            save(); // may throw an IOException
            return newNeed;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized(cupboard) {
            if (cupboard.containsKey(need.getId()) == false) {
                return null; // the need does not exist
            }
            cupboard.put(need.getId(),need);
            save(); // may throw IOException
            return need;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
        synchronized(cupboard){
            if (cupboard.containsKey(id)){
                cupboard.remove(id); // if need with id exists, remove it and save
                return save(); 
            }
            return false; // otherwise, return false
        }
    }
}
