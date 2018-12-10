package Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import games.bad.taskcrawler.R;

@Entity(tableName = "icon_table")
public class Icon extends Item {
    public static String TAG = "ICON";

    @ColumnInfo(name = "icon_type")
    private int iconType;

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public int getIconType() {
        return iconType;
    }


    public Icon(String name, String iconFilename, int cost, int requiredLevel, boolean purchased, int iconType) {
        super(name, iconFilename, cost,requiredLevel,purchased);
        this.setIconType(iconType);
    }

    public static boolean iconExists(Context context, int id) {
        if(AppDatabase.getAppDatabase(context).iconDAO().iconExists(id) == 1) {
            return true;
        }
        return false;
    }

    public static void insertIcon(Context context, Icon icon) {
        Log.d(TAG, "INSERTING ICON INTO DATABASE");
        AppDatabase.getAppDatabase(context).iconDAO().insertIcon(icon);
    }

    public void commit(Context context) {
        AppDatabase.getAppDatabase(context).iconDAO().updateIcon(this);
    }

    public static Icon getIcon(Context context, int id) {
        return AppDatabase.getAppDatabase(context).iconDAO().getIcon(id);
    }

    public static List<Icon> getIcons(Context context) {
        Log.d(TAG, "RETREVING ICON LIST...");
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllIcons();
        return icons;
    }

    public static List<Icon> getAllPurchasedIcons(Context context) {
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllPurchasedIcons();
        return icons;
    }

    public static List<Icon> getAllPurchasedPlayerIcons(Context context) {
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllPurchasedPlayerIcons();
        return icons;
    }
    public static List<Icon> getAllUnpurchasedPlayerIcons(Context context) {
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllUnpurchasedPlayerIcons();
        return icons;
    }
    public static List<Icon> getAllPurchasedEnemyIcons(Context context) {
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllPurchasedEnemyIcons();
        return icons;
    }
    public static List<Icon> getAllUnpurchasedEnemyIcons(Context context) {
        List<Icon> icons = AppDatabase.getAppDatabase(context).iconDAO().getAllUnpurchasedEnemyIcons();
        return icons;
    }

    //static method to initialize all item data.
    public static void initializeItems(Context context, Resources res) {

        //if there is data in this table already...
        if(Icon.getIcons(context).size() != 0) {
            return; //do not initialize it.
        }

        String[] iconJSON = res.getStringArray(R.array.icons);

        Log.d(TAG, "INITIALIZING ITEMS:");
        JSONObject item_json;

        for(int i = 0; i < iconJSON.length; i++) {
            try {
                Log.d(TAG, "CREATED AN ICON!!!!!");
                item_json = new JSONObject(iconJSON[i]);

                String icon_name = item_json.getString("name");
                String icon_icon_filename = item_json.getString("icon_filename");
                int icon_type = item_json.getInt("icon_type");
                int icon_cost = item_json.getInt("cost");
                int icon_required_level = item_json.getInt("required_level");

                boolean icon_is_purchased = false;

                if(item_json.has("purchased")) {
                    icon_is_purchased = item_json.getBoolean("purchased");
                }

                Icon.insertIcon(context, new Icon(icon_name, icon_icon_filename, icon_cost, icon_required_level, icon_is_purchased, icon_type));

            } catch (JSONException e) { Log.e(TAG, "exception", e);}
        }
    }
}
