package DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import entities.Food;

@Dao
public interface FoodDAO {
    @Insert
    long insertFood(Food food);

    @Query("SELECT * FROM food WHERE day = :day")
    List<Food> getFoodsByDay(String day);

    @Query("SELECT * FROM food")
    List<Food> getAllFoods();

    @Query("UPDATE food SET done = :doneStatus WHERE id = :foodId")
    void markAsDone(int foodId, boolean doneStatus);

    @Query("SELECT done FROM food WHERE id = :foodId")
    boolean getMealStatus(int foodId);

}

