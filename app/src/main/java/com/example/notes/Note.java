package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class Note extends AppCompatActivity {
    int noteId;
    public static final String EXTRA_TEXT = "com.example.notes.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        EditText editText = (EditText) findViewById(R.id.noteText);
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if(noteId != -1) {
            //to access notes from Main Activity, the arraylist needs to be static.
            editText.setText(MainActivity.notes.get(noteId));
        }
        else{
            //if -1 we need to create a new element for notes. Otherwise ArrayIndexOutofBounds (notes.get(-1)?!?!)
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
        }


        //add a editTextChanged listener so whenever there is a change, we update the array.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            // s is the sequence of changed characters
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //changing our notes will change our static arrayAdapter
                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                //saves whenever someone changes something
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                //to convert our notes array into preffered preferenece, we use a set since order doesn't really matter.
                HashSet<String> set = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }
    //updates our stored data when the back button is pressed.
    @Override
    public void onBackPressed(){
        Toast.makeText(this,"saved", Toast.LENGTH_SHORT).show();
        /*
        EditText editText = (EditText) findViewById(R.id.noteText);
        //String note = editText.getText().toString();
        Intent intent = getIntent();
        int noteId = intent.getIntExtra("noteId", -1);
        if(noteId != -1){
            //to access notes from Main Activity, the arraylist needs to be static.
            //editText.setText(MainActivity.notes.get(noteId));
        }
        */
        super.onBackPressed();
    }
}
