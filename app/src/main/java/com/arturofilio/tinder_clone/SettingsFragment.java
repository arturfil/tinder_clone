package com.arturofilio.tinder_clone;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

public class SettingsFragment extends Fragment{

    private static final String TAG = "SettingsFragment";
    private Context mContext = this.getContext();

    private Button mLogoutBtn;
    private TextView mProfileInfo, mUsername, mUserPhoneNumber;
    private ImageView mProfileImage, mSettings;

    private String currentUId;
    private String userId, name, phone, profileImageUrl;


    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);

        mAuth = FirebaseAuth.getInstance();

        //currentUId = mAuth.getCurrentUser().getUid();
        userId = mAuth.getCurrentUser().getUid();

        /*mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userGender).child(userId);*/

        mUsername = (TextView) view.findViewById(R.id.profile_info);
        mLogoutBtn = (Button) view.findViewById(R.id.btn_logout);
        mSettings = (ImageView) view.findViewById(R.id.btn_settings);

        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                intent.putExtra("userGender", userGender);
                startActivity(intent);
            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                Toast.makeText(getContext(), "You were logged out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

//        getUserInfo();
        checkGender();

        return view;
    }

    private void getUserInfo() {

        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name") != null) {
                        name = map.get("name").toString();
                        mUsername.setText(name);

                    }

                    if(map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        mUserPhoneNumber.setText(name);

                    }

                    /*if(map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);

                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getOppositeGender() {

        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeGender);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()
                        && !dataSnapshot.child("connections").child("nope").hasChild(userId)
                        && !dataSnapshot.child("connections").child("like").hasChild(userId)) {

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
