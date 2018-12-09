package games.bad.taskcrawler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;

import android.os.Handler;
import android.support.design.widget.Snackbar;

import android.support.v4.app.NotificationManagerCompat;
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
import android.view.View;
import android.widget.TextView;
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
    private static final int uniqueID = 420111;
    private static final String CHANNEL_ID = "com.games.bad.taskcrawler.notifx";

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private RecyclerView taskListRecyclerView;
    private TextView noTaskTextView;
    private TaskListAdapter taskListAdapter;

    private Handler taskListUpdateHandler;
    private Runnable taskListUpdateRunnable;

    private TextView playerInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noTaskTextView = findViewById(R.id.noTaskTextView);
        playerInfoText = findViewById(R.id.playerInfoText);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Weapon.initializeItems(this, this.getResources()); //init the database, if it is not already.
        Icon.initializeItems(this, this.getResources());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<Task> tasks = Task.getTasksInOrder(this);
        if(tasks.size() == 0) {
            noTaskTextView.setVisibility(View.VISIBLE);
        }else{
            noTaskTextView.setVisibility(View.GONE);
        }
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
        createNotificationChannel();
        notificationMethod("YOUR THING IS DUE", "GET IT DONE WOOOEOWOEWOEWWEWOW DO THE THING YOU DUMMY");
        updatePlayerDataView();
    }

    //the task tap callback callback method
    @Override
    public void onTaskTapped(Task task){
        //show the dialog
        TaskPromptDialog tpd = new TaskPromptDialog(this, task);
        tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updatePlayerDataView();
            }
        });
        tpd.show();
    }

    public void updatePlayerDataView() {
        //updates the visible views that display the player's stats.
        SharedPreferences preferences = this.getSharedPreferences("com.games.bad.taskcrawler", Context.MODE_PRIVATE);
        long experience = preferences.getLong("experience", 0);
        long gold = preferences.getLong("gold", 0);
        long level = preferences.getLong("level", 1);
        playerInfoText.setText(String.format("Level: %d, Exp: %d, Gold, %d", experience, gold, level));
    }

    public void updateTaskList() {
        List<Task> tasks = Task.getTasksInOrder(this);
        if(tasks.size() == 0) {
            noTaskTextView.setVisibility(View.VISIBLE);
        }else{
            noTaskTextView.setVisibility(View.GONE);
        }

        this.taskListAdapter.updateList(tasks);
    }

    @Override
    public void onResume() {
        super.onResume();
        taskListAdapter.updateList(Task.getTasksInOrder(this));
        //check the "Tasks" item when this activity resumes.
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.getMenu().getItem(0).setChecked(true);
        updateTaskList();
        updatePlayerDataView();
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

    //Create a notification channel on API26+, because it needs it to run on those systems.
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notificationMethod(String title, String content) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.icon_enemy_elf_archer);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(content);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(uniqueID, notificationBuilder.build());

    }
}