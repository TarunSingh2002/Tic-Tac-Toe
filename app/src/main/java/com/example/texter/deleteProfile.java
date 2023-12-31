package com.example.texter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class deleteProfile extends AppCompatActivity {

    private TextView textViewEmail , textViewAuth;
    private EditText editTextPassword;
    private TextInputLayout textInputLayoutPassword;
    private AppCompatButton buttonDeleteProfile , buttonAuthenticate;
    private ALoadingDialog aLoadingDialog;
    private LinearLayout linearLayoutDeleteProfile;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private String userPwdCurr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);
        aLoadingDialog=new ALoadingDialog(this);
        textViewEmail=findViewById(R.id.textViewEmail);
        textViewAuth=findViewById(R.id.textViewAuth);
        editTextPassword=findViewById(R.id.editViewPassword);
        textInputLayoutPassword=findViewById(R.id.textInputLayoutPassword);
        buttonDeleteProfile=findViewById(R.id.buttonDeleteProfile);
        buttonAuthenticate=findViewById(R.id.buttonAntication);
        linearLayoutDeleteProfile=findViewById(R.id.linearLayoutDeleteProfile);

        buttonDeleteProfile.setEnabled(false);
        linearLayoutDeleteProfile.setBackgroundColor(Color.parseColor("#808080"));

        authProfile=FirebaseAuth.getInstance();
        firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(deleteProfile.this, profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        else {
            textViewEmail.setText(firebaseUser.getEmail());
            reAuthentication(firebaseUser);
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
    private void reAuthentication(FirebaseUser firebaseUser) {
        buttonAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurr=editTextPassword.getText().toString();
                userPwdCurr=trimStartEndSpaces(userPwdCurr);
                if(validatePassword(userPwdCurr , textInputLayoutPassword))
                {
                    aLoadingDialog.show();
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                aLoadingDialog.cancel();
                                editTextPassword.setEnabled(false);
                                buttonDeleteProfile.setEnabled(true);
                                buttonAuthenticate.setEnabled(false);
                                linearLayoutDeleteProfile.setBackgroundColor(Color.parseColor("#FED36A"));
                                buttonAuthenticate.setBackgroundResource(R.drawable.button_style_five);
                                textViewAuth.setText("You are authenticated/verified now");
                                buttonDeleteProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showAlertDialog();
                                    }
                                });
                            }
                            else{
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(deleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                aLoadingDialog.cancel();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(deleteProfile.this);
        builder.setTitle("Delete User and Related Data?");
        builder.setMessage("Do you really want to delete your profile and related data? This action is irreversible!");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteUserDetails(firebaseUser);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void deleteUser() {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    authProfile.signOut();
                    Intent intent = new Intent(deleteProfile.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    try{
                        throw task.getException();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(deleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                aLoadingDialog.cancel();
            }
        });
    }

    private void DeleteUserDetails(FirebaseUser firebaseUser) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User Details");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(deleteProfile.this, "Something went worong", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReferenceTwo = FirebaseDatabase.getInstance().getReference("User Avatar");
        databaseReferenceTwo.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteUser();
                Toast.makeText(deleteProfile.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(deleteProfile.this, "Something went worong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}