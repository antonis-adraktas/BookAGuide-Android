package com.antonis.bookaguide;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGAPP="BookAGuide";

    private FirebaseAuth auth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        auth=FirebaseAuth.getInstance();
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this,RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {
        String email=mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();
        if (email.equals("") || password.equals("")){
            return;
        }
        Toast.makeText(this,"Login in progress...",Toast.LENGTH_SHORT).show();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(LOGAPP,"signInWithEmailAndPassword() onComplete "+task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.d(LOGAPP,"Problem signing in: "+task.getException());
                    showErrorDialog("There was a problem signing in");
                }else{
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                    Log.d(MainActivity.LOGAPP,auth.getCurrentUser().getUid()+"\n"+auth.getCurrentUser().getEmail());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.AD_UNIT_NAME, auth.getCurrentUser().getEmail());
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "Email/Password");
                    Log.d(LOGAPP,bundle.toString());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                    finish();
                    startActivity(intent);
                }
            }
        });


    }


    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
