package DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Transaction;

import java.util.List;

import entities.Plan;
import entities.PlanWithMeals;

@Dao
public interface PlanDAO {

    // Insert a new plan
    @Insert
    void insertPlan(Plan plan);

    // Update an existing plan
    @Update
    void updatePlan(Plan plan);

    // Delete a plan
    @Delete
    void deletePlan(Plan plan);

    // Get all plans
    @Query("SELECT * FROM 'plan'")
    List<Plan> getAllPlans();

    // Get a plan by day
    @Query("SELECT * FROM 'plan' WHERE day = :day")
    Plan getPlanByDay(String day);

    // Get total calories for a specific plan by summing food calories
//    @Transaction
//    @Query("SELECT ( " +
//            "(SELECT calories FROM food WHERE id = plan.breakfastFoodId) + " +
//            "(SELECT calories FROM food WHERE id = plan.lunchFoodId) + " +
//            "(SELECT calories FROM food WHERE id = plan.snackFoodId) + " +
//            "(SELECT calories FROM food WHERE id = plan.dinnerFoodId) " +
//            ") as totalCalories FROM 'plan' WHERE id = :planId")
//    int calculateTotalCalories(int planId);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE id = :id")
    PlanWithMeals getPlanWithMealsById(int id);

    // Get all plans with full meal details (breakfast, lunch, snack, dinner)
    @Transaction
    @Query("SELECT * FROM 'plan'")
    List<PlanWithMeals> getPlansWithMeals();

    // Get a specific plan by day with full meal details
    @Transaction
    @Query("SELECT * FROM 'plan' WHERE day = :day")
    PlanWithMeals getPlanWithMealsByDay(String day);

}
