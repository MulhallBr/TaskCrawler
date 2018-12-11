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

import Model.Player;
import Model.Task;

public class TaskFightDialog extends Dialog {
    private Activity activity;
    private Task task;
    private Button okayButton;
    private TextView experienceTextView, goldTextView, levelTextView;
    private ImageView taskIconView;
    private ImageView levelUpImageView;

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
        taskIconView = findViewById(R.id.iconImageView);
        levelUpImageView = findViewById(R.id.levelUpImageView);

        levelTextView.setVisibility(View.GONE);
        levelUpImageView.setVisibility(View.GONE);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okayButtonOnClick();
            }
        });
        fillViews();
    }

    public void fillViews() {
        long oldLevel, oldGold;
        oldLevel = Player.getPlayer().getLevel(activity);
        oldGold = Player.getPlayer().getGold(activity);
        long experience_gained = this.task.complete(activity);
        long goldGained = Player.getPlayer().getGold(activity) - oldGold;
        long levelGained = Player.getPlayer().getLevel(activity) - oldLevel;

        taskIconView.setImageResource(this.task.getIconResourceId(activity, activity.getResources()));
        experienceTextView.setText(String.format("+ %d experience", experience_gained));
        if(goldGained > 0) {
            goldTextView.setText(String.format("+ %d gold", goldGained));
        }
        if(levelGained > 0) {
            levelUpImageView.setVisibility(View.VISIBLE);
            levelTextView.setVisibility(View.VISIBLE);
            levelTextView.setText(String.format("Leveled up to level %d", Player.getPlayer().getLevel(activity)));
        }
    }

    private void okayButtonOnClick() {
        dismiss();
    }
}
