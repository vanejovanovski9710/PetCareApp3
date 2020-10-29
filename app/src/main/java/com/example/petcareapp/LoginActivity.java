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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPassword;
    private Button btnLogin,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);

        btnLogin = findViewById(R.id.btnLoginLogin);
        btnSignUp = findViewById(R.id.btnSignUpLogin);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
        if (currentUser != null) {

            // Transition to the next activity

            Intent intent = new Intent(LoginActivity.this, LostPetsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignUpRegister) {
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.btnLoginLogin) {
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                   //     Log.d(TAG, "signInWithEmail:success");
                        //FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Authentication succesfull.",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this,LostPetsActivity.class);
                        startActivity(intent);

                 //       updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                     //   Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
               //         updateUI(null);
                    }
                }
            });
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