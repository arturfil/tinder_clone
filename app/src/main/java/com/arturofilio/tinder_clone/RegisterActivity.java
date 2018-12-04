package com.arturofilio.tinder_clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmail;
    EditText mName;
    EditText mPassword;
    EditText mPasswordConfirm;
    RadioGroup mGender;
    Button btnRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = (EditText) findViewById(R.id.edt_email);
        mName = (EditText) findViewById(R.id.edt_name);
        mPassword = (EditText) findViewById(R.id.edt_password);
        mPasswordConfirm = (EditText) findViewById(R.id.edt_password2);
        mGender = (RadioGroup) findViewById(R.id.gender_selection);
        btnRegister = (Button) findViewById(R.id.btn_register);

        // Initiate Firebase
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUpUser();


            }
        });
    }

    private void signUpUser() {



        String emailField      = mEmail.getText().toString();
        final String nameField = mName.getText().toString();
        String password        = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(emailField) || TextUtils.isEmpty(nameField) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(passwordConfirm)) {

            Toast.makeText(this, "You need to fill all the fields",
                    Toast.LENGTH_SHORT).show();
            return;

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, "Password must contain at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.equals(passwordConfirm)) {
            mAuth.createUserWithEmailAndPassword(emailField,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        int selectedId = mGender.getCheckedRadioButtonId();
                        final RadioButton radioButton = (RadioButton) findViewById(selectedId);

                        if (radioButton.getText() == null) {
                            Toast.makeText(RegisterActivity.this, "Please select a Gender option", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance()
                                .getReference().child("Users").child(radioButton.getText().toString())
                                .child(userId).child("name");

                        currentUserDb.setValue(nameField);

                        Toast.makeText(RegisterActivity.this, "Successful Registration!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Oops, something went wrong, please try again",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } else {
            Toast.makeText(this, "Passwords must be the same", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
