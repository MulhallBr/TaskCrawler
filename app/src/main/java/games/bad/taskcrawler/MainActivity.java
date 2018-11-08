package games.bad.taskcrawler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import Model.AppDatabase;
import Model.Task;
import Model.TaskListRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Task> tasks;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppDatabase.getAppDatabase(this).taskDAO().nukeTable(); //WIPES THE ENTIRE TASK TABLE.
        Task.createTask(this, new Task("Make a healthy breakfast", 20, 0, 24, 8));
        Task.createTask(this, new Task("Go to sleep a little earlier tonight :(", 50, 1, 24, 48));
        Task.createTask(this, new Task("End global fascism", 69, 2, 24, 48));
        Task.createTask(this, new Task("Clean up the streets.", 69, 2, 24, 48));
        Task.createTask(this, new Task("Find 600 bees and put them inside somebody's car.", 69, 2, 24, 48));
        Task.createTask(this, new Task("Go 2 days without smoking cigarettes", 69, 2, 24, 48));

        tasks = Task.getTasksInOrder(this);
        RecyclerView taskListRecyclerView = findViewById(R.id.task_list_recycler_view);
        TaskListRecyclerViewAdapter taskListRecycleViewAdapter = new TaskListRecyclerViewAdapter(tasks, this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "OnCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsMenuItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            // Handle the camera action

        } else if (id == R.id.nav_shop) {
            Intent myIntent = new Intent(MainActivity.this, ShopActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else if (id == R.id.nav_new_task) {

        } else if (id == R.id.nav_inventory) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}