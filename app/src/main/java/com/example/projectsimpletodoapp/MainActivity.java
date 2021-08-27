package com.example.projectsimpletodoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TASK_TEXT = "task_text";
    public static final String KEY_TASK_POSITION = "task_position";
    public static final int EDIT_TEXT_CODE = 20;

    private List<String> tasks;
    private EditText editText;
    private TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.addText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        loadTasks();

        TasksAdapter.OnLongClickListener onLongClickListener = position -> {
            //Delete the task from list
            tasks.remove(position);
            //notify the adapter
            tasksAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Task has removed", Toast.LENGTH_SHORT).show();
            saveTasks();
        };

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //handle the result of the edit activity
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        String taskText = result.getData().getStringExtra(KEY_TASK_TEXT);
                        //extract the original position of the edited item from the position key
                        int position = result.getData().getExtras().getInt(KEY_TASK_POSITION);

                        //update the model at the right position with new item text
                        tasks.set(position, taskText);
                        //notify the adapter
                        tasksAdapter.notifyItemChanged(position);
                        //persist the changes
                        saveTasks();
                        Toast.makeText(getApplicationContext(), "Task updated successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.w("MainActivity", "Unknown call to onActivityResult");
                    }

                }
        );


        TasksAdapter.OnClickListener onClickListener = position -> {
            Log.d("MainActivity", "Single click at position: " + position);
            //create new activity
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            //pass the data being edited
            i.putExtra(KEY_TASK_TEXT, tasks.get(position));
            i.putExtra(KEY_TASK_POSITION, position);
            //display the activity
            activityResultLauncher.launch(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            //startActivityForResult(i, EDIT_TEXT_CODE);

        };

        tasksAdapter = new TasksAdapter(tasks, onLongClickListener, onClickListener);
        recyclerView.setAdapter(tasksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            if(editText.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this, "Add new todo task", Toast.LENGTH_SHORT).show();
            }else {
                String todoTask = editText.getText().toString();
                //Add task to the list
                tasks.add(todoTask);
                //Notify the adapter that an task is inserted
                tasksAdapter.notifyItemInserted(tasks.size() - 1);
                editText.setText("");
                Toast.makeText(getApplicationContext(), "Task has added", Toast.LENGTH_SHORT).show();
                saveTasks();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //loads data from the file
    private void loadTasks(){
        try {
            tasks = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error loading/reading tasks", e);
            tasks = new ArrayList<>();
        }
    }
    //writes new data to the file
    private void saveTasks(){
        try {
            FileUtils.writeLines(getDataFile(), tasks);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving/writing tasks", e);
        }
    }

}