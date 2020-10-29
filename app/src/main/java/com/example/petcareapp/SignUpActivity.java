package com.example.petcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText edtEmail,edtUsername,edtPassword;
    private Button btnLogin,btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = findViewById(R.id.edtEmailRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        edtUsername = findViewById(R.id.edtUsernameRegister);
        btnLogin = findViewById(R.id.btnLoginRegister);
        btnSignUp = findViewById(R.id.btnSignUpRegister);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentUser != null) {

            // Transition to the next activity
            Intent intent = new Intent(SignUpActivity.this,LostPetsActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignUpRegister) {
            mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Authentication succesfull.",
                                Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid())
                                .child("username").setValue(edtUsername.getText().toString());

                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(edtUsername.getText().toString()).build();
                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this,"Display name updated",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (v.getId() == R.id.btnLoginRegister) {
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }


    public void rootLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }
}