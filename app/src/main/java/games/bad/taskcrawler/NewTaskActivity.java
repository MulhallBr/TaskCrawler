package games.bad.taskcrawler;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewTaskActivity extends TaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        okayButton.setEnabled(false);
    }

    @Override
    protected void onInputChanged() {
        super.onInputChanged();
        if(     task_title != "" &&
                task_icon_id != -1 &&
                length_hour != -1 &&
                length_minute != -1 &&
                start_year != -1 &&
                start_month != -1 &&
                start_day != -1 &&
                start_hour != -1 &&
                start_minute != -1 &&
                interval_days != -1 &&
                interval_hours != -1) {
            okayButton.setEnabled(true);
        }else{
            okayButton.setEnabled(false);
        }
    }

}
