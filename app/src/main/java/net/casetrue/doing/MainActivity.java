package net.casetrue.doing;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TaskDBHelper taskDBHelper;
    ListView lstTasks;
    ArrayList<Task> tasks;
    TaskAdabter taskAdapter;
    Button btnNewTask;
    Notification.Builder notificatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstTasks = findViewById(R.id.lstTasks);
        taskDBHelper = new TaskDBHelper(this);
        btnNewTask = findViewById(R.id.btnNewTask);
        tasks = new ArrayList<>();
        notificatio = new Notification.Builder(this);

        btnNewTask.setOnClickListener(v ->
        {
            Intent i = new Intent(this, AddTask.class);
            i.putExtra(TaskDBHelper.COLUMN_ID, "-1");
            startActivity(i);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        tasks.clear();

        tasks = TaskDBHelper.getAllTasks(taskDBHelper.getReadableDatabase());

        taskAdapter = new TaskAdabter(getApplicationContext(), tasks);
        lstTasks.setAdapter(taskAdapter);

        lstTasks.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent i = new Intent(this, AddTask.class);
            i.putExtra(TaskDBHelper.COLUMN_ID, taskAdapter.getItemId(position));
            i.putExtra("tasks", tasks.get(position));
            startActivity(i);
        });
    }
}