package games.bad.taskcrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import Model.AppDatabase;
import Model.Icon;
import Model.Player;
import Model.Weapon;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Switch notifications = findViewById(R.id.switch_notifications);
        Boolean notifiSwitchState = notifications.isChecked();

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

    private void aboutDevelopers() {
        Intent myIntent = new Intent(SettingsActivity.this, AboutActivity.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    private void resetData() {
        //WIPE USER PREFERENCES/STATS
        //Player.getPlayer().reset(this);

        AppDatabase.getAppDatabase(this).taskDAO().nukeTable();
        AppDatabase.getAppDatabase(this).weaponDAO().nukeTable();
        AppDatabase.getAppDatabase(this).iconDAO().nukeTable();
        //AppDatabase.getAppDatabase(this).consumableDAO().nukeTable();

        //then, re-init the database. This will have to be a method inside each item class.
        Weapon.initializeItems(this, this.getResources());
        Icon.initializeItems(this, this.getResources());

    }


}
