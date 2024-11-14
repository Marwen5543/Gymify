package com.example.mobileproject.DAO;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileproject.entities.Category;
import com.example.mobileproject.entities.Product;

import java.util.List;


@Dao
public interface ProductDAO {
    @Insert
    void addProduct(Product p);

    @Update
    void updateProduct(Product p);

    @Delete
    void deleteProduct(Product p);

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();

    @Query("SELECT * FROM product where id = :id")
    Product getOne(int id);

    @Query("SELECT * FROM product where category = :categoryvalue")
    List<Product> getByCategory(Category categoryvalue);

    //@Query("SELECT * FROM product where inCart = TRUE")
    //List<Product> getInCart();

    //@Query("UPDATE product SET inCart = 0 WHERE id = :id") // Use 0 for false in SQLite
    //void removeFromCart(int id);
}

