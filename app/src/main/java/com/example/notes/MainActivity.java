package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    SharedPreferences sharedPreferences;

    public void openNotesActivity(){
        Intent intent = new Intent(getApplicationContext(), Note.class);
        startActivity(intent);
    }

    public void openNotesActivity(int i) {
        Intent intent = new Intent(getApplicationContext(), Note.class);
        intent.putExtra("noteId", i);
        startActivity(intent);
    }
    //creates our menuInflater that takes in the menu item. Creates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //executes when menu item is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        // switch to the new note activity.
        if(item.getItemId() == R.id.add_New_Note) {
            openNotesActivity();
            return true;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        ListView myListView = findViewById(R.id.myListView);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if (set == null){
            notes.add("Example Note");
        }
        else {
            notes = new ArrayList(set);
        }

        //create an arrayadapter which adapts our ListView to the arrayList notes its static so Note.java is apart of it
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Item pressed", notes.get(position));
                openNotesActivity(position);
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                final int deletePos = position;
                //Log.i("Item Long pressed", notes.get(position));
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete note?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(deletePos);
                                //always notify the arrayAdapter when there is a change to the array.
                                arrayAdapter.notifyDataSetChanged();

                                //update your notes and store
                                //to convert our notes array into preffered preferenece, we use a set since order doesn't really matter.
                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null).show();

                return true;
            }
        });

        /*
        ArrayList<String> friends = new ArrayList<>();
        friends.add("Steve");
        friends.add("Rodger");
        friends.add("Stan");
        try {
            sharedPreferences.edit().putString("friends", ObjectSerializer.serialize(friends)).apply();
            Log.i("friends", ObjectSerializer.serialize(friends));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> newfriends = new ArrayList<>();

        try {
            newfriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("new Friends", newfriends.toString());

        final ArrayList<String> newfriends2 = newfriends;
        //ListView
        ListView myListView = findViewById(R.id.myListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newfriends2);
        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Item pressed", newfriends2.get(position));
                //SharedPreferences sharedPreferences1 = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences1.edit();
                //store item index and note for editing

                openNotesActivity();
            }
        });


        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                Log.i("Item Long pressed", newfriends2.get(position));
                return true;
            }
        });
        /*

    */

    }
}

