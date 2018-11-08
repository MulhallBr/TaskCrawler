//this file is the database handler for our application.
//this class is used like a singleton to return a static instance of a class that handles all dataase interactions through specific class DAOs


package Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database (entities = {Task.class, Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE; //static instance of our database.
    public abstract TaskDAO taskDAO();
    public abstract ItemDAO itemDAO();


    //Create, if necessary, and and return the static database instance.
    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
