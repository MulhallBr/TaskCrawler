package games.bad.taskcrawler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.os.Handler;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.util.Log;
import java.util.List;

import Adapters.TaskListAdapter;
import Interfaces.TaskTapCallback;
import Model.Icon;
import Model.Task;
import Model.Weapon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskTapCallback {

    private static final String TAG = "MainActivity";

    // Constants needed for a basic notification
    private static final int uniqueID = 420111;
    private static final String CHANNEL_ID = "com.games.bad.taskcrawler.notifx";

    private DrawerLayout drawer;

    private TextView noTaskTextView;
    private TextView playerInfoText;

    // Initialize the Adapter, Handler and Runnable for the RecyclerView
    private TaskListAdapter taskListAdapter;
    private Handler taskListUpdateHandler;
    private Runnable taskListUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "OnCreate"); // Logcat print item for debugging

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noTaskTextView = findViewById(R.id.noTaskTextView);
        playerInfoText = findViewById(R.id.playerInfoText);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Weapon.initializeItems(this, this.getResources());  // Initialize the database,
        Icon.initializeItems(this, this.getResources());    // if it hasn't been done already.

        // Toggles the name of the action bar based on whether or not the  navigation drawer is open.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<Task> tasks = Task.getTasksInOrder(this);  // Place all of the Task objects into a list.

        if (tasks.size() == 0) {
            // If there are no tasks, show the text view prompting the user to add new tasks.
            noTaskTextView.setVisibility(View.VISIBLE);
        } else {
            // If there are tasks, make sure that prompt is not visible.
            noTaskTextView.setVisibility(View.GONE);
        }

        // Set up the RecyclerView and Adapter
        RecyclerView taskListRecyclerView = findViewById(R.id.task_list_recycler_view);
        taskListAdapter = new TaskListAdapter(tasks, this);
        taskListRecyclerView.setAdapter(taskListAdapter);
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tell the task list adapter that this method is accepting callbacks.
        taskListAdapter.setTapTaskCallback(this);

        taskListUpdateHandler = new Handler();  // Set the handler

        taskListUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateTaskList();
                taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 15000); // 15000
            }
        };

        taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 15000);

        createNotificationChannel();                                                    // NOTIFICATION CHANNEL
        notificationMethod("YOUR THING IS DUE", "GET IT DONE YOU DUMMY!"); // NOTIFICATION

        updatePlayerDataView();
    }

    // The task tap callback method.
    @Override
    public void onTaskTapped(Task task){

        // Pop up the Dialog view on top of the MainActivity.
        TaskPromptDialog tpd = new TaskPromptDialog(this, task);

        tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updatePlayerDataView();
            }
        });

        tpd.show();
    }

    // Method in order to update the Player's different stats, displayed at the top of the screen.
    public void updatePlayerDataView() {

        // Updates the views that display the player's stats.
        SharedPreferences preferences = this.getSharedPreferences("com.games.bad.taskcrawler", Context.MODE_PRIVATE);

        // Each of the players (initial?) stats.
        long level = preferences.getLong("level", 1);           // PLAYER LEVEL
        long experience = preferences.getLong("experience", 0); // PLAYER EXP
        long gold = preferences.getLong("gold", 15);            // PLAYER GOLD

        playerInfoText.setText(String.format("Level: %d, Exp: %d, Gold, %d", level, experience, gold));
    }

    // Method in order to update the task list, whenever editing, deleting or adding a task.
    public void updateTaskList() {
        // Place all of the Task objects into a list.
        List<Task> tasks = Task.getTasksInOrder(this);

        if (tasks.size() == 0) {
            // If there are no tasks, show the text view prompting the user to add new tasks.
            noTaskTextView.setVisibility(View.VISIBLE);
        } else {
            // If there are tasks, make sure that prompt is not visible.
            noTaskTextView.setVisibility(View.GONE);
        }

        this.taskListAdapter.updateList(tasks);
    }

    // We override this method in order to make certain updates.
    @Override
    public void onResume() {
        super.onResume();

        taskListAdapter.updateList(Task.getTasksInOrder(this));

        // Check the "Tasks" item when the MainActivity resumes.
        NavigationView nv = findViewById(R.id.nav_view);
        nv.getMenu().getItem(0).setChecked(true);

        // Update the task list and the player's stats.
        updateTaskList();
        updatePlayerDataView();
    }

    // We override this method so that if the NavDrawer is open,
    // it won't close the app if the back button is pressed.
    @Override
    public void onBackPressed() {

        Log.d(TAG, "OnBackPressed"); // Logcat print item for debugging

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    // Gets called when the user taps an option in the Navigation Drawer.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");

        int id = item.getItemId(); // The ID of the option the user has tapped...

        if (id == R.id.nav_tasks) {

            // Do nothing.

        } else if (id == R.id.nav_shop) {

            // Create an intent to launch the SHOP Activity
            Intent myIntent = new Intent(MainActivity.this, ShopActivity.class);
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_new_task) {

            // Create an intent to launch the NEW TASK Activity
            Intent myIntent = new Intent(MainActivity.this, NewTaskActivity.class);
            myIntent.putExtra("mode", "NEW");
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_inventory) {

            // Create an intent to launch the INVENTORY Activity
            Intent myIntent = new Intent(MainActivity.this, InventoryActivity.class);
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_settings) {

            // Create an intent to launch the SETTINGS Activity
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Create a notification channel on API 26+, because it needs it to run on those systems.
    private void createNotificationChannel() {

        // If statement to check the API is greater or equal to Oreo
        // If true, then create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Channel name and description variables
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            // Creating the Channel itself
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            // Implement the Channel using the notification manager
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    // Create the notification, passing in title and content.
    public void notificationMethod(String title, String content) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        // Set all of the attributes of the notification
        notificationBuilder.setSmallIcon(R.drawable.icon_enemy_elf_archer);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(content);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        // Implement the notification using the notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(uniqueID, notificationBuilder.build());

    }
}