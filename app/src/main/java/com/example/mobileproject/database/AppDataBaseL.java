package com.example.mobileproject.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobileproject.DAO.ExerciseDAO;
import com.example.mobileproject.entities.Exercise;


@Database(entities = {Exercise.class},version = 1 , exportSchema = false)
public abstract class AppDataBaseL extends RoomDatabase {
    private static AppDataBaseL instance;
    public abstract ExerciseDAO ExerciseDAO();

    public static AppDataBaseL getAppDataBaseL(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBaseL.class,
                            "exercise_db")
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }


}