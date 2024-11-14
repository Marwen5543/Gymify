package com.example.mobileproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileproject.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Insert
    void addExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("SELECT * FROM exercise")
    List<Exercise> getAllExercises();

    @Query("SELECT * FROM exercise WHERE id = :id")
    Exercise getExerciseById(int id);
}