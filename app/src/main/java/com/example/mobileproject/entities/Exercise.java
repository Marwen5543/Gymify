package com.example.mobileproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String photo; // URL or path to the photo

    @ColumnInfo
    private String name; // Name of the exercise

    @ColumnInfo
    private int duration; // Duration of the exercise in minutes

    @ColumnInfo
    private Points points; // Points for the exercise (3, 5, or 10)

    @ColumnInfo
    private String description; // Description of the exercise

    public Exercise() {
    }

    public Exercise(String photo, String name, int duration, Points points, String description) {
        this.photo = photo;
        this.name = name;
        this.duration = duration;
        this.points = points;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", points=" + points +
                ", description='" + description + '\'' +
                '}';
    }

    // Enum for points
    public enum Points {
        THREE(3),
        FIVE(5),
        TEN(10);

        private final int value;

        Points(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}