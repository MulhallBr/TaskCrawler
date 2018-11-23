package Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import games.bad.taskcrawler.R;

@Entity(tableName = "item_table")
public class Item {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

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

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "name")
    private String name; //the name of the item.

    @ColumnInfo(name = "description")
    private String description; //the item description.

    @ColumnInfo(name = "icon_id")
    private int iconId;

    @ColumnInfo(name = "cost")
    private int cost; //the cost of this item, in gold.

    @ColumnInfo(name = "purchased")
    private boolean purchased;

    @ColumnInfo(name = "required_level")
    private int requiredLevel; //the level required to purchase this item.

    @ColumnInfo(name = "item_type")
    private int itemType; //0 = Weapon, 1 = consumable, 2 = icon.

    @ColumnInfo(name = "xp_boost")
    private int xpBoost; //the percentage of xp this item boosts

    @ColumnInfo(name = "gold_boost")
    private int goldBoost; //the percentage of gold this item boosts

    public Item(int id, String name, String description, int iconId, int cost, int requiredLevel, int itemType, int xpBoost, int goldBoost) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setIconId(iconId);
        this.setCost(cost);
        this.setRequiredLevel(requiredLevel);
        this.setItemType(itemType);
        this.setPurchased(false);
        this.setXpBoost(xpBoost);
        this.setGoldBoost(goldBoost);
    }

    public static boolean itemExists(Context context, int id) {
        if(AppDatabase.getAppDatabase(context).itemDAO().itemExists(id) == 1) {
            return true;
        }
        return false;
    }

    public static void insertItem(Context context, Item item) {
        AppDatabase.getAppDatabase(context).itemDAO().insertItem(item);
    }

    public static List<Item> getItems(Context context) {
        List<Item> items = AppDatabase.getAppDatabase(context).itemDAO().getAllItems();
        return items;
    }

    public static void initializeItems(Context context, Resources res) {
        //read the item data from an XML source
        //insert that data into the sql database if it does not already exist.
        //if it does, do nothing.

        String[] weaponsJson = res.getStringArray(R.array.weapons);

        Log.d("ITEMS", "INITIALIZING ITEMS:");
        JSONObject item_json;

        for(int i = 0; i < weaponsJson.length; i++) {
            try {
                item_json = new JSONObject(weaponsJson[i]);
                Log.d("DEATH","ABOUT TO CREATE AN ITEM:");
                if(!Item.itemExists(context, item_json.getInt("id")))  {
                    Log.d("DEATH","CREATING THE ITEM!!!!!!!!!!!!!!!!!:");
                    Item.insertItem(context, new Item(  item_json.getInt("id"),
                                                        item_json.getString("name"),
                                                        item_json.getString("description"),
                                                        item_json.getInt("icon_id"),
                                                        item_json.getInt("cost"),
                                                        item_json.getInt("required_level"),
                                                        item_json.getInt("item_type"),
                                                        item_json.getInt("xp_boost"),
                                                        item_json.getInt("gold_boost")));
                }
            } catch (JSONException e) { Log.e("JSON_ERROR", "exception", e);}
        }
    }
}
