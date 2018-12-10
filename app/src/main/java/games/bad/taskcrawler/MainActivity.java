package games.bad.taskcrawler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;

import android.os.Handler;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Interfaces.TaskTapCallback;
import Model.Icon;
import Model.Player;
import Model.Task;
import Adapters.TaskListAdapter;
import Model.Weapon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskTapCallback {

    // Items needed for a basic notification
    private static final int uniqueID = 420111;
    private static final String CHANNEL_ID = "com.games.bad.taskcrawler.notifx";

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private RecyclerView taskListRecyclerView;
    private TextView noTaskTextView;
    private TaskListAdapter taskListAdapter;

    private Handler taskListUpdateHandler;
    private Runnable taskListUpdateRunnable;
    private NavigationView navigationView;
    private Toolbar toolbarLayout;

    private TextView playerInfoText;
    private LinearLayout drawerHeaderContainer;

    private TextView drawerPlayerInfoText;
    private ImageView equippedWeaponImageView;
    private ImageView playerIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noTaskTextView = findViewById(R.id.noTaskTextView);
        playerInfoText = findViewById(R.id.playerInfoText);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toolbarLayout = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarLayout);

        playerIconImageView = findViewById(R.id.playerIconImageView);

        equippedWeaponImageView = findViewById(R.id.equippedWeaponImageView);

        drawerHeaderContainer = findViewById(R.id.drawerHeaderContainer);

        //drawerPlayerInfoText = drawerHeaderContainer.findViewById(R.id.playerInfoText);


        Weapon.initializeItems(this, this.getResources()); //init the database, if it is not already.
        Icon.initializeItems(this, this.getResources());

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

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
                taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 5000); //15000
            }
        };
        taskListUpdateHandler.postDelayed(taskListUpdateRunnable, 5000);
        createNotificationChannel(); //create the notification channel so this app can do noticiations on OREO+
        //notificationMethod("YOUR THING IS DUE", "GET IT DONE WOOOEOWOEWOEWWEWOW DO THE THING YOU DUMMY");
        updatePlayerDataView(); //display player bullshit.
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
                updateTaskList();
            }
        });
        tpd.show();
    }

    public void updatePlayerDataView() {
        //updates the visible views that display the player's stats.
        if(Player.getPlayer().getEquippedIconId(this) != -1) {
            Icon playerIcon = Icon.getIcon(this, (int)Player.getPlayer().getEquippedIconId(this));
            playerIconImageView.setImageResource(this.getResources().getIdentifier(playerIcon.getIconFilename(), "drawable", this.getApplicationContext().getPackageName()));
        }

        if(Player.getPlayer().getEquippedWeaponId(this) != -1) {
            equippedWeaponImageView.setImageResource(this.getResources().getIdentifier(Weapon.getWeapon(getBaseContext(), (int)Player.getPlayer().getEquippedWeaponId(getBaseContext())).getIconFilename(), "drawable", this.getApplicationContext().getPackageName()));
        }else{
            //equippedWeaponImageView.setImageResource(0);
        }

        String text = String.format("Level: %d, Exp: %d, Gold, %d",
                Player.getPlayer().getLevel(this),
                Player.getPlayer().getExperience(this),
                Player.getPlayer().getGold(this));
        playerInfoText.setText(text);

        //drawerPlayerInfoText.setText("oops");

    }

    public void updateTaskList() {
        List<Task> tasks = Task.getTasksInOrder(this);

        for (Task task : tasks) {
            long overdueCycles = task.getOverduePeriods(this);
            if (overdueCycles > 0) {
                //Snackbar.make(playerIconImageView, String.format("EE: %d", overdueCycles), Toast.LENGTH_LONG).show();
                Player.getPlayer().addExperience(this, overdueCycles * - 5); //you lose 5xp for every period you leave each task overdue.
            }
        }

        if(tasks.size() == 0) {
            noTaskTextView.setVisibility(View.VISIBLE);
        }else{
            noTaskTextView.setVisibility(View.GONE);
        }

        this.taskListAdapter.updateList(tasks);
        updatePlayerDataView();
    }

    @Override
    public void onResume() {
        super.onResume();
        taskListAdapter.updateList(Task.getTasksInOrder(this));
        //check the "Tasks" item when this activity resumes.
        navigationView.getMenu().getItem(0).setChecked(true);
        updateTaskList();
        updatePlayerDataView();
    }

    //we override this method so that if the NavDrawer is open, it doesn't close the app.
    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed");
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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

        drawerLayout.closeDrawer(GravityCompat.START);
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