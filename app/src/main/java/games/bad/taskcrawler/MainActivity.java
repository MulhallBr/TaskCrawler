package games.bad.taskcrawler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.util.Log;
import java.util.List;

import Adapters.TaskListAdapter;
import Interfaces.TaskTapCallback;
import Model.Icon;
import Model.Player;
import Model.Task;
import Model.Weapon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskTapCallback {

    private static final String CHANNEL_ID = "com.games.bad.taskcrawler.notifx";

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private RecyclerView taskListRecyclerView;
    
    private TextView noTaskTextView;
    private TextView playerInfoText;
    private TextView playerInfoGold;

    // Initialize the Adapter, Handler and Runnable for the RecyclerView
    private TaskListAdapter taskListAdapter;
    private Handler taskListUpdateHandler;
    private Runnable taskListUpdateRunnable;
    private NavigationView navigationView;
    private Toolbar toolbarLayout;

    private LinearLayout drawerHeaderContainer;

    private TextView drawerPlayerInfoText;
    private ImageView equippedWeaponImageView;
    private ImageView playerIconImageView;

    // PROGRESS BAR
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "OnCreate"); // Logcat print item for debugging

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noTaskTextView = findViewById(R.id.noTaskTextView);
        playerInfoText = findViewById(R.id.playerInfoLevel);
        playerInfoGold = findViewById(R.id.playerInfoGold);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toolbarLayout = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarLayout);

        playerIconImageView = findViewById(R.id.playerIconImageView);

        equippedWeaponImageView = findViewById(R.id.equippedWeaponImageView);

        drawerHeaderContainer = findViewById(R.id.drawerHeaderContainer);

        //drawerPlayerInfoText = drawerHeaderContainer.findViewById(R.id.playerInfoText);

        Weapon.initializeItems(this, this.getResources());  // Initialize the database,
        Icon.initializeItems(this, this.getResources());    // if it hasn't been done already.


        // Toggles the name of the action bar based on whether or not the  navigation drawer is open.
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

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

                taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 5000); //15000
            }
        };
        taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 5000);
        createNotificationChannel(); // Create the notification channel so this app can do noticiations on OREO+
        //notificationMethod("YOUR THING IS DUE", "GET IT DONE. DO THE THING YOU DUMMY");
        updatePlayerDataView(); // Display player information.

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
                updateTaskList();
            }
        });

        tpd.show();
    }

    // Method in order to update the Player's different stats, displayed at the top of the screen.
    public void updatePlayerDataView() {
    
        // Updates the views that display the player's stats.
        if(Player.getPlayer().getEquippedIconId(this) != -1) {
            Icon playerIcon = Icon.getIcon(this, (int)Player.getPlayer().getEquippedIconId(this));
            playerIconImageView.setImageResource(this.getResources().getIdentifier(playerIcon.getIconFilename(), "drawable", this.getApplicationContext().getPackageName()));
        }

        if(Player.getPlayer().getEquippedWeaponId(this) != -1) {
            equippedWeaponImageView.setImageResource(this.getResources().getIdentifier(Weapon.getWeapon(getBaseContext(), (int)Player.getPlayer().getEquippedWeaponId(getBaseContext())).getIconFilename(), "drawable", this.getApplicationContext().getPackageName()));
        }else{
            //equippedWeaponImageView.setImageResource(0);
        }

        String level = String.format("Level: %d",
                Player.getPlayer().getLevel(this));      // PLAYER LEVEL

        String gold = String.format("%d",
                Player.getPlayer().getGold(this));      // PLAYER GOLD

        playerInfoText.setText(level);
        playerInfoGold.setText(gold);

        // Update the progress bar with Player Exp and it's new maximum
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress((int) Player.getPlayer().getExperience(this));
        progressBar.setMax((int) Player.getPlayer().getNextLevelExperience(this));

        //drawerPlayerInfoText.setText("oops");
    }

    // Method in order to update the task list, whenever editing, deleting or adding a task.
    public void updateTaskList() {
        // Place all of the Task objects into a list.
        List<Task> tasks = Task.getTasksInOrder(this);

        for (Task task : tasks) {
            long overdueCycles = task.getOverduePeriods(this);
            if (overdueCycles > 0) {
                //Snackbar.make(playerIconImageView, String.format("EE: %d", overdueCycles), Toast.LENGTH_LONG).show();
                Player.getPlayer().addExperience(this, overdueCycles * - 5); //you lose 5xp for every period you leave each task overdue.
            }
        }

        if(tasks.size() == 0) {
            // If there are no tasks, show the text view prompting the user to add new tasks.
            noTaskTextView.setVisibility(View.VISIBLE);
        } else {
            // If there are tasks, make sure that prompt is not visible.
            noTaskTextView.setVisibility(View.GONE);
        }

        this.taskListAdapter.updateList(tasks);
        updatePlayerDataView();
    }

    // We override this method in order to make certain updates.
    @Override
    public void onResume() {
        super.onResume();

        taskListAdapter.updateList(Task.getTasksInOrder(this));

        // Check the "Tasks" item when the MainActivity resumes.
        navigationView.getMenu().getItem(0).setChecked(true);

        // Update the task list and the player's stats.
        updateTaskList();
        updatePlayerDataView();
    }

    // We override this method so that if the NavDrawer is open,
    // it won't close the app if the back button is pressed.
    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed"); // Logcat print item for debugging.
        
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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

        drawerLayout.closeDrawer(GravityCompat.START);
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
}