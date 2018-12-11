package games.bad.taskcrawler;

import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;

import Model.AppDatabase;
import Model.Task;

public class EditTaskActivity extends TaskActivity {

    int taskID;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View parentLayout = findViewById(android.R.id.content);

        // In the EditTaskActivity, the "BACK" button becomes the "DELETE" button.
        backButton.setText("DELETE"); // Change button text to "DELETE".

        // Change the color of it's background to a nice deep red.
        backButton.getBackground().setColorFilter(this.getResources().getColor(R.color.warningPrimary), PorterDuff.Mode.MULTIPLY);

        taskID = getIntent().getIntExtra("task_id", -1); // Check if this activity was passed a task ID.

        // If it was not passed an ID, then show an error, and end the activity.
        if(taskID == -1) {

            Snackbar.make(parentLayout, "ERROR: Invalid Task ID!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

            finish(); // Ending the activity.
        }

        task = Task.getTaskById(this, taskID);  // Grab the task the user wants to edit
                                                        // using the ID that was passed in.

        // Grab all of the Task's information using the class getters.
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

        // Fill the views with the Task's information
        taskNameInput.setText(task.getTitle());
        updateIconImageView();
        updateLengthTextView();
        updateFirstTimeTextView();
        updateRecurrenceTextView();
    }

    @Override
    protected void onOkayButtonPressed() {
        super.onOkayButtonPressed();

        // Replace all of the old information with the edited info.
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

        // Save all of the changes made to this task.
        task.commit(getApplicationContext());
    }

    @Override
    protected void onBackButtonPressed() {
        // This is actually the delete button!
        // Was changed above, in onCreate.
        AppDatabase.getAppDatabase(getBaseContext()).taskDAO().deleteTask(task); // Delete the task!
        super.onBackButtonPressed();
    }
}
