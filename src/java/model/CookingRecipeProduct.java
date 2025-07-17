/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP
 */
public class CookingRecipeProduct {
    private CookingRecipe cookingRecipe;
    private Product product;

    public CookingRecipeProduct() {
    }

    public CookingRecipeProduct(CookingRecipe cookingRecipe, Product product) {
        this.cookingRecipe = cookingRecipe;
        this.product = product;
    }

    public CookingRecipe getCookingRecipe() {
        return cookingRecipe;
    }

    public void setCookingRecipe(CookingRecipe cookingRecipe) {
        this.cookingRecipe = cookingRecipe;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    
}
