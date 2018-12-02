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
    void deleteItem(Weapon weapon);

    @Query("SELECT * FROM weapon_table ORDER BY id ASC")
    List<Weapon> getAllItems();

    @Query("DELETE FROM weapon_table")
    public void nukeTable();
}
