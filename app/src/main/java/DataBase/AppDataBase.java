package DataBase;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import DAO.FoodDAO;
import DAO.PlanDAO;
import entities.Food;
import entities.Plan;

@Database(entities = {Food.class, Plan.class}, version = 5, exportSchema = false)  // Update the version number to 4
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    // Migration to handle adding the 'name' column in Food
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE food ADD COLUMN name TEXT");
            database.execSQL("ALTER TABLE food ADD COLUMN week TEXT");
            database.execSQL("ALTER TABLE food ADD COLUMN day TEXT");
            database.execSQL("ALTER TABLE food ADD COLUMN recipe TEXT");
            database.execSQL("ALTER TABLE food ADD COLUMN time TEXT");
            database.execSQL("ALTER TABLE food ADD COLUMN mealType TEXT");
        }
    };

    // Migration to handle the addition of the 'Plan' entity (new table)
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the Plan table
            database.execSQL("CREATE TABLE IF NOT EXISTS `plan` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`day` TEXT," +
                    "`dishesSummary` TEXT," +
                    "`breakfastFoodId` INTEGER NOT NULL," +
                    "`lunchFoodId` INTEGER NOT NULL," +
                    "`dinnerFoodId` INTEGER NOT NULL," +
                    "`snackFoodId` INTEGER," +  // Add snackFoodId column for snacks
                    "`totalCalories` INTEGER NOT NULL," +
                    "FOREIGN KEY(`breakfastFoodId`) REFERENCES `food`(`id`) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY(`lunchFoodId`) REFERENCES `food`(`id`) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY(`dinnerFoodId`) REFERENCES `food`(`id`) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY(`snackFoodId`) REFERENCES `food`(`id`) ON DELETE CASCADE ON UPDATE CASCADE)");
        }
    };

    // Migration to add the 'done' attribute to the 'plan' table
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'plan' ADD COLUMN done INTEGER NOT NULL DEFAULT 0");  // 0 represents false

        }
    };
    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'food' ADD COLUMN done INTEGER NOT NULL DEFAULT 0");  // 0 represents false

        }
    };
    public abstract FoodDAO foodDAO();
    public abstract PlanDAO planDAO();  // Add PlanDAO to access Plan entity

    public static AppDataBase getAppDataBase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "room_test_db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4 ,MIGRATION_4_5 ) // Add new migration
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
