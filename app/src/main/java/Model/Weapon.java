package Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.content.Context;
import android.content.res.Resources;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import games.bad.taskcrawler.R;

@Entity(tableName = "weapon_table")
public class Weapon extends Item {

    public static String TAG = "WEAPON";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ColumnInfo(name = "description")
    private String description; //the item description.

    public int getXpBoost() {
        return xpBoost;
    }

    public void setXpBoost(int xpBoost) {
        this.xpBoost = xpBoost;
    }

    public int getGoldBoost() {
        return goldBoost;
    }

    public void setGoldBoost(int goldBoost) {
        this.goldBoost = goldBoost;
    }

    @ColumnInfo(name = "xp_boost")
    private int xpBoost; //the item description.

    @ColumnInfo(name = "gold_boost")
    private int goldBoost; //the item description.


    public Weapon(String name, String iconFilename, int cost, int requiredLevel, boolean purchased, String description, int xpBoost, int goldBoost) {
        super(name, iconFilename, cost,requiredLevel,purchased);
        this.setDescription(description);
        this.setXpBoost(xpBoost);
        this.setGoldBoost(goldBoost);
    }

    public static Weapon getWeapon(Context context, int id) {
        return AppDatabase.getAppDatabase(context).weaponDAO().getWeaponById(id);
    }

    public static boolean itemExists(Context context, int id) {
        if(AppDatabase.getAppDatabase(context).weaponDAO().itemExists(id) == 1) {
            return true;
        }
        return false;
    }

    public static void insertWeapon(Context context, Weapon weapon) {
        Log.d(TAG, "INSERTING WEAPON INTO DATABASE");
        AppDatabase.getAppDatabase(context).weaponDAO().insertItem(weapon);
    }

    public void commit(Context context) {
        AppDatabase.getAppDatabase(context).weaponDAO().updateWeapon(this);
    }

    public static List<Weapon> getWeapons(Context context) {
        Log.d(TAG, "RETREVING WEAPON LIST...");
        List<Weapon> weapons = AppDatabase.getAppDatabase(context).weaponDAO().getAllItems();
        return weapons;
    }

    public static List<Weapon> getAllUnpurchasedWeapons(Context context) {
        List<Weapon> weapons = AppDatabase.getAppDatabase(context).weaponDAO().getAllUnpurchasedWeapons();
        return weapons;
    }

    public static List<Weapon> getAllPurchasedWeapons(Context context) {
        List<Weapon> weapons = AppDatabase.getAppDatabase(context).weaponDAO().getAllPurchasedWeapons();
        return weapons;
    }

    public static void nukeTable(Context context) {
        AppDatabase.getAppDatabase(context).weaponDAO().nukeTable();

    }

    //static method to initialize all item data.
    public static void initializeItems(Context context, Resources res) {

        //if there is data in this table already...
        if(Weapon.getWeapons(context).size() != 0) {
            return; //do not initialize it.
        }

        String[] weaponsJson = res.getStringArray(R.array.weapons);

        Log.d(TAG, "INITIALIZING ITEMS:");
        JSONObject item_json;

        //nuke the table to empty the contents.

        for(int i = 0; i < weaponsJson.length; i++) {
            try {
                Log.d(TAG, "CREATED A WEAPON!!!!!");
                item_json = new JSONObject(weaponsJson[i]);

                String weapon_name = item_json.getString("name");
                String weapon_description = item_json.getString("description");
                String weapon_icon_filename = item_json.getString("icon_filename");
                int weapon_cost = item_json.getInt("cost");
                int weapon_required_level = item_json.getInt("required_level");
                int weapon_xp_boost = item_json.getInt("xp_boost");
                int weapon_gold_boost = item_json.getInt("gold_boost");

                boolean weapon_is_purchased = false;

                if(item_json.has("purchased")) {
                    weapon_is_purchased = item_json.getBoolean("purchased");
                }

                Weapon.insertWeapon(context, new Weapon(weapon_name, weapon_icon_filename, weapon_cost, weapon_required_level, weapon_is_purchased, weapon_description, weapon_xp_boost, weapon_gold_boost));

            } catch (JSONException e) { Log.e(TAG, "exception", e);}
        }
    }
}
