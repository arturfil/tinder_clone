package com.arturofilio.tinder_clone;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;
    private Context mContext = MainActivity.this;
    private String currentUId;
    private DatabaseReference usersDb;

    // variables for the pageAdapter
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    ListView listView;
    List<Cards> rowItems;

    FirebaseAuth mAuth;

    Button mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkGender();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.profile_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.main_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.chat_icon);

        mViewPager.setCurrentItem(1);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

//        mLogoutBtn = (Button) findViewById(R.id.btn_logout);

        rowItems = new ArrayList<Cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(oppositeGender).child(userId).child("connections").child("like")
                        .child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                usersDb.child(oppositeGender).child(userId).child("connections").child("nope")
                        .child(currentUId).setValue(true);
                
                isConnection(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {


            }

            @Override
            public void onScroll(float v) {
                
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });


        /*mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(mContext, WelcomeActivity.class);
                Toast.makeText(mContext, "You were logged out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });*/
    }

    private void setupViewPager(ViewPager mViewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SettingsFragment());
        adapter.addFragment(new CardsFragment());
        adapter.addFragment(new ChatFragment());
        mViewPager.setAdapter(adapter);

    }

    private void isConnection(String userId) {

        DatabaseReference currentUserCopnnectionsDb = usersDb.child(userGender).child(currentUId)
                .child("connections").child("like").child(userId);

        currentUserCopnnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(mContext, "A match has been made!", Toast.LENGTH_SHORT).show();
                    usersDb.child(oppositeGender).child(dataSnapshot.getKey()).child("connections")
                            .child("matches").child(currentUId).setValue(true);
                    usersDb.child(userGender).child(currentUId).child("connections")
                            .child("matches").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void getOppositeGender() {

        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeGender);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()
                        && !dataSnapshot.child("connections").child("nope").hasChild(currentUId)
                        && !dataSnapshot.child("connections").child("like").hasChild(currentUId)) {

                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString());
                    rowItems.add(item);

                    arrayAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String userGender;
    private String oppositeGender;

    public void checkGender() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Male");

        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals(user.getUid())) {
                        userGender = "Male";
                        oppositeGender = "Female";
                        getOppositeGender();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Female");

        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userGender = "Female";
                    oppositeGender = "Male";
                    getOppositeGender();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
