package com.ufund.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Need entity
 * 
 *
 */
public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // String format for the string representation of Need
    static final String STRING_FORMAT = "Need [id=%d, name=%s, price=%.2f, quantity=%d, type=%s, status=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private Double price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    @JsonProperty("status") private String status;

    /**
     * Create a need with the given id, name, price, quantity, type, and status
     * @param id The id of the need
     * @param name The name of the need
     * @param price The price of the need
     * @param quantity The quantity of the need, i.e., how much is left to support
     * @param type The type of the need
     * @param status The current status of the need
     * 
     *{@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") Double price, 
                @JsonProperty("quantity") int quantity, @JsonProperty("type") String type, @JsonProperty("status") String status){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }

    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {return id;}

    /**
     * Retrieves the name of the Need
     * @return The name of the Need
     */
    public String getName() {return name;}

    /**
     * Sets the name of the need - used for JSON object to Java conversions
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the price of the Need
     * @return The price of the Need
     */
    public Double getPrice() {return price;} 
    /**
     * Sets the price of the need - used for JSON object to Java conversions
     * @param price The price of the Need - String representation
     */
    public void setPrice(String price) {this.price = Double.parseDouble(price);} // converts String to Double, and stores

    /**
     * Retrieves the quantity of the Need
     * @return The quantity of the Need
     */
    public int getQuantity() {return quantity;}

    /**
     * Sets the quantity of the Need - used for JSON object to Java conversions
     * @param quantity The quantity of the Need - String representation
     */
    public void setQuantity(String quantity) {this.quantity = Integer.parseInt(quantity);} // converts String to int, and stores

    /**ufund-api/src/main/java/com/ufund/api/ufundapi/model/Need.java
     * Retrieves the type of the Need
     * @return The type of the Need
     */
    public String getType() {return type;}

    /**
     * Sets the type of the Need - used for JSON object to Java conversions
     * @param type The type of the need
     */
    public void setType(String type) {this.type = type;}

    /**
     * Retrieves the status of the Need
     * @return The status of the Need
     */
    public String getStatus() {return status;}

    /**
     * Sets the status of the Need - used for JSON object to Java conversions
     * @param status The status of the Need
     */
    public void setStatus(String status) {this.status = status;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantity,type,status);
    }



}
