/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imgUrl;
    private Category category;
    private double shelfLifeHours;
    private double rate;

    public Product() {
    }

    public Product(int id, String name, String description, double price, int stock, String imgUrl, Category category, double shelfLifeHours, double rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imgUrl = imgUrl;
        this.category = category;
        this.shelfLifeHours = shelfLifeHours;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getShelfLifeHours() {
        return shelfLifeHours;
    }

    public void setShelfLifeHours(double shelfLifeHours) {
        this.shelfLifeHours = shelfLifeHours;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
     public double getRate() {
        return rate;
    }
}
