package Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "task_table")
public class Task implements Comparable<Task> {

    private static final String TAG = "TASK";


    public long getTimeUntilDueInSeconds() {
        //how long until this is due:
        Calendar myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.YEAR, this.getNextOccurrenceYear());
        myCalendar.set(Calendar.MONTH, this.getNextOccurrenceMonth());
        myCalendar.set(Calendar.DAY_OF_MONTH, this.getNextOccurrenceDay());
        myCalendar.set(Calendar.HOUR_OF_DAY, this.getNextOccurrenceHour());
        myCalendar.set(Calendar.MINUTE, this.getNextOccurrenceMinute());
        myCalendar.set(Calendar.SECOND, 0);

        return (myCalendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public int compareTo(Task other_) {
        return (int)Math.floor(this.getTimeUntilDueInSeconds() - other_.getTimeUntilDueInSeconds());
    }

    public int getLengthHours() {
        return lengthHours;
    }

    public void setLengthHours(int lengthHours) {
        this.lengthHours = lengthHours;
    }

    public int getLengthMinutes() {
        return lengthMinutes;
    }

    public void setLengthMinutes(int lengthMinutes) {
        this.lengthMinutes = lengthMinutes;
    }

    public int getNextOccurrenceYear() {
        return nextOccurrenceYear;
    }

    public void setNextOccurrenceYear(int nextOccurrenceYear) {
        this.nextOccurrenceYear = nextOccurrenceYear;
    }

    public int getNextOccurrenceMonth() {
        return nextOccurrenceMonth;
    }

    public void setNextOccurrenceMonth(int nextOccurrenceMonth) {
        this.nextOccurrenceMonth = nextOccurrenceMonth;
    }

    public int getNextOccurrenceDay() {
        return nextOccurrenceDay;
    }

    public void setNextOccurrenceDay(int nextOccurrenceDay) {
        this.nextOccurrenceDay = nextOccurrenceDay;
    }

    public int getNextOccurrenceHour() {
        return nextOccurrenceHour;
    }

    public void setNextOccurrenceHour(int nextOccurrenceHour) {
        this.nextOccurrenceHour = nextOccurrenceHour;
    }

    public int getNextOccurrenceMinute() {
        return nextOccurrenceMinute;
    }

    public void setNextOccurrenceMinute(int nextOccurrenceMinute) {
        this.nextOccurrenceMinute = nextOccurrenceMinute;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public int getIntervalHour() {
        return intervalHour;
    }

    public void setIntervalHour(int intervalHour) {
        this.intervalHour = intervalHour;
    }

    @ColumnInfo(name = "length_hours")
    private int lengthHours;
    @ColumnInfo(name = "length_minutes")
    private int lengthMinutes;

    @ColumnInfo(name = "next_occurrence_year")
    private int nextOccurrenceYear;

    @ColumnInfo(name = "next_occurrence_month")
    private int nextOccurrenceMonth;

    @ColumnInfo(name = "next_occurrence_day")
    private int nextOccurrenceDay;

    @ColumnInfo(name = "next_occurrence_hour")
    private int nextOccurrenceHour;

    @ColumnInfo(name = "next_occurrence_minute")
    private int nextOccurrenceMinute;

    @ColumnInfo(name = "intervalDays")
    private int intervalDays;

    @ColumnInfo(name = "intervalHours")
    private int intervalHour;

    public long getLastOverdueCheckTime() {
        return lastOverdueCheckTime;
    }

    public void setLastOverdueCheckTime(long lastOverdueCheckTime) {
        this.lastOverdueCheckTime = lastOverdueCheckTime;
    }

    @ColumnInfo(name = "last_overdue_check_time")
    private long lastOverdueCheckTime;


    @ColumnInfo(name = "title")
    private String title; //the title of the task.

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "icon_id")
    private int iconId;

    @ColumnInfo(name = "time_last_completed")
    private long timeLastCompleted; //time last completed. -1 for never. In seconds.

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public long getTimeLastCompleted() {
        return timeLastCompleted;
    }

    public void setTimeLastCompleted(long timeLastCompleted) {
        this.timeLastCompleted = timeLastCompleted;
    }

    public Task(String title,
                int iconId,
                int lengthHours,
                int lengthMinutes,
                int nextOccurrenceYear,
                int nextOccurrenceMonth,
                int nextOccurrenceDay,
                int nextOccurrenceHour,
                int nextOccurrenceMinute,
                int intervalDays,
                int intervalHour) {

        this.setIconId(iconId);
        this.setTitle(title);
        this.setLengthHours(lengthHours);
        this.setLengthMinutes(lengthMinutes);

        this.setNextOccurrenceYear(nextOccurrenceYear);
        this.setNextOccurrenceMonth(nextOccurrenceMonth);
        this.setNextOccurrenceDay(nextOccurrenceDay);
        this.setNextOccurrenceHour(nextOccurrenceHour);
        this.setNextOccurrenceMinute(nextOccurrenceMinute);

        this.setIntervalDays(intervalDays);
        this.setIntervalHour(intervalHour);
        this.setTimeLastCompleted(-1); //-1 because this has never been completed before.
        this.setLastOverdueCheckTime(-1);
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

    public static Task getTaskById(Context context, int id) {
        return AppDatabase.getAppDatabase(context).taskDAO().getTaskById(id);
    }

    public int getIconResourceId(Context c, Resources r) {
        Icon icon = Icon.getIcon(c, this.iconId);
        return r.getIdentifier(icon.getIconFilename(), "drawable", c.getPackageName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLengthAsString(boolean shortMode) {
        return Task.getLengthAsString(this.getLengthHours(), this.getLengthMinutes(), shortMode);
    }

    public static String getLengthAsString(int hours, int minutes, boolean shortMode) {
        StringBuilder sb = new StringBuilder();
        sb.append("Takes ");
        if(hours > 0) {
            if(shortMode) {
                sb.append(String.format("%d hr", hours));
            }else{
                sb.append(String.format("%d hour", hours));
            }
            if(hours != 1) {
                sb.append("s");
            }
        }
        if(hours > 0 && minutes > 0) {
            if(shortMode) {
                sb.append(", ");
            }else{
                sb.append(" and ");

            }
        }

        if(minutes > 0) {
            if(shortMode) {
                sb.append(String.format("%d min", minutes));
            }else{
                sb.append(String.format("%d minute", minutes));
            }
            if(minutes != 1) {
                sb.append("s");
            }
        }

        return sb.toString();
    }

    public String getTimeUntilNextOccurrenceAsString() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    public String getNextOccurrenceAsString() {
        return Task.getNextOccurrenceAsString(this.getNextOccurrenceYear(), this.getNextOccurrenceMonth(), this.getNextOccurrenceDay(), this.getNextOccurrenceHour(), this.getNextOccurrenceMinute());
    }

    public static String getNextOccurrenceAsString(int year, int month, int day, int hour, int minute) {
        StringBuilder sb = new StringBuilder();
        sb.append("Due ");

        Calendar myCalender = Calendar.getInstance();

        //calculate the nice readable "Tues, Jan" from "dp_month = 1 && dp_dayOfMonth = 7"
        myCalender.set(Calendar.YEAR, year);
        myCalender.set(Calendar.MONTH, month);
        myCalender.set(Calendar.DAY_OF_MONTH, day);

        String day_of_week = myCalender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String month_name = myCalender.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        sb.append(String.format("%s %s %d, %d at %02d:%02d", day_of_week, month_name, day, year, hour, minute));
        return sb.toString();
    }

    public String getIntervalAsString(boolean flavourText) {
        return Task.getIntervalAsString(this.getIntervalDays(), this.getIntervalHour(), flavourText);
    }

    public static String getIntervalAsString(int days, int hours, boolean flavourText) {
        if(days == 0 && hours == 0) {
            if(flavourText) {
                return "Never recurs";
            }else{
                return "Never";
            }
        }

        StringBuilder sb = new StringBuilder();
        if(flavourText) {
            sb.append("Occurs every ");
        }

        if(days != 0) {
            sb.append(String.format("%d day", days));
            if(days != 1) {
                sb.append("s");
            }
        }

        if(days != 0 && hours != 0){
            sb.append(" and ");
        }

        if(hours != 0) {
            sb.append(String.format("%d hour", hours));
            if(hours != 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }

    public String getTimeUntilDueAsString() {
        return Task.getTimeUntilDueAsString(this.getTimeUntilDueInSeconds());
    }

    public static String getTimeUntilDueAsString(long due_in_seconds) {
        //This gets weird with negative numbers.
        //Fix it.

        //if its been due for 5 minutes
        if(due_in_seconds <= 0 && due_in_seconds > -(5*60)) {
            return "now";
        }else if(due_in_seconds > 0 && due_in_seconds < 61) {
            return "in a few seconds";
        }

        long due_in_seconds_static = due_in_seconds;
        long due_in_days;
        long due_in_hours;
        long due_in_minutes;

        due_in_days = due_in_seconds / (60 * 60 * 24); //set the value to the output of the division operation, without remainder.
        due_in_seconds = due_in_seconds % (60 * 60 * 24);

        due_in_hours = due_in_seconds / (60 * 60);
        due_in_seconds = due_in_seconds % (60 * 60);

        due_in_minutes = (due_in_seconds / (60));

        StringBuilder sb = new StringBuilder();

        if(due_in_days != 0) {
            sb.append(String.format("%d day", Math.abs(due_in_days)));
            if(due_in_days != 1) {
                sb.append("s");
            }
        }
        String dayString = sb.toString();
        sb.setLength(0);

        if(due_in_hours != 0) {
            sb.append(String.format("%d hour", Math.abs(due_in_hours)));
            if(due_in_hours != 1) {
                sb.append("s");
            }
        }
        String hourString = sb.toString();
        sb.setLength(0);

        if(due_in_minutes != 0) {
            sb.append(String.format("%d minute", Math.abs(due_in_minutes)));
            if(due_in_minutes != 1) {
                sb.append("s");
            }
        }
        String minuteString = sb.toString();
        sb.setLength(0);

        if(due_in_seconds_static > 0) {
            sb.append("in ");
        }

        if(Math.abs(due_in_days) > 3) { //days are greater than three
            sb.append(dayString);
        }else if( due_in_days != 0) { //days are less than or equal to three, but not zero:
            sb.append(dayString);
            sb.append(", ");
            sb.append(hourString);
        }else{ //days are zero
            if(Math.abs(due_in_hours) > 12) {
                sb.append(hourString);
            }else{
                if(Math.abs(due_in_hours) != 0) {
                    sb.append(hourString);
                    sb.append(", ");
                }
                sb.append(minuteString);
            }
        }
        if (due_in_seconds_static < -(5 * 60)) {
            sb.append(" ago");
        }
        return sb.toString();
    }

    public long getIntervalInMillis() {
        return (this.getIntervalDays() * 86400000) + (this.getIntervalHour() * 3600000);
    }

    public long getNextOccurrenceInMillis() {
        Calendar timeDueCalendar = Calendar.getInstance();
        timeDueCalendar.set(Calendar.YEAR, this.getNextOccurrenceYear());
        timeDueCalendar.set(Calendar.MONTH, this.getNextOccurrenceMonth());
        timeDueCalendar.set(Calendar.DAY_OF_MONTH, this.getNextOccurrenceDay());
        timeDueCalendar.set(Calendar.HOUR_OF_DAY, this.getNextOccurrenceHour());
        timeDueCalendar.set(Calendar.MINUTE, this.getNextOccurrenceMinute());
        return timeDueCalendar.getTimeInMillis();
    }

    public static void nukeTable(Context context) {
        AppDatabase.getAppDatabase(context).taskDAO().nukeTable();
    }

    //will tell you how many 5 minute cycles have passed since this task was due :)
    //upon calling this method, it will update state and start counting cycles from the moment this is called.
    public long getOverduePeriods(Context context) {
        long period = 300; //5 minutes in seconds
        if(this.getTimeUntilDueInSeconds() >= 0) { //its not overdue!
            return 0;
        }

        if(this.getLastOverdueCheckTime() == -1) { //the overdue timer has not been set:
            this.setLastOverdueCheckTime(this.getNextOccurrenceInMillis() / 1000); //set it to the time when this thing was due.
        }

        long periodsSinceLastChecked = (long)Math.floor(((System.currentTimeMillis() / 1000) - this.getLastOverdueCheckTime()) / period);

        this.setLastOverdueCheckTime(this.getLastOverdueCheckTime() + (periodsSinceLastChecked * period));
        this.commit(context);

        return periodsSinceLastChecked;
    }

    public long complete(Context context) {

        double xp_multiplier = 1;
        double gold_multiplier = 1;


        int equippedWeaponId = (int)Player.getPlayer().getEquippedWeaponId(context);
        if(equippedWeaponId != -1) {
            xp_multiplier += Weapon.getWeapon(context, equippedWeaponId).getXpBoost() * .01;
            gold_multiplier += Weapon.getWeapon(context, equippedWeaponId).getGoldBoost() * .01;
        }

        long experience = 30; //base reward
        long gold = 10; //base gold.

        //randomize the reward a bit.
        gold += Math.floor(Math.random()*20);
        experience += Math.floor(Math.random()*50);

        //reward for how long it takes.
        experience += 2 * ((getLengthHours() * 60) + getLengthMinutes()); //2 xp for every minute spent on the task.

        //give early bonuses.
        if(getTimeUntilDueInSeconds() > 0) {//if this isn't an overdue task.
            if(getTimeUntilDueInSeconds() >= 7200) { //2 hours early
                experience+= 50;
                gold += 20;
            }else if(getTimeUntilDueInSeconds() >= 28800) { //8 hours early
                experience += 200;
                gold += 50;
            }else if(getTimeUntilDueInSeconds() >= 86400) { //24 hours early
                experience += 400;
                gold += 100;
            }
        }

        experience *= xp_multiplier;
        gold *= gold_multiplier;

        //long experience = Math.min(420, (long)Math.floor(420.f * (((getTimeUntilDueInSeconds() / (60*60)) * ((getLengthHours()*60) + getLengthMinutes())) / 8640.f)));
        Player.getPlayer().addExperience(context, experience);
        Player.getPlayer().addGold(context, gold);

        if(this.getIntervalInMillis() == 0 ) {
            AppDatabase.getAppDatabase(context).taskDAO().deleteTask(this);
        }else {
            this.setTimeLastCompleted(System.currentTimeMillis() / 1000);

            //set the next occurrence
            Calendar nextOccurrenceCalendar = Calendar.getInstance();
            nextOccurrenceCalendar.setTimeInMillis(getNextOccurrenceInMillis() + getIntervalInMillis());

            setNextOccurrenceYear(nextOccurrenceCalendar.get(Calendar.YEAR));
            setNextOccurrenceMonth(nextOccurrenceCalendar.get(Calendar.MONTH));
            setNextOccurrenceDay(nextOccurrenceCalendar.get(Calendar.DAY_OF_MONTH));
            setNextOccurrenceHour(nextOccurrenceCalendar.get(Calendar.HOUR_OF_DAY));
            setNextOccurrenceMinute(nextOccurrenceCalendar.get(Calendar.MINUTE));
            this.setLastOverdueCheckTime(-1);
            this.commit(context);
        }

        return experience;
    }
}
