/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author ASUS
 * 
 */
public class Feedback {
    private int id;
    private User user;
    private Product product;
    private String content;
    private Timestamp createdAt;
    private double rate;
    public Feedback(int id, User user, Product product, String content, Timestamp createdAt,double rate) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.content = content;
        this.createdAt = createdAt;
        this.rate = rate;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public void setRate(double rate){
        this.rate = rate;
    }
     public double getRate(){
        return rate;
    }
}
