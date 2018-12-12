package games.bad.taskcrawler;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import Model.Task;

public class NotificationService extends IntentService {

    final static String NOTIFICATION_INFO = "notification_info";

    // Notification stuffs
    private static final int uniqueID = 420111;
    private static final String CHANNEL_ID = "com.games.bad.taskcrawler.notifx";

    public NotificationService() {
        super(NOTIFICATION_INFO);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        boolean i = true;

       while (i) {
           long time_in_seconds = (getDueTimeInMillis(0) - System.currentTimeMillis()) / 1000;

            if (time_in_seconds < 0 && !isNotificationVisible()) {

                notificationMethod("Task Crawler", "Your task is due!", 0); // Notification !!
                i = false;

            } else if (!isNotificationVisible()){
                i = false;
            }
        }
    }

    private boolean isNotificationVisible() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent test = PendingIntent.getActivity(this, uniqueID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }

    // Create the notification, passing in title and content.
    public void notificationMethod(String title, String content, int index) {

        List<Task> tasks = Task.getTasksInOrder(this);

        // Create an intent for a deep-link to SecondActivity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        // Get our PendingIntent by way of the TaskStackBuilder
        PendingIntent contentIntent = TaskStackBuilder.create(getApplicationContext())
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(MainActivity.NOTIFICATION_TAPPED, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_enemy_elf_archer)
                .setTicker(tasks.get(index).getTitle())
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Implement the notification using the notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(uniqueID, notificationBuilder.build());

    }

    public long getDueTimeInMillis(int index) {
        List<Task> tasks = Task.getTasksInOrder(this);

        // How long until a task is due:
        Calendar myCalendar = Calendar.getInstance();

        if(tasks.size() != 0) {
            myCalendar.set(Calendar.YEAR, tasks.get(index).getNextOccurrenceYear());
            myCalendar.set(Calendar.MONTH, tasks.get(index).getNextOccurrenceMonth());
            myCalendar.set(Calendar.DAY_OF_MONTH, tasks.get(index).getNextOccurrenceDay());
            myCalendar.set(Calendar.HOUR_OF_DAY, tasks.get(index).getNextOccurrenceHour());
            myCalendar.set(Calendar.MINUTE, tasks.get(index).getNextOccurrenceMinute());
            myCalendar.set(Calendar.SECOND, index);
        }

        return myCalendar.getTimeInMillis();
    }

}
