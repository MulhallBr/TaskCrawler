package games.bad.taskcrawler;

import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import Model.AppDatabase;
import Model.Task;

public class EditTaskActivity extends TaskActivity {
    int taskID;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View parentLayout = findViewById(android.R.id.content);

        //in this activity, the "back" button becomes the "DELETE" button.
        backButton.setText("DELETE");//change button text to "DELETE"
        //change the color tint to a nice deep Red.
        backButton.getBackground().setColorFilter(this.getResources().getColor(R.color.warningPrimary), PorterDuff.Mode.MULTIPLY);

        taskID = getIntent().getIntExtra("task_id", -1); //see if this activity was passed a task id

        if(taskID == -1) { //if it was not
            //show an error.
            Snackbar.make(parentLayout, "ERROR: Invalid Task ID!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            finish(); //and end the activity.
        }

        task = Task.getTaskById(this, taskID); //get the task we want to edit.

        task_title = task.getTitle();
        task_icon_id = task.getIconId();

        length_hour = task.getLengthHours();
        length_minute = task.getLengthMinutes();

        next_occurrence_year = task.getNextOccurrenceYear();
        next_occurrence_month = task.getNextOccurrenceMonth();
        next_occurrence_day = task.getNextOccurrenceDay();
        next_occurrence_hour = task.getNextOccurrenceHour();
        next_occurrence_minute = task.getNextOccurrenceMinute();

        interval_days = task.getIntervalDays();
        interval_hours = task.getIntervalHour();

        //Fill the views with the task data :)
        taskNameInput.setText(task.getTitle());
        updateIconImageView();
        updateLengthTextView();
        updateFirstTimeTextView();
        updateRecurrenceTextView();
    }

    @Override
    protected void onOkayButtonPressed() {
        super.onOkayButtonPressed();

        task.setTitle(task_title);

        task.setLengthHours(length_hour);
        task.setLengthMinutes(length_minute);

        task.setNextOccurrenceYear(next_occurrence_year);
        task.setNextOccurrenceMonth(next_occurrence_month);
        task.setNextOccurrenceDay(next_occurrence_day);
        task.setNextOccurrenceHour(next_occurrence_hour);
        task.setNextOccurrenceMinute(next_occurrence_minute);

        task.setIconId(task_icon_id);

        task.setIntervalDays(interval_days);
        task.setIntervalHour(interval_hours);

        task.commit(getApplicationContext());
        //save the changes made to this task.
    }

    @Override
    protected void onBackButtonPressed() {
        //this is actually the delete button!!!!
        AppDatabase.getAppDatabase(getBaseContext()).taskDAO().deleteTask(task);//delete the task!
        super.onBackButtonPressed();
    }
}
