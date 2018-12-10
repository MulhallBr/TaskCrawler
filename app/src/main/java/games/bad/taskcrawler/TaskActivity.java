package games.bad.taskcrawler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
    Do not attempt to call this class on its own, however. It servers no purpose by itself.
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
    protected TextView firstOcurranceTextView;
    protected TextView recurranceTextView;

    protected ImageView iconImageView;
    protected TextView iconTextView;

    protected String task_title = "";

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
        Activity activity = this;

        //enable the Back arrow in the Action Bar:
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

        iconImageView = findViewById(R.id.item_shop_icon);
        iconTextView = findViewById(R.id.icon_titleview);

        lengthTextView = findViewById(R.id.lengthTextView);

        firstOcurranceTextView = findViewById(R.id.firstOcurranceTextView);
        recurranceTextView = findViewById(R.id.recurranceTextView);

        final Calendar myCalender = Calendar.getInstance();

        next_occurrence_year = myCalender.get(Calendar.YEAR);
        next_occurrence_month = myCalender.get(Calendar.MONTH);
        next_occurrence_day = myCalender.get(Calendar.DAY_OF_MONTH);
        next_occurrence_hour = myCalender.get(Calendar.HOUR_OF_DAY);
        next_occurrence_minute = myCalender.get(Calendar.MINUTE);


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
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, IconSelectActivity.class);
                startActivityForResult(intent, 420);
            }
        });

        lengthSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        firstTimeSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFirstTimeDatePickerDialog(); //this will open the date picker, and then the Time picker and finally set values in the activity state.
            }
        });

        recurrenceSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecurrencePickerDialog();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });


        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkayButtonPressed();
            }
        });

        updateIconImageView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Results returned!!!!!");
        if (requestCode == 420) {
            if (resultCode == RESULT_OK) {
                task_icon_id = data.getIntExtra("icon_id", -1);
                if(task_icon_id != -1) {
                    updateIconImageView();
                }
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

        recurranceTextView.setText(Task.getIntervalAsString(interval_days, interval_hours, true));

        onInputChanged();
    }

    protected void updateLengthTextView() {
        lengthTextView.setText(Task.getLengthAsString(length_hour, length_minute, false));
        onInputChanged();
    }

    protected void updateFirstTimeTextView() {
        firstOcurranceTextView.setText(Task.getNextOccurrenceAsString(next_occurrence_year, next_occurrence_month, next_occurrence_day, next_occurrence_hour, next_occurrence_minute));
        onInputChanged();
    }

    protected void showRecurrencePickerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.day_hour_dialog, null);
        dialogBuilder.setView(dialogView);

        final NumberPicker dayNumberPicker = (NumberPicker) dialogView.findViewById(R.id.dayPicker);
        dayNumberPicker.setMinValue(0);
        dayNumberPicker.setMaxValue(365);
        dayNumberPicker.setValue(2);

        final NumberPicker hourNumberPicker = (NumberPicker) dialogView.findViewById(R.id.hourPicker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setValue(3);

        final TextView recurTitle = (TextView) dialogView.findViewById(R.id.recurTitle);
        final TextView upperTitle = (TextView) dialogView.findViewById(R.id.titleTextView);

        Button rpCancelButton = (Button) dialogView.findViewById(R.id.cancelButton);
        Button rpOkayButton = (Button) dialogView.findViewById(R.id.okayButton);


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
            public void onClick(View v) {
                interval_days = dayNumberPicker.getValue();
                interval_hours = hourNumberPicker.getValue();
                alertDialog.cancel();
                updateRecurrenceTextView();
            }
        });

        rpCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        //Define the Time Picker Dialog
        final TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int tp_hour, int tp_minute) {
                if (view.isShown()) {
                    Calendar myCal = Calendar.getInstance();
                    myCal.set(Calendar.YEAR, dp_year);
                    myCal.set(Calendar.MONTH, dp_month);
                    myCal.set(Calendar.DAY_OF_MONTH, dp_dayOfMonth);
                    myCal.set(Calendar.HOUR_OF_DAY, tp_hour);
                    myCal.set(Calendar.MINUTE, tp_minute);
                    if( myCal.getTimeInMillis() - System.currentTimeMillis() > 0) {
                        next_occurrence_year = dp_year;
                        next_occurrence_month = dp_month;
                        next_occurrence_day = dp_dayOfMonth;
                        next_occurrence_hour = tp_hour;
                        next_occurrence_minute = tp_minute;
                        updateFirstTimeTextView();
                    }else{
                        Snackbar.make(recurrenceSelectLayout, "Cannot set time to the past!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        };
        //WHY IS THIS NOT 24 HOURS?!?!?!?!??!!?
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, myTimeListener, next_occurrence_hour, next_occurrence_minute, false);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, next_occurrence_year, next_occurrence_month, next_occurrence_day);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, myTimeListener, length_hour, length_minute, true);
        timePickerDialog.setTitle("How long will it take?\n(HH:MM)");
        timePickerDialog.show();
    }

    protected void onBackButtonPressed() {
        finish();
    }

    protected void onOkayButtonPressed() {
        finish();
    }

    //called any time the user changes an input value
    protected void onInputChanged() {

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
