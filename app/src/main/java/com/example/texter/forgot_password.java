package com.example.texter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class forgot_password extends AppCompatActivity {

    private EditText email;
    private TextInputLayout email_layout;
    private AppCompatButton reset_password;
    private ALoadingDialog aLoadingDialog;
    private FirebaseAuth authProfile;
    private String text_email;
    private final static String TAG = "forgot_password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reset_password = findViewById(R.id.reset_password);
        aLoadingDialog =  new ALoadingDialog(this);
        authProfile = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_string);
        email_layout = findViewById(R.id.email);

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_email = email.getText().toString();
                text_email=trimStartEndSpaces(text_email);
                if(validateEmail(text_email))
                {
                    aLoadingDialog.show();
                    resetPassword(text_email);
                }
            }
        });
    }

    private void resetPassword(String textEmail) {
        authProfile.sendPasswordResetEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    aLoadingDialog.cancel(); //stopping the progress bar
                    Toast.makeText(forgot_password.this, "please check your email", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(forgot_password.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        email_layout.setError("User does not exists or is no longer valid , please register again");
                        email_layout.requestFocus();
                    }
                    catch(Exception e)
                    {
                        Log.e(TAG ,e.getMessage());
                        Toast.makeText(forgot_password.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    aLoadingDialog.cancel(); //stopping the progress bar
                }
            }
        });
    }
    public static String trimStartEndSpaces(String input) {
        int start = 0;
        int end = input.length() - 1;

        // Trim leading spaces
        while (start <= end && Character.isWhitespace(input.charAt(start))) {
            start++;
        }

        // Trim trailing spaces
        while (end >= start && Character.isWhitespace(input.charAt(end))) {
            end--;
        }

        return input.substring(start, end + 1);
    }
    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            email_layout.setError("Email address is required");
            email_layout.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("Invalid email address");
            email_layout.requestFocus();
            return false;
        } else {
            email_layout.setError(null);
            return true;
        }
    }
}
