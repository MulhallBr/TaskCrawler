package Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ItemDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM item_table WHERE id = :id)")
    int itemExists(int id);

    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * FROM item_table ORDER BY id ASC")
    List<Item> getAllItems();

    @Query("DELETE FROM item_table")
    public void nukeTable();
}
