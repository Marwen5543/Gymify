package com.example.mobileproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "cartitem")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private  int userId;
    private int productId;

    // Constructor without userId, since it's fixed


    public CartItem(int userId, int productId ) {
        this.productId = productId;
        this.userId = userId;
    }

    // Getters and setters (no setter for userId)
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
