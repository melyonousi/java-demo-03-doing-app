package net.casetrue.doing;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity implements View.OnClickListener {
    TaskDBHelper taskDBHelper;
    TextView time_textView;
    TextView date_textView;
    EditText edit_describe;
    Button btnAdd;
    Button btnDelete;
    Button btnEdit;
    Button btnCancel;
    long id;
    TimePickerDialog t_picker;
    DatePickerDialog d_picker;
    AlertDialog.Builder alert;
    Task _task;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        btnAdd = findViewById(R.id.btnAddTask);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        taskDBHelper = new TaskDBHelper(this);
        date_textView = findViewById(R.id.e_date);
        time_textView = findViewById(R.id.e_time);
        edit_describe = findViewById(R.id.e_describe);
        alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

        id = Integer.parseInt(getIntent().getExtras().get(TaskDBHelper.COLUMN_ID).toString());

        if (id == -1) {
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            date_textView.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            time_textView.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            Intent i = getIntent();
            _task = (Task) i.getSerializableExtra("tasks");
            edit_describe.setText(_task.getTask());
            date_textView.setText(_task.getDate());
            time_textView.setText(_task.getTime());
        }

        /*
         * display date picker
         */
        date_textView.setOnClickListener(v ->
        {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            d_picker = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> date_textView.setText(String.format("%02d-%02d-%04d", dayOfMonth, (monthOfYear + 1), year1)), year, month, day);
            d_picker.show();
        });

        /*
         * display time picker
         */
        time_textView.setOnClickListener(v ->
        {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            t_picker = new TimePickerDialog(this, (tp, sHour, sMinute) -> time_textView.setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, true);
            t_picker.show();
        });

        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTask:
                if (edit_describe.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.input_placeholder_descibe), Toast.LENGTH_LONG).show();
                    return;
                }

                Task task = new Task();
                task.setTask(edit_describe.getText().toString());
                task.setDate(date_textView.getText().toString());
                task.setTime(time_textView.getText().toString());

                if (TaskDBHelper.createTask(taskDBHelper.getWritableDatabase(), task) != -1)
                    Toast.makeText(this, getText(R.string.success), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, getText(R.string.faild), Toast.LENGTH_LONG).show();

                finish();
                break;
            case R.id.btnEdit:
                if (edit_describe.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.input_placeholder_descibe), Toast.LENGTH_LONG).show();
                    return;
                }

                _task.setTask(edit_describe.getText().toString());
                _task.setDate(date_textView.getText().toString());
                _task.setTime(time_textView.getText().toString());
                if (TaskDBHelper.updateTask(taskDBHelper.getReadableDatabase(), _task) != -1)
                    Toast.makeText(this, getString(R.string.alertUpdateSuccess), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, getString(R.string.alertUpdateFaild), Toast.LENGTH_LONG).show();
                taskDBHelper.close();
                finish();
                break;
            case R.id.btnDelete:
                alert.setTitle(getString(R.string.alertTitle));
                alert.setMessage(getString(R.string.alertMessage));
                alert.setIcon(R.drawable.ic_baseline_delete_forever_24);
                alert.setPositiveButton(getString(R.string.alertDelete), (dialog, which) ->
                        {
                            TaskDBHelper.deleteTask(taskDBHelper.getReadableDatabase(), id);
                            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccess), Toast.LENGTH_LONG).show();
                            finish();
                        }
                );
                alert.setNegativeButton(getString(R.string.alertCancel), (dialog, which) -> Toast.makeText(getApplicationContext(), getString(R.string.alertCancel), Toast.LENGTH_LONG).show());
                alert.show();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}