package games.bad.taskcrawler;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Model.Task;

public class TaskFightDialog extends Dialog {
    private Activity activity;
    private Task task;
    private Button okayButton;
    private TextView experienceTextView, goldTextView, levelTextView;
    private ConstraintLayout itemView;
    private ImageView taskIconView;

    private int experienceGained, goldGained, levelGained;

    TaskFightDialog(Activity act, Task task) {
        super(act);
        activity = act;
        this.task = task;

        //task.complete();


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_fight_dialog);

        okayButton = findViewById(R.id.okayButton);
        experienceTextView = findViewById(R.id.experienceTextView);
        goldTextView = findViewById(R.id.goldTextView);
        levelTextView = findViewById(R.id.levelTextView);
        itemView = findViewById(R.id.itemView);
        taskIconView = findViewById(R.id.iconImageView);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okayButtonOnClick();
            }
        });
        fillViews();
    }


    private void fillViews() {
        taskIconView.setImageResource(this.task.getIconResourceId(activity, activity.getResources()));
        long[] gains = this.task.complete(activity);

        experienceTextView.setText(String.format("+ %dxp", gains[0]));
        if (gains[1] != 0) {
            levelTextView.setText(String.format("Leveled up to level %d", gains[1]));
        } else {
            // Hide these views
        }
        goldTextView.setText(String.format("+ %d gold", gains[2]));

    }

    private void okayButtonOnClick() {
        dismiss();
    }
}
