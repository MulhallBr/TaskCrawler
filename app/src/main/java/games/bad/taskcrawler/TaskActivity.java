package games.bad.taskcrawler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import Model.Icon;
import Model.Task;

/*
    This is the base-class for both EditTaskActivity and NewTaskActivity. It is responsible for filling
    common functionality between both activities, as they are both similar.
    Do not attempt to call this class on its own, however. It serves no purpose by itself.
*/

public abstract class TaskActivity extends AppCompatActivity {
    private static String TAG = "TASK_ACTIVITY";

    public TextView taskNameInput;

    protected ConstraintLayout iconSelectLayout;
    protected ConstraintLayout lengthSelectLayout;
    protected ConstraintLayout firstTimeSelectLayout;
    protected ConstraintLayout recurrenceSelectLayout;

    protected Button backButton;
    protected Button okayButton;

    protected TextView lengthTextView;
    protected TextView firstOccurrenceTextView;
    protected TextView recurrenceTextView;

    protected ImageView iconImageView;
    protected TextView iconTextView;

    protected String task_title = null;

    protected int task_icon_id = -1;

    protected int length_hour = -1;
    protected int length_minute = -1;

    protected int next_occurrence_year = -1;
    protected int next_occurrence_month = -1;
    protected int next_occurrence_day = -1;
    protected int next_occurrence_hour = -1;
    protected int next_occurrence_minute = -1;

    protected int interval_days = -1;
    protected int interval_hours = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Enable the back button in the action bar.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        taskNameInput = findViewById(R.id.taskNameInput);
        iconSelectLayout = findViewById(R.id.iconSelectLayout);
        lengthSelectLayout = findViewById(R.id.lengthSelectLayout);
        firstTimeSelectLayout = findViewById(R.id.firstTimeSelectLayout);
        recurrenceSelectLayout = findViewById(R.id.recurrenceSelectLayout);
        backButton = findViewById(R.id.backButton);
        okayButton = findViewById(R.id.okayButton);

        iconImageView = findViewById(R.id.icon_imageview);
        iconTextView = findViewById(R.id.icon_titleview);

        lengthTextView = findViewById(R.id.lengthTextView);

        firstOccurrenceTextView = findViewById(R.id.firstOcurranceTextView);
        recurrenceTextView = findViewById(R.id.recurranceTextView);

        taskNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task_title = taskNameInput.getText().toString();
                onInputChanged();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        iconSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this, IconSelectActivity.class);
                startActivityForResult(intent, 420);
            }
        });

        lengthSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        firstTimeSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFirstTimeDatePickerDialog();    // This will open the Date picker, and then the Time picker
                                                    // and finally set values in the activity state.
            }
        });

        recurrenceSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecurrencePickerDialog();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonPressed();
            }
        });


        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkayButtonPressed();
            }
        });

        updateIconImageView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCodeTwo, Intent data) {

        Log.d(TAG, "Results returned!!!!!"); // Logcat print for debugging

        if (requestCode == 420 && resultCodeTwo == RESULT_OK) {

            task_icon_id = data.getIntExtra("icon_id", -1);

            if (task_icon_id != -1) {
                updateIconImageView();
            }
        }
    }

    protected void updateIconImageView() {
        if(Icon.iconExists(this, task_icon_id)) {
            Icon icon = Icon.getIcon(this, task_icon_id);
            iconImageView.setImageResource(this.getResources().getIdentifier(icon.getIconFilename(), "drawable", this.getPackageName()));
            iconTextView.setText(icon.getName());
        }
    }

    protected void updateRecurrenceTextView() {
        recurrenceTextView.setText(Task.getIntervalAsString(interval_days, interval_hours, true));
        onInputChanged();
    }

    protected void updateLengthTextView() {
        lengthTextView.setText(Task.getLengthAsString(length_hour, length_minute));
        onInputChanged();
    }

    protected void updateFirstTimeTextView() {
        firstOccurrenceTextView.setText(Task.getNextOccurrenceAsString(next_occurrence_year, next_occurrence_month, next_occurrence_day, next_occurrence_hour, next_occurrence_minute));
        onInputChanged();
    }

    protected void showRecurrencePickerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.day_hour_dialog, null);
        dialogBuilder.setView(dialogView);

        final NumberPicker dayNumberPicker = dialogView.findViewById(R.id.dayPicker);
        dayNumberPicker.setMinValue(0);
        dayNumberPicker.setMaxValue(365);
        dayNumberPicker.setValue(2);

        final NumberPicker hourNumberPicker = dialogView.findViewById(R.id.hourPicker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setValue(3);

        final TextView recurTitle = dialogView.findViewById(R.id.recurTitle);
        final TextView upperTitle = dialogView.findViewById(R.id.titleTextView);

        Button rpCancelButton = dialogView.findViewById(R.id.cancelButton);
        Button rpOkayButton = dialogView.findViewById(R.id.okayButton);


        class TitleUpdateInterface {
            void updateText() {
                if( dayNumberPicker.getValue() == 0 && hourNumberPicker.getValue() == 0) {
                    upperTitle.setText("Recurs");
                }else{
                    upperTitle.setText("Recurs every");

                }
                recurTitle.setText(Task.getIntervalAsString(dayNumberPicker.getValue(), hourNumberPicker.getValue(), false));
            }
        };
        final TitleUpdateInterface titleUpdateInterface = new TitleUpdateInterface();

        dayNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                titleUpdateInterface.updateText();
            }
        });

        hourNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                titleUpdateInterface.updateText();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        rpOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interval_days = dayNumberPicker.getValue();
                interval_hours = hourNumberPicker.getValue();
                alertDialog.cancel();
                updateRecurrenceTextView();
            }
        });

        rpCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if(interval_days != -1) {
            dayNumberPicker.setValue(interval_days);
        }
        if(interval_hours != -1) {
            hourNumberPicker.setValue(interval_hours);
        }
        titleUpdateInterface.updateText();

    }

    private void showFirstTimeTimePickerDialog(final int dp_year, final int dp_month, final int dp_dayOfMonth) {
        final Calendar myCalender = Calendar.getInstance();

        // Define the Time Picker Dialog
        final TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int tp_hour, int tp_minute) {
                if (view.isShown()) {
                    next_occurrence_year = dp_year;
                    next_occurrence_month = dp_month;
                    next_occurrence_day = dp_dayOfMonth;
                    next_occurrence_hour = tp_hour;
                    next_occurrence_minute = tp_minute;

                    updateFirstTimeTextView();
                }
            }
        };

        // WHY IS THIS NOT 24 HOURS?!?!?!?!??!!?
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, myTimeListener, myCalender.get(Calendar.HOUR), myCalender.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("What time will it start?\n(HH:MM)");
        timePickerDialog.show();
    }

    protected void showFirstTimeDatePickerDialog() {
        final Calendar myCalender = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dp_year, int dp_month, int dp_dayOfMonth) {
                showFirstTimeTimePickerDialog(dp_year, dp_month, dp_dayOfMonth);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Which day will it start?");
        datePickerDialog.show();
    }

    protected void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int tp_hour, int tp_minute) {
                if (view.isShown()) {
                    length_hour = tp_hour;
                    length_minute = tp_minute;
                    updateLengthTextView();
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, myTimeListener, 0, 30, true);
        timePickerDialog.setTitle("How long will it take?\n(HH:MM)");
        timePickerDialog.show();
    }

    protected void onBackButtonPressed() {
        finish();
    }

    protected void onOkayButtonPressed() {
        finish();
    }

    // Called any time the user changes an input value
    protected void onInputChanged() {
        // Override in child classes ?
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
