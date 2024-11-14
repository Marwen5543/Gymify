package com.example.mobileproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mobileproject.DAO.CartItemDAO;
import com.example.mobileproject.DAO.ProductDAO;
import com.example.mobileproject.entities.CartItem;
import com.example.mobileproject.entities.Converters;
import com.example.mobileproject.entities.Product;

@Database(entities = {Product.class, CartItem.class},version = 2 , exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDataBaseS extends RoomDatabase {
    private static AppDataBaseS instance;

    public abstract ProductDAO ProductDAO();
    public abstract CartItemDAO CartItemDAO();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            // Rename the original Product table
            database.execSQL("ALTER TABLE Product RENAME TO Product_old");

            // Create the new Product table with the updated price type
            database.execSQL("CREATE TABLE Product (id INTEGER PRIMARY KEY NOT NULL, title TEXT, price TEXT)");

            // Copy data from the old table to the new table
            database.execSQL("INSERT INTO Product (id, title, price) SELECT id, title, price FROM Product_old");

            // Drop the old table
            database.execSQL("DROP TABLE Product_old");
        }

    };

    public static AppDataBaseS getAppDataBaseS(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBaseS.class,
                            "product_db")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }
}