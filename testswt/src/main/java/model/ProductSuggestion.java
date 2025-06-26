/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author ASUS
 */

public class ProductSuggestion {
    private int id;
    private String suggestedByBmi;
    private Product product;
    private Timestamp CreatedAt;

    public ProductSuggestion() {
    }

    
    public ProductSuggestion(int id, String suggestedByBmi, Product product, Timestamp CreatedAt) {
        this.id = id;
        this.suggestedByBmi = suggestedByBmi;
        this.product = product;
        this.CreatedAt = CreatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuggestedByBmi() {
        return suggestedByBmi;
    }

    public void setSuggestedByBmi(String suggestedByBmi) {
        this.suggestedByBmi = suggestedByBmi;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp CreatedAt) {
        this.CreatedAt = CreatedAt;
    }
    
    
    
}
