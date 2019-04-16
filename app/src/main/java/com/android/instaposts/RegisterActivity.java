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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DUPLICATE_USER_MESSAGE = "User with this email already exist.";
    public static final String USERS_KEY = "users";
    public static final String REQUIRED_FIELDS_MESSAGE = "All fields are required";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
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
            registerNewUser();
        } else {
            registerUserProgress.setVisibility(View.GONE);
            Toast.makeText(this, REQUIRED_FIELDS_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void registerNewUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(registrationEmail.getText().toString(), registrationPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), DUPLICATE_USER_MESSAGE, Toast.LENGTH_LONG).show();
                    makeFieldsEditable();
                } else if (task.isSuccessful()) {
                    final User newUser = new User(name.getText().toString(), nickname.getText().toString(), registrationEmail.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseDatabase.getInstance().getReference().child(USERS_KEY).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            openLoginActivity();
                        }
                    });
                }
            }
        });
    }


    private void openLoginActivity() {
        registerUserProgress.setVisibility(View.GONE);
        Intent returnToLogin = new Intent();
        returnToLogin.putExtra(EMAIL_KEY, registrationEmail.getText().toString());
        returnToLogin.putExtra(PASSWORD_KEY, registrationPassword.getText().toString());
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

    private void makeFieldsEditable() {
        registrationEmail.setEnabled(true);
        registrationPassword.setEnabled(true);
        nickname.setEnabled(true);
        name.setEnabled(true);
        register.setEnabled(true);
        registerUserProgress.setVisibility(View.GONE);
    }


}
