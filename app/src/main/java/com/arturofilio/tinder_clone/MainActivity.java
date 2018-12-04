package com.arturofilio.tinder_clone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    private Context mContext = MainActivity.this;

    FirebaseAuth mAuth;

    Button mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mLogoutBtn = (Button) findViewById(R.id.btn_logout);

        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
//                makeTost(MainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
//                makeTost(MainActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "onAdapterAboutToEmpty: ");
                i++;

            }

            @Override
            public void onScroll(float v) {
                
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
//                makeTost(MainActivity.this, "Clicked!");
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });


        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(mContext, WelcomeActivity.class);
                Toast.makeText(mContext, "You were logged out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }


    static void makeTost(Context ctx, String s) {
        Toast.makeText(ctx,s, Toast.LENGTH_SHORT).show();
    }
}
