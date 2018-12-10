package Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;

//This class will contain all the methods necessary to handling player data.
//things like experience, level, gold, etc will all be here.
public class Player {
    private static final Player ourInstance = new Player();

    public static Player getPlayer() {
        return ourInstance;
    }

    private Player() {
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("com.games.bad.taskcrawler", Context.MODE_PRIVATE);
    }
    private SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return this.getSharedPreferences(context).edit();
    }

    public long getExperience(Context context) {
        return this.getSharedPreferences(context).getLong("experience", 0);
    }
    public long getLevel(Context context) {
        return this.getSharedPreferences(context).getLong("level", 1);
    }
    public long getGold(Context context) {
        return this.getSharedPreferences(context).getLong("gold", 0);
    }

    //calculates how much XP the player needs before they can level up.
    public long getNextLevelExperience(Context context) {
        return (this.getLevel(context) * 500) + (((this.getLevel(context)-1)* this.getLevel(context)) * 50);
    }

    //adds experience, and levels the player up if necessary.
    public long addExperience(Context context, long xp) {
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        long current_experience = this.getExperience(context);
        long new_experience = 0;
        if(current_experience + xp > this.getNextLevelExperience(context)){
            new_experience = (current_experience + xp) - this.getNextLevelExperience(context);
            this.levelUp(context);
        }else{
            new_experience = current_experience + xp;
        }

        if(new_experience < 0) {
            new_experience = 0;
        }

        editor.putLong("experience",new_experience);
        editor.commit();
        return this.getExperience(context);
    }

    public long levelUp(Context context) {
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        editor.putLong("level", this.getLevel(context) + 1);
        editor.commit();
        return this.getLevel(context);
    }

    public long addGold(Context context, long gold){
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        editor.putLong("gold", this.getGold(context) + gold);
        editor.commit();
        return this.getGold(context);
    }

    public void reset(Context context) {
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        editor.putLong("level", 1);
        editor.putLong("experience", 0);
        editor.putLong("gold", 0);
        editor.commit();
    }

    public void setEquippedWeapon(Context context, Weapon weapon) {
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        editor.putLong("weapon", weapon.getId());
        editor.commit();
    }

    public long getEquippedWeaponId(Context context) {
        SharedPreferences preferences = this.getSharedPreferences(context);
        return preferences.getLong("weapon", -1);
    }

    public void setEquippedIcon(Context context, Icon icon) {
        SharedPreferences.Editor editor = this.getSharedPreferencesEditor(context);
        editor.putLong("icon", icon.getId());
        editor.commit();
    }

    public long getEquippedIconId(Context context) {
        SharedPreferences preferences = this.getSharedPreferences(context);
        return preferences.getLong("icon", -1);
    }

}
