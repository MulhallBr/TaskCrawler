package Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IconDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM icon_table WHERE id = :id)")
    int iconExists(int id);

    @Query("SELECT * FROM icon_table WHERE purchased is 1")
    List<Icon> getAllPurchasedIcons();

    @Insert
    void insertIcon(Icon icon);

    @Update
    void updateIcon(Icon icon);

    @Delete
    void deleteIcon(Icon icon);

    @Query("SELECT * FROM icon_table ORDER BY id ASC")
    List<Icon> getAllIcons();

    @Query("DELETE FROM icon_table")
    public void nukeTable();
}
