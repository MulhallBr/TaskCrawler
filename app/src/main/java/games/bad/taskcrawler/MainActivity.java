package games.bad.taskcrawler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;

import android.os.Handler;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Interfaces.TaskTapCallback;
import Model.AppDatabase;
import Model.Icon;
import Model.Task;
import Adapters.TaskListAdapter;
import Model.Weapon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskTapCallback {

    // Items needed for a basic notification
    NotificationCompat.Builder notification;
    private static final int uniqueID = 420111;

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private RecyclerView taskListRecyclerView;
    private TaskListAdapter taskListAdapter;

    private Handler taskListUpdateHandler;
    private Runnable taskListUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Basic notification
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true); // Kills the notification when the activity is created

        Weapon.initializeItems(this, this.getResources()); //init the database, if it is not already.
        Icon.initializeItems(this, this.getResources());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<Task> tasks = Task.getTasksInOrder(this);
        taskListRecyclerView = findViewById(R.id.task_list_recycler_view);
        taskListAdapter = new TaskListAdapter(tasks, this);
        taskListRecyclerView.setAdapter(taskListAdapter);
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //tell the task list adapter that this method is accepting callbacks.
        taskListAdapter.setTapTaskCallback(this);

        taskListUpdateHandler = new Handler();

        taskListUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateTaskList();
                taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 15000); //15000
            }
        };
        taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 15000);
    }

    //the task tap callback callback method
    @Override
    public void onTaskTapped(Task task){
        //show the dialog
        TaskPromptDialog tpd = new TaskPromptDialog(this, task);
        tpd.show();
    }


    public void updateTaskList() {
        this.taskListAdapter.updateList(Task.getTasksInOrder(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        taskListAdapter.updateList(Task.getTasksInOrder(this));
        //check the "Tasks" item when this activity resumes.
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.getMenu().getItem(0).setChecked(true);
    }

    //we override this method so that if the NavDrawer is open, it doesn't close the app.
    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //gets called when the user taps an option in the Navigation Drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");

        int id = item.getItemId(); //the ID of the option the user clicked...

        if (id == R.id.nav_tasks) {
            //do nothing.
        } else if (id == R.id.nav_shop) {
            Intent myIntent = new Intent(MainActivity.this, ShopActivity.class);
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_new_task) {

            Intent myIntent = new Intent(MainActivity.this, NewTaskActivity.class);
            myIntent.putExtra("mode", "NEW");
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_inventory) {
            Intent myIntent = new Intent(MainActivity.this, InventoryActivity.class);
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This is just the workings of a basic notification
    // This needs to be refactored when we actually have the mechanics down,
    // so we can send a notification whenever a task reaches a certain time left until Due.
    public void notificationMethod() {

        // Build the notification
        notification.setSmallIcon(R.drawable.icon_enemy_elf_archer);
        notification.setTicker("Your task is due!");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Task Crawler");
        notification.setContentText("Your task is due!");

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        // Builds the notification and issues it (sending it out)
        NotificationManager notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notManager.notify(uniqueID, notification.build());

    }
}