package com.android.instaposts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private EditText registrationEmail;
    private EditText registrationPassword;
    private EditText name;
    private EditText nickname;
    private ProgressBar registerUserProgress;
    private RelativeLayout mainContentRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        register = (Button) findViewById(R.id.register_user);
        registrationEmail = (EditText) findViewById(R.id.user_email);
        registrationPassword = (EditText) findViewById(R.id.user_password);
        name = (EditText) findViewById(R.id.name);
        nickname = (EditText) findViewById(R.id.nickname);
        registerUserProgress = (ProgressBar) findViewById(R.id.register_user_progress);
        mainContentRegister = (RelativeLayout) findViewById(R.id.main_content_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUserProgress.setVisibility(View.VISIBLE);
        if (!registrationEmail.getText().toString().isEmpty() && !registrationPassword.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !nickname.getText().toString().isEmpty()) {
            makeFieldsNonEditable();
            User newUser = new User(name.getText().toString(), nickname.getText().toString(), registrationEmail.getText().toString(), registrationPassword.getText().toString());
            registerNewUser(newUser);
        } else {
            registerUserProgress.setVisibility(View.GONE);
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerNewUser(final User newUser) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(registrationEmail.getText().toString(), registrationPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        openLoginActivity(newUser);
                    }
                });
            }
            }
        });
    }

    private void openLoginActivity(User newUser) {
        registerUserProgress.setVisibility(View.GONE);
        Intent returnToLogin = new Intent();
        returnToLogin.putExtra("email", newUser.getEmail());
        returnToLogin.putExtra("password", newUser.getPassword());
        setResult(Activity.RESULT_OK, returnToLogin);
        finish();
    }

    private void makeFieldsNonEditable() {
        registrationEmail.setEnabled(false);
        registrationPassword.setEnabled(false);
        nickname.setEnabled(false);
        name.setEnabled(false);
        register.setEnabled(false);
    }

}
