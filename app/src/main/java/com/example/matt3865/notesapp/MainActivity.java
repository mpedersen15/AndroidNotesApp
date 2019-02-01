package com.example.matt3865.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView notesListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> notes;
    SharedPreferences sharedPref;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {


            Log.i("Activity Result", "Code: " + requestCode + " - " + data.getStringExtra("note"));

            String newNote = data.getStringExtra("note");

            if (requestCode == 9999) {
                notes.add(newNote);
            } else {
                notes.set(requestCode, newNote);
            }

            // TODO: save notes list to shared preferences
            updateSharedPreferences();

            arrayAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent navIntent = new Intent(getApplicationContext(), NoteEditActivity.class);

        navIntent.putExtra("note", "");

        startActivityForResult(navIntent, 9999);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        String serializedNotes = sharedPref.getString("notes", "");

        if (!serializedNotes.matches("")) {
            try {
                notes = (ArrayList<String>)ObjectSerializer.deserialize(serializedNotes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            notes = new ArrayList<>();

            notes.add("Feed snake");
            notes.add("Walk goose");
        }


        notesListView = findViewById(R.id.notesListView);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        notesListView.setAdapter(arrayAdapter);


        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Edit requested", "Need to bring user to Edit page");
                Intent navIntent = new Intent(getApplicationContext(), NoteEditActivity.class);

                navIntent.putExtra("note", notes.get(position));

                startActivityForResult(navIntent, position);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Delete requested", "Need to show an alert confirming!");

                showConfirmDialog(position);

                return true;
            }
        });
    }

    private void showConfirmDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note?")
                .setMessage("This will delete the note permanently")
                .setPositiveButton("Confirm Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Delete confirmed", "Need to delete note!" + position);

                        notes.remove(position);

                        arrayAdapter.notifyDataSetChanged();
                        updateSharedPreferences();
                        // TODO: update shared preferences
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateSharedPreferences() {
        try {

        sharedPref.edit().putString("notes", ObjectSerializer.serialize(notes)).commit();
        Log.i("Stored Notes: ", sharedPref.getString("notes", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
