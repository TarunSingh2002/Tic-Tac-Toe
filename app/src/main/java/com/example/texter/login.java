package com.example.texter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private TextView a,b,forgot_password;
    private EditText email_string , password_string;
    private TextInputLayout email , password;
    private AppCompatButton login;
    private ALoadingDialog aLoadingDialog;
    private FirebaseAuth authProfile;
    private final static String TAG = "login";
    private String text_email="" , text_pwd ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        a=findViewById(R.id.button1);
        b=findViewById(R.id.button2);
        login = findViewById(R.id.login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        email_string=findViewById(R.id.email_string);
        password_string=findViewById(R.id.password_string);
        aLoadingDialog = new ALoadingDialog(this);
        authProfile = FirebaseAuth.getInstance();
        forgot_password =findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, forgot_password.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Sign_up_user.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Sign_up_user.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_string.getText().toString() , pwd = password_string.getText().toString();
                email=trimStartEndSpaces(email);
                pwd=trimStartEndSpaces(pwd);
                text_email=email;
                text_pwd=pwd;
                if(validateEmail(email) && validatePassword(pwd))
                {
                    aLoadingDialog.show();
                    loginUser(email,pwd);
                }
            }
        });
    }

    private void loginUser(String email_string, String pwd) {
        authProfile.signInWithEmailAndPassword(email_string,pwd).addOnCompleteListener(com.example.texter.login.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    // showing a dialog box if email not verified
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        Intent intent = new Intent(login.this, MainActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                    aLoadingDialog.cancel(); //stopping the progress bar
                }
                else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        email.setError("User does not exits or no longer valid. Please register again");
                        email.requestFocus();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        password.setError("Invalid credentials, Kindly, check and re-enter.");
                        password.requestFocus();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG ,e.getMessage());
                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    aLoadingDialog.cancel(); //stopping the progress bar
                }
            }
        });

    }

    private void showAlertDialog() {  // showing dialog box for email verification
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");
        // open Email App if User click/taps Continue button
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // To email app in new window and not within our app
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();
        //Show the  Alert Dialog
        alertDialog.show();
    }

    private boolean validateEmail(String val) {
        if (val.isEmpty())
        {
            email.setError("Email address is required");
            email.requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches())
        {
            email.setError("Invalid email address");
            email.requestFocus();
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }

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
    private boolean validatePassword(String val) {
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            password.requestFocus();
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    //check already loggedIn or not
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null ){
            Intent intent = new Intent(login.this, MainActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}