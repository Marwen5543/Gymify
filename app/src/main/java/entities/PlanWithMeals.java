package entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class PlanWithMeals {

    @Embedded
    public Plan plan;  // Embedded Plan object

    // Relations for each meal type
    @Relation(
            parentColumn = "breakfastFoodId",
            entityColumn = "id"
    )
    public Food breakfastFood;

    @Relation(
            parentColumn = "lunchFoodId",
            entityColumn = "id"
    )
    public Food lunchFood;

    @Relation(
            parentColumn = "snackFoodId",
            entityColumn = "id"
    )
    public Food snackFood;

    @Relation(
            parentColumn = "dinnerFoodId",
            entityColumn = "id"
    )
    public Food dinnerFood;

    // Method to calculate total calories
    public int calculateTotalCalories() {
        int totalCalories = 0;
        if (breakfastFood != null) {
            totalCalories += breakfastFood.getCalories();
        }
        if (lunchFood != null) {
            totalCalories += lunchFood.getCalories();
        }
        if (snackFood != null) {
            totalCalories += snackFood.getCalories();
        }
        if (dinnerFood != null) {
            totalCalories += dinnerFood.getCalories();
        }
        return totalCalories;
    }
}
