//this file is the database handler for our application.
//this class is used like a singleton to return a static instance of a class that handles all dataase interactions through specific class DAOs

package Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database (entities = {Task.class, Weapon.class, Icon.class}, version = 14)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE; //static instance of our database.
    private static final String TAG = "APPDATABASE";

    //we need to create a static instance of every DAO that we want to use in this database.
    public abstract TaskDAO taskDAO();
    public abstract WeaponDAO weaponDAO();
    public abstract IconDAO iconDAO();

    //Create, if necessary, and and return the static database instance.
    //this is singleton hackery, which can only return ONE instance of the object.
    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    //this is the deconstructor.
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
