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
public class ProposedProduct {
    private int id;
    private User nutritionist;
    private String name;
    private String description;
    private String reason;
    private Timestamp createdAt;

    public ProposedProduct() {
    }

    public ProposedProduct(int id, User nutritionist, String name, String description, String reason, Timestamp createdAt) {
        this.id = id;
        this.nutritionist = nutritionist;
        this.name = name;
        this.description = description;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getNutritionist() {
        return nutritionist;
    }

    public void setNutritionist(User nutritionist) {
        this.nutritionist = nutritionist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
    
}
