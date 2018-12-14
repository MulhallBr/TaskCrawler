package games.bad.taskcrawler;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import Model.AppDatabase;
import Model.Icon;
import Model.Player;
import Model.Weapon;

public class SettingsActivity extends AppCompatActivity {

    private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        parentLayout = findViewById(android.R.id.content);

        // Enable the back button in the action bar.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Switch notifications = findViewById(R.id.switch_notifications);
        Boolean notifiSwitchState = notifications.isChecked();  // Check to see if the notifications are on or off.
                                                                // ... it doesn't do anything yet.

        Button reset = findViewById(R.id.btn_reset_data);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });

        Button about = findViewById(R.id.btn_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDevelopers();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Launch a new activity to show the About app and developers page.
    private void aboutDevelopers() {
        Intent myIntent = new Intent(SettingsActivity.this, AboutActivity.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    // Method to reset all player data and clear the database to default settings.
    private void resetData() {
        Snackbar.make(parentLayout, "All user settings reset!", Snackbar.LENGTH_LONG).show();

        Player.getPlayer().reset(this);  //WIPE USER PREFERENCES/STATS

        AppDatabase.getAppDatabase(this).taskDAO().nukeTable();
        AppDatabase.getAppDatabase(this).weaponDAO().nukeTable();
        AppDatabase.getAppDatabase(this).iconDAO().nukeTable();

        //then, re-init the database. This will have to be a method inside each item class.
        Weapon.initializeItems(this, this.getResources());
        Icon.initializeItems(this, this.getResources());
    }
}
