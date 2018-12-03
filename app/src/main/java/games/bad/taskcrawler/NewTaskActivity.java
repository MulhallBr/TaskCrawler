package games.bad.taskcrawler;

import android.os.Bundle;

import Model.Task;

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
                next_occurrence_year != -1 &&
                next_occurrence_month != -1 &&
                next_occurrence_day != -1 &&
                next_occurrence_hour != -1 &&
                next_occurrence_minute != -1 &&
                interval_days != -1 &&
                interval_hours != -1) {
            okayButton.setEnabled(true);
        }else{
            okayButton.setEnabled(false);
        }
    }

    @Override
    protected void onOkayButtonPressed() {
        super.onOkayButtonPressed();
        //save the new task :)
        Task.createTask(this, new Task(this.task_title, this.task_icon_id, this.length_hour, this.length_minute, this.next_occurrence_year, this.next_occurrence_month, this.next_occurrence_day, this.next_occurrence_hour, this.next_occurrence_minute, this.interval_days, this.interval_hours));
    }


}
