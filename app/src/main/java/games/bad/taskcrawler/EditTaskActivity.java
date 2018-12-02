package games.bad.taskcrawler;

import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

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
        start_year = task.getFirstOccurrenceYear();
        start_month = task.getFirstOccurrenceMonth();
        start_day = task.getFirstOccurrenceDayOfMonth();
        start_hour = task.getRecurrenceHour();
        start_minute = task.getRecurrenceMinute();

        taskNameInput.setText(task.getTitle());
        updateLengthTextView();
        updateFirstTimeTextView();
        updateRecurrenceTextView();
    }

    @Override
    protected void onOkayButtonPressed() {
        super.onOkayButtonPressed();
        //save the changes made to this task.
    }
}
