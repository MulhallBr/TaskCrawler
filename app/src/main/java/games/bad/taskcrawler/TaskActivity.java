package games.bad.taskcrawler;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TaskActivity extends AppCompatActivity {
    //ConstraintLayout fuck = findViewById(R.id.fuckyou);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    public void frick(View v_) {
       Snackbar.make(v_, "AAAAAAAAAAAAA FUCK!!!!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
