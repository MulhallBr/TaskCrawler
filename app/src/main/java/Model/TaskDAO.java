//Data Access Object for the Task class.
//used to interface between the Task class, and the AppDatabase db.
package Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    List<Task> getAllTasks();

    @Query("DELETE FROM task_table")
    public void nukeTable();
}
