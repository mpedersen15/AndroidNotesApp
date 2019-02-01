package com.example.matt3865.notesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity {

    EditText notesEditText;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();

        String newNote  = notesEditText.getText().toString();

        int resultCode = RESULT_OK;

        if (newNote.matches("")) {
            newNote = "empty";
            resultCode = RESULT_CANCELED;
        }

        intent.putExtra("note", newNote);
        setResult(resultCode, intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        notesEditText = findViewById(R.id.noteEditText);

        Intent intent = getIntent();

        String note = intent.getStringExtra("note");

        notesEditText.setText(note);
    }
}
