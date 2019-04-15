package com.android.instaposts;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;
    private EditText loginEmail;
    private EditText loginPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar loginUserProgress;
    private RelativeLayout mainContent;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loginButton = (Button) findViewById(R.id.btn_login);
        registerButton = (Button) findViewById(R.id.btn_register);
        loginEmail = (EditText) findViewById(R.id.email);
        loginPassword = (EditText) findViewById(R.id.password);
        loginUserProgress = (ProgressBar) findViewById(R.id.login_user_progress);
        mainContent = (RelativeLayout) findViewById(R.id.main_content_login);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        loginUserProgress.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.btn_login:
                loginUser();
                break;
            case R.id.btn_register:
                registerUser();
                break;
        }
    }

    private void loginUser() {
        if (!loginEmail.getText().toString().isEmpty() && !loginPassword.getText().toString().isEmpty()) {
            makeFieldsNonEditable();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                openDashboard();
                            } else {
                                makeFieldsEditable();
                                Toast.makeText(getApplicationContext(), "Email or Password Incorrect", Toast.LENGTH_SHORT).show();
                            }
                            loginUserProgress.setVisibility(View.GONE);
                        }
                    });
        } else {
            loginUserProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        loginUserProgress.setVisibility(View.GONE);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loginEmail.setText(data.getStringExtra("email"));
            loginPassword.setText(data.getStringExtra("password"));
            Toast.makeText(this, "Registration Successful. Please Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeFieldsNonEditable() {
        loginEmail.setEnabled(false);
        loginPassword.setEnabled(false);
        loginButton.setEnabled(false);
    }

    private void makeFieldsEditable() {
        loginEmail.setEnabled(true);
        loginPassword.setEnabled(true);
        loginButton.setEnabled(true);
    }

    private void openDashboard() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
