package com.arturofilio.tinder_clone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment{

    private static final String TAG = "SettingsFragment";
    private Context mContext = this.getContext();

    private Button mLogoutBtn;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);


        mLogoutBtn = (Button) view.findViewById(R.id.btn_logout);

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(mContext, WelcomeActivity.class);
                Toast.makeText(mContext, "You were logged out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return view;
    }
}
