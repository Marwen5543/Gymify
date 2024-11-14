package entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan", foreignKeys = {
        @ForeignKey(entity = Food.class,
                parentColumns = "id",
                childColumns = "breakfastFoodId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Food.class,
                parentColumns = "id",
                childColumns = "lunchFoodId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Food.class,
                parentColumns = "id",
                childColumns = "snackFoodId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Food.class,
                parentColumns = "id",
                childColumns = "dinnerFoodId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
})
public class Plan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String dishesSummary;
    private String day;
    private int breakfastFoodId;
    private int lunchFoodId;
    private int snackFoodId;  // Added snackFoodId
    private int dinnerFoodId;
    private int totalCalories;
    private boolean done = false;  // New attribute with default value

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
//    private int userId;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
// Getters and Setters

    public Plan(String day, int breakfastFoodId, int dinnerFoodId, String dishesSummary, int lunchFoodId, int snackFoodId, int totalCalories, boolean done) {
        this.day = day;
        this.breakfastFoodId = breakfastFoodId;
        this.dinnerFoodId = dinnerFoodId;
        this.dishesSummary = dishesSummary;
        this.lunchFoodId = lunchFoodId;
        this.snackFoodId = snackFoodId;
        this.totalCalories = totalCalories;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDishesSummary() {
        return dishesSummary;
    }

    public void setDishesSummary(String dishesSummary) {
        this.dishesSummary = dishesSummary;
    }

    public int getBreakfastFoodId() {
        return breakfastFoodId;
    }

    public void setBreakfastFoodId(int breakfastFoodId) {
        this.breakfastFoodId = breakfastFoodId;
    }

    public int getLunchFoodId() {
        return lunchFoodId;
    }

    public void setLunchFoodId(int lunchFoodId) {
        this.lunchFoodId = lunchFoodId;
    }

    public int getSnackFoodId() {
        return snackFoodId;
    }

    public void setSnackFoodId(int snackFoodId) {
        this.snackFoodId = snackFoodId;
    }

    public int getDinnerFoodId() {
        return dinnerFoodId;
    }

    public void setDinnerFoodId(int dinnerFoodId) {
        this.dinnerFoodId = dinnerFoodId;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    // Method to calculate total calories (optional)
    public void calculateTotalCalories(int breakfastCalories, int lunchCalories, int snackCalories, int dinnerCalories) {
        this.totalCalories = breakfastCalories + lunchCalories + snackCalories + dinnerCalories;
    }
}
