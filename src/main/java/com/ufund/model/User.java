package com.ufund.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User
 * 
 *
 */
public  class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    // package private for tests
    static final String STRING_FORMAT = "User [username=%s, manager=%b]";

    @JsonProperty("username") private String username;
    @JsonProperty("id") private int id;
    @JsonProperty("manager") private boolean manager = false;  // manager's default is false
    
    
    /**
     * Create a user with the given name
     * @param username The name of the user
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields. If a field 
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(@JsonProperty("username") String username) {

        this.username = username; // username is immutable after it is set by the constructor
        if ("admin".equalsIgnoreCase(username)) { // if the user is logging in as admin
            this.manager = true;                 // they are a manager
        }

    }

    /**
     * Retrieves the username of the User
     * @return The username of the User
     */
    public String getUsername() {return username;}

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    /**
     * Retrieves the manager status of the User
     * @return The manager status of the User
     */
    public boolean isManager() {return manager;}



    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, manager);
    }
}
