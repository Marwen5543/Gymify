package com.example.mobileproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileproject.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void addUSer(User u);
    @Update
    void updateUSer(User u);
    @Delete
    void deleteUSer(User u);
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT * FROM user where id = :id")
    User getOne(int id);
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    User login(String email, String password);
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User findUserByEmail(String email);
}
