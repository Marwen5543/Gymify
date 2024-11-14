package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "food")
public class Food implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String week;
    private String name;
    private String day;
    private String recipe;
    private int calories;
    private String time;
    private String mealType;
    private boolean done = false;  // New attribute with default value

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Food(String week, String day, String recipe, int calories, String time, String name, String mealType,boolean done) {
        this.day=day;
        this.name=name;
        this.recipe=recipe;
        this.time=time;
        this.calories= calories;
        this.mealType=mealType;
        this.week=week;
        this.done=done;

    }

    public Food() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }



    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

