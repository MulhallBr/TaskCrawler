package games.bad.taskcrawler;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Model.Task;

public class TaskPromptDialog extends Dialog {
    private Activity activity;
    private Button editButton, fightButton;
    private ImageView iconImageView;
    private TextView titleTextView, lengthTextView, nextOccurrenceTextView, intervalTextView;
    private Task task;

    TaskPromptDialog(Activity act, Task task) {
        super(act);
        activity = act;
        this.task = task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_prompt_dialog);

        iconImageView = findViewById(R.id.iconImageView);
        titleTextView = findViewById(R.id.titleTextView);
        nextOccurrenceTextView = findViewById(R.id.nextOccurrenceTextView);
        lengthTextView = findViewById(R.id.lengthTextView);
        intervalTextView = findViewById(R.id.intervalTextView);

        editButton = findViewById(R.id.editButton);
        fightButton = findViewById(R.id.okayButton);

        iconImageView.setImageResource(task.getIconResourceId(activity, activity.getResources()));
        titleTextView.setText(task.getTitle());
        lengthTextView.setText(task.getLengthAsString(false));
        nextOccurrenceTextView.setText(task.getNextOccurrenceAsString());
        intervalTextView.setText(task.getIntervalAsString(true));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editButtonOnClick();
                dismiss();  // This is necessary, because if the user deletes the task,
                            // the app will crash if it comes back to this activity trying to modify the non-existent task.
            }
        });

        fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fightButtonOnClick();
            }
        });
    }

    private void editButtonOnClick() {
        Intent myIntent = new Intent(activity, EditTaskActivity.class);
        myIntent.putExtra("task_id", this.task.getId());
        activity.startActivity(myIntent);
    }

    private void fightButtonOnClick() {
        TaskFightDialog tfd = new TaskFightDialog(activity, this.task);
        tfd.show();
        dismiss(); // dismiss this dialog so it isn't sitting behind the new one.
    }
}
