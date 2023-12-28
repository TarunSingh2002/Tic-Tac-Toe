package com.example.texter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class updatePassword extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private TextInputLayout  new_password_layout , old_password_layout , new_confirm_password_layout;
    private EditText editTextPwdCurr , editTextPwdNew , editTextPwdConfirmNew;
    private AppCompatButton buttonChangePwd , buttonReAuthtication;
    private String userPwdCurr;
    private TextView textViewEmail , textViewAuth;
    private ALoadingDialog aLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        aLoadingDialog = new ALoadingDialog(this);
        textViewEmail=findViewById(R.id.email);
        editTextPwdCurr=findViewById(R.id.password_string);
        old_password_layout=findViewById(R.id.password);
        buttonChangePwd=findViewById(R.id.ok);
        textViewAuth=findViewById(R.id.notAuth);
        new_password_layout=findViewById(R.id.new_password);
        new_confirm_password_layout=findViewById(R.id.new_ConfirmPassword);
        editTextPwdNew=findViewById(R.id.password_new_string);
        editTextPwdConfirmNew=findViewById(R.id.confirmPassword_new_string);
        buttonReAuthtication=findViewById(R.id.authentication);

        editTextPwdNew.setEnabled(false);
        editTextPwdConfirmNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);
        buttonChangePwd.setBackgroundResource(R.drawable.button_style_five);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        textViewEmail.setText(firebaseUser.getEmail());
        if(firebaseUser.equals("")) {
            Toast.makeText(this, "Something went worng", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(updatePassword.this, profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }else{
            reAuthentication(firebaseUser);
        }

    }

    private void reAuthentication(FirebaseUser firebaseUser) {
        buttonReAuthtication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurr=editTextPwdCurr.getText().toString();
                userPwdCurr=trimStartEndSpaces(userPwdCurr);
                if(validatePassword(userPwdCurr , old_password_layout))
                {
                    aLoadingDialog.show();
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                aLoadingDialog.cancel();
                                editTextPwdConfirmNew.setEnabled(true);
                                editTextPwdNew.setEnabled(true);
                                editTextPwdCurr.setEnabled(false);
                                buttonChangePwd.setEnabled(true);
                                buttonReAuthtication.setEnabled(false);
                                buttonChangePwd.setBackgroundResource(R.drawable.button_style_three);
                                buttonReAuthtication.setBackgroundResource(R.drawable.button_style_five);
                                textViewAuth.setText("You are authenticated/verified now");
                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }
                            else{
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(updatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                aLoadingDialog.cancel();
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userNewPwd = editTextPwdNew.getText().toString();
        String userNewConfirmPwd = editTextPwdConfirmNew.getText().toString();
        userNewConfirmPwd=trimStartEndSpaces(userNewConfirmPwd);
        userNewPwd=trimStartEndSpaces(userNewPwd);
        if(userNewPwd.equals(userPwdCurr))
        {
            new_password_layout.setError("Same Password");
            new_password_layout.requestFocus();
        }
        else if( validatePassword(userNewPwd , new_password_layout)  && validateConfirmPassword(userNewConfirmPwd , userNewPwd , new_confirm_password_layout))
        {
            aLoadingDialog.show();
            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        aLoadingDialog.cancel();
                        Intent intent = new Intent(updatePassword.this, profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        try{
                            throw task.getException();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(updatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        aLoadingDialog.cancel();
                    }
                }
            });
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
    private boolean validatePassword(String val , TextInputLayout layout) {
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
            layout.setError("Field cannot be empty");
            layout.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            layout.setError("Password is too weak");
            layout.requestFocus();
            return false;
        }
        else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateConfirmPassword(String val, String val2 , TextInputLayout layout) {
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
            layout.setError("Password Field cannot be empty");
            layout.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            layout.setError("Password is too weak");
            layout.requestFocus();
            return false;
        } else if (!val2.equals(val)) {
            layout.setError("Password confirmation is required");
            layout.requestFocus();
            return false;
        } else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }
}