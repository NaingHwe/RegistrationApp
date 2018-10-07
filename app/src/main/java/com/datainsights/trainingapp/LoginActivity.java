package com.datainsights.trainingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    private final String TAG = LoginActivity.class.getSimpleName();
    EditText loginEtMail, loginEtPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btn_login);
        loginEtMail = findViewById(R.id.login_et_email);
        loginEtPassword = findViewById(R.id.login_et_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        if (TextUtils.isEmpty(loginEtMail.getText())) {
            loginEtMail.setError("Please type a mail");
            loginEtMail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(loginEtPassword.getText())) {
            loginEtPassword.setError("Please type a password");
            loginEtPassword.requestFocus();
            return;
        }
        loading(true);
        mAuth.signInWithEmailAndPassword(loginEtMail.getText().toString(), loginEtPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loading(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            doLoginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String errMsg = "Fail to login";
                            if (task.getException() != null) {
                                errMsg = task.getException().getMessage();
                            }
                            Toast.makeText(LoginActivity.this, errMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void doLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}
