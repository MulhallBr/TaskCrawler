package Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import java.util.List;

@Entity(tableName = "task_table")
public class Task {

    private static final String TAG = "TASK";

    @ColumnInfo(name = "title")
    private String title; //the title of the task.

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "time_to_complete")
    private int timeToComplete;

    @ColumnInfo(name = "icon_id")
    private int iconId;

    @ColumnInfo(name = "interval")
    private int interval; //the time, in hours, between this task recurring. 0 if it never recurs.

    @ColumnInfo(name = "time_last_completed")
    private int timeLastCompleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getTimeLastCompleted() {
        return timeLastCompleted;
    }

    public void setTimeLastCompleted(int timeLastCompleted) {
        this.timeLastCompleted = timeLastCompleted;
    }

    public Task(String title, int timeToComplete, int iconId, int interval, int timeLastCompleted) {
        this.title = title;
        this.timeToComplete = timeToComplete;
        this.iconId = iconId;
        this.interval = interval;
        this.timeLastCompleted = timeLastCompleted;
    }

    public void commit(Context context) {
        AppDatabase.getAppDatabase(context).taskDAO().updateTask(this);
    }

    public static Task createTask(Context context, Task task) {
        //insert a task to the database.
        AppDatabase.getAppDatabase(context).taskDAO().insertTask(task);
        return task;
    }

    public static List<Task> getTasksInOrder(Context context) {
        //go into the database and retrieve every task, order them by date, pushing those that have been finished recently to the bottom.
        List<Task> tasks = AppDatabase.getAppDatabase(context).taskDAO().getAllTasks();
        return tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
