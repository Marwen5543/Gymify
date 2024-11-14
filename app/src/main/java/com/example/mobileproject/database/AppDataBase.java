package com.example.mobileproject.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobileproject.DAO.UserDAO;
import com.example.mobileproject.entities.User;


@Database(entities = {User.class},version = 1 , exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;
    public abstract UserDAO UserDAO();

    public static AppDataBase getAppDataBase(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "user_db")
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }


}
