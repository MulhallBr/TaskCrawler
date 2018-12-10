package Model;

import android.app.Activity;
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

//this is an abstract class that is implemented by Weapon and Icon.
//

public class Item {
    private static final String TAG = "ITEM";

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconFilename() { return iconFilename; }

    public void setIconFilename(String iconFilename) { this.iconFilename = iconFilename; }

    public int getCost() { return cost; }

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

    public int getIconResourceId(Activity activity) {
        return activity.getResources().getIdentifier(this.getIconFilename(), "drawable", activity.getPackageName());
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name; //the name of the item.

    @ColumnInfo(name = "icon_file_name")
    private String iconFilename;

    @ColumnInfo(name = "cost")
    private int cost; //the cost of this item, in gold.

    @ColumnInfo(name = "purchased")
    private boolean purchased;

    @ColumnInfo(name = "required_level")
    private int requiredLevel; //the level required to purchase this item.

    public Item(String name, String iconFilename, int cost, int requiredLevel, boolean purchased) {
        this.setName(name);
        this.setIconFilename(iconFilename);
        this.setCost(cost);
        this.setRequiredLevel(requiredLevel);
        this.setPurchased(purchased);
    }
}
