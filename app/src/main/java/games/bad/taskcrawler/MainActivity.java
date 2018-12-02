package games.bad.taskcrawler;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import Model.AppDatabase;
import Model.Icon;
import Model.Task;
import Adapters.TaskListAdapter;
import Model.Weapon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        AppDatabase.getAppDatabase(this).clearAllTables();
        Weapon.initializeItems(this, this.getResources()); //init the database, if it is not already.
        Icon.initializeItems(this, this.getResources());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //;AppDatabase.getAppDatabase(this).taskDAO().nukeTable(); //WIPES THE ENTIRE TASK TABLE.
        Task.createTask(this, new Task("Make a healthy breakfast", 20, 0, 24, 8));
        Task.createTask(this, new Task("Go to sleep a little earlier tonight :(", 50, 1, 24, 48));
        Task.createTask(this, new Task("End global fascism", 69, 2, 24, 48));
        Task.createTask(this, new Task("Clean up the streets.", 69, 2, 24, 48));
        Task.createTask(this, new Task("Find 600 bees and put them inside somebody's car.", 69, 2, 24, 48));
        Task.createTask(this, new Task("Go 2 days without smoking cigarettes", 69, 2, 24, 48));

        List<Task> tasks = Task.getTasksInOrder(this);
        RecyclerView taskListRecyclerView = findViewById(R.id.task_list_recycler_view);
        TaskListAdapter taskListRecycleViewAdapter = new TaskListAdapter(tasks, this);
        taskListRecyclerView.setAdapter(taskListRecycleViewAdapter);
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();

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

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}