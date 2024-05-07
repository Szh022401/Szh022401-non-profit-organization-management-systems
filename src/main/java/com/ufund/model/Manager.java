package com.ufund.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a Manager
 * 
 *
 */
public class Manager extends User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());


    public Manager(@JsonProperty("username") String username) {
        super(username);
    }

    public boolean isValidName() {
        return this.getUsername().equalsIgnoreCase("admin");
    }
}
