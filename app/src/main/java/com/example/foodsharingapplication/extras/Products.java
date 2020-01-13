package com.example.foodsharingapplication.extras;

public class Products {
    public static final String PAYPAL_CLIENT_ID="AVULyNmcckcTupOFuhbixw6Y9-eLfvfFeIWWA-oWTDoDLmYBywtiJTJLsfEcfhklndSEfIVgMv0DXASr";

    private String productImageName;
    private String productImageUri;
    private String productName;
    private String productDescription;
    private String prodcutRegion;
    private String productLocation;
    private float productPrice;

    public Products() {
        //default constructor
    }

    public String getProductImageName() {
        return productImageName;
    }

    public void setProductImageName(String productImageName) {
        this.productImageName = productImageName;
    }

    public String getProductImageUri() {
        return productImageUri;
    }

    public void setProductImageUri(String productImageUri) {
        this.productImageUri = productImageUri;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProdcutRegion() {
        return prodcutRegion;
    }

    public void setProdcutRegion(String prodcutRegion) {
        this.prodcutRegion = prodcutRegion;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }
}
