package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        button = (Button) findViewById(R.id.mainButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openNotesActivity();
            }
        });

        ArrayList<String> friends = new ArrayList<>();
        friends.add("Steve");
        friends.add("Rodger");
        friends.add("Stan");
        try {
            ;
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();
            Log.i("friends", ObjectSerializer.serialize(friends));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        ArrayList<String> newfriends = new ArrayList<>();

        try {
            newfriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.i("new Friends", newfriends.toString());
    }
    public void openNotesActivity() {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }

}

