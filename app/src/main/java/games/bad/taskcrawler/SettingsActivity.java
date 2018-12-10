package games.bad.taskcrawler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
    }


}
