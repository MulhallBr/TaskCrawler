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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/*
    This is the base-class for both EditTaskActivity and NewTaskActivity. It is responsible for filling
    common functionality between both activities, as they are both similar.
    Do not attempt to call this class on its own, however. It servers no purpose by itself.
 */

public abstract class TaskActivity extends AppCompatActivity {
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

    protected String task_title = "";

    protected int task_icon_id = -1;

    protected int length_hour = -1;
    protected int length_minute = -1;

    protected int start_year = -1;
    protected int start_month = -1;
    protected int start_day = -1;
    protected int start_hour = -1;
    protected int start_minute = -1;

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

        lengthTextView = findViewById(R.id.lengthTextView);

        firstOcurranceTextView = findViewById(R.id.firstOcurranceTextView);
        recurranceTextView = findViewById(R.id.recurranceTextView);

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
                startActivity(intent);
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
                finish();
            }
        });


        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkayButtonPressed();
                finish();
            }
        });
    }

    protected void updateRecurrenceTextView() {
        if(interval_hours == 0 && interval_days == 0) {
            recurranceTextView.setText("Never recurs");
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append("Recurs every ");

            if(interval_days != 0) {
                sb.append("%d day");
                if(interval_days != 1) {
                    sb.append("s");
                }
            }

            if(interval_days != 0 && interval_hours != 0){
                sb.append(" and ");
            }

            if(interval_hours != 0) {
                sb.append("%d hour");
                if(interval_hours != 1) {
                    sb.append("s");
                }
            }
            if(interval_days != 0) {
                recurranceTextView.setText(String.format(sb.toString(), interval_days, interval_hours));
            }else{
                recurranceTextView.setText(String.format(sb.toString(), interval_hours));
            }
        }
        onInputChanged();
    }

    protected void updateLengthTextView() {
        lengthTextView.setText(String.format("Takes %d hour(s) and %d minute(s)", length_hour, length_minute));
        onInputChanged();
    }

    protected void updateFirstTimeTextView() {
        Calendar myCalender = Calendar.getInstance();
        //calculate the nice readable "Tues, Jan" from "dp_month = 1 && dp_dayOfMonth = 7"
        myCalender.set(Calendar.YEAR, start_year);
        myCalender.set(Calendar.MONTH, start_month);
        myCalender.set(Calendar.DAY_OF_MONTH, start_day);

        String day_of_week = myCalender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String month_name = myCalender.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        firstOcurranceTextView.setText(String.format("First occurs on %s %s %d, %d at %02d:%02d", day_of_week, month_name, start_day, start_year, start_hour, start_minute));
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
        final TextView upperTitle = (TextView) dialogView.findViewById(R.id.upperTitle);

        Button rpCancelButton = (Button) dialogView.findViewById(R.id.cancelButton);
        Button rpOkayButton = (Button) dialogView.findViewById(R.id.okayButton);


        class TitleUpdateInterface {
            void updateText() {
                StringBuilder s = new StringBuilder();
                s.append("%d day");
                if(dayNumberPicker.getValue() != 1) { s.append("s"); }
                s.append(", %d hour");
                if(hourNumberPicker.getValue() != 1) { s.append("s"); }
                if( dayNumberPicker.getValue() == 0 && hourNumberPicker.getValue() == 0) {
                    upperTitle.setText("Recurs");
                    recurTitle.setText("Never");
                }else{
                    upperTitle.setText("Recurs every");
                    recurTitle.setText(String.format(s.toString(), dayNumberPicker.getValue(), hourNumberPicker.getValue()));
                }
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
        final Calendar myCalender = Calendar.getInstance();

        //Define the Time Picker Dialog
        final TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int tp_hour, int tp_minute) {
                if (view.isShown()) {
                    start_year = dp_year;
                    start_month = dp_month;
                    start_day = dp_dayOfMonth;
                    start_hour = tp_hour;
                    start_minute = tp_minute;

                    updateFirstTimeTextView();
                }
            }
        };
        //WHY IS THIS NOT 24 HOURS?!?!?!?!??!!?

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

    protected void onOkayButtonPressed() {

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
