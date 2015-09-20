package com.example.brandon.checkpoints;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class Leaderboards extends ActionBarActivity {
    ImageView imageView;
    FileInputStream fis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_leaderboards);
        imageView = (ImageView)findViewById(R.id.trophyView);
        imageView.setImageResource(R.mipmap.trophy);

        setupStats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupStats(){
        Firebase ref = new Firebase("https://checkpoints.firebaseio.com");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView view = (TextView)findViewById(R.id.leaderstats);
                String des="";
                    for (DataSnapshot child : snapshot.getChildren()) {
                        des+=child.getKey()+":\n";
                        for (DataSnapshot user : child.getChildren()) {
                            String time = user.child("Time").getValue(String.class);
                            des+="[ Name = " + user.getKey() + "     ||      Time = " + time + " ]\n";
                           // System.out.println("diff: " + child.getKey() + " a user " + user.getKey() + " time: " + time);
                        }
                        des+="\n";
                    }
                view.setText(des);
                view.setTextColor(Color.parseColor("#FFFFFF"));
                }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
