package com.example.mobileproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileproject.entities.CartItem;
import com.example.mobileproject.entities.CartItem;

import java.util.List;

@Dao
public interface CartItemDAO {
    @Insert
    void addCartItem(CartItem c);

    @Update
    void updateCartItem(CartItem c);

    @Delete
    void deleteCartItem(CartItem c);

    @Query("SELECT * FROM cartitem")
    List<CartItem> getAllCartItems();

    @Query("SELECT * FROM cartitem where id = :id")
    CartItem getOne(int id);

   // @Query("SELECT EXISTS(SELECT 1 FROM cartitem WHERE userId = :userId AND productId = :productId)")
   //boolean isProductInCart(int userId, int productId);
   @Query("SELECT EXISTS(SELECT 1 FROM cartitem WHERE productId = :productId)")
    boolean isProductInCart(int productId);

    @Query("DELETE FROM cartitem WHERE productId = :productId")
    void removeProductFromCart(int productId);

    @Query("SELECT * FROM cartitem WHERE productId = :productId LIMIT 1")
    CartItem getCartItemByProductId(int productId);

}
