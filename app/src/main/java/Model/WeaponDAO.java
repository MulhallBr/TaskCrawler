package Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WeaponDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM weapon_table WHERE id = :id)")
    int itemExists(int id);

    @Insert
    void insertItem(Weapon weapon);

    @Update
    void updateWeapon(Weapon weapon);

    @Delete
    void deleteWeapon(Weapon weapon);

    @Query("SELECT * FROM weapon_table ORDER BY id ASC")
    List<Weapon> getAllItems();

    @Query("SELECT * FROM weapon_table WHERE purchased = 0 ORDER BY required_level ASC, id")
    List<Weapon> getAllUnpurchasedWeapons();

    @Query("SELECT * FROM weapon_table WHERE purchased = 1 ORDER BY required_level ASC, id")
    List<Weapon> getAllPurchasedWeapons();

    @Query("SELECT * FROM weapon_table WHERE id = :id")
    Weapon getWeaponById(int id);

    @Query("DELETE FROM weapon_table")
    public void nukeTable();
}
