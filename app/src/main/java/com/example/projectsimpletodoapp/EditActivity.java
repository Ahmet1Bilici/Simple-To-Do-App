package com.example.projectsimpletodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        editText = findViewById(R.id.editTask);
        saveButton = findViewById(R.id.updateButton);

        getSupportActionBar().setTitle("Edit Task");

        editText.setText(getIntent().getStringExtra(MainActivity.KEY_TASK_TEXT));

        saveButton.setOnClickListener(v -> {
            Intent i = new Intent();

            i.putExtra(MainActivity.KEY_TASK_TEXT, editText.getText().toString());
            i.putExtra(MainActivity.KEY_TASK_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_TASK_POSITION));

            setResult(RESULT_OK, i);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

    }
}