package com.example.texter;
import androidx.core.content.ContextCompat;

import static android.view.View.MEASURED_HEIGHT_STATE_SHIFT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.transition.CircularPropagation;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {

    ImageView previousAvatarForStroke , currentAvatarForStroke;
    ImageView circularImageView,
            avatar1 , avatar2 , avatar3 , avatar4 , avatar5 , avatar6 , avatar7 , avatar8 , avatar9 , avatar10,
            avatar11 , avatar12 , avatar13 , avatar14 , avatar15 , avatar16 , avatar17 , avatar18 , avatar19 , avatar20 ,
            avatar21 , avatar22 , avatar23 , avatar24 , avatar25 , avatar26 , avatar27 , avatar28 , avatar29 , avatar30,
            avatar31 , avatar32 , avatar33 , avatar34 , avatar35 , avatar36 , avatar37 , avatar38 , avatar39 , avatar40,
            avatar41 , avatar42 , avatar43 , avatar44 , avatar45 , avatar46 , avatar47 , avatar48 , avatar49 , avatar50;
    private FirebaseAuth auth;
    AppCompatButton btn , updatePassword , btn2;
    private ALoadingDialog aLoadingDialog;
    private AppCompatTextView name_view , email_view , dob_view  , gender_view , number_view;
    private AppCompatButton name_change  , dob_change  , gender_change , number_change;
    private String name , email , dob  , gender , phone_number, new_number , new_name;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private String currentAvatar , previousAvatar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("User Details");
    private DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        aLoadingDialog = new ALoadingDialog(this);
        //button
        btn=findViewById(R.id.loginOut);
        btn2=findViewById(R.id.deleteProfile);
        name_change = findViewById(R.id.buttonEnd);
        updatePassword=findViewById(R.id.buttonEnd2);
        dob_change = findViewById(R.id.buttonEnd4);
        gender_change = findViewById(R.id.buttonEnd5);
        number_change = findViewById(R.id.buttonEnd6);
        //Textview
        name_view = findViewById(R.id.name);
        email_view = findViewById(R.id.email);
        dob_view  = findViewById(R.id.dob);
        gender_view  = findViewById(R.id.gender);
        number_view  = findViewById(R.id.number);
        //change button click listener
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, deleteProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, updatePassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        name_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_box, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(profile.this);
                dialogBuilder.setView(dialogView);
                // getting views
                AppCompatButton btn =dialogView.findViewById(R.id.ok);
                TextInputLayout layout =dialogView.findViewById(R.id.input_layout);
                AppCompatEditText editText = dialogView.findViewById(R.id.input);
                TextView title = dialogView.findViewById(R.id.title);
                // setting the data in dialog box
                editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                title.setText("Update Name");
                layout.setHint("User Name");
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
                // getting the data from edit text
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new_name = editText.getText().toString();
                        new_name=trimStartEndSpaces(new_name);
                        if(validateName(new_name).equalsIgnoreCase("empty"))
                        {
                            layout.setError("Field cannot be empty");
                            layout.requestFocus();
                        }
                        else if(validateName(new_name).equalsIgnoreCase("same"))
                        {
                            layout.setError("Name cannot be same as before");
                            layout.requestFocus();
                        }
                        else
                        {
                            aLoadingDialog.show();
                            name_view.setText(new_name);
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(new_name).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            aLoadingDialog.cancel();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        number_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_box, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(profile.this);
                dialogBuilder.setView(dialogView);
                // getting views
                AppCompatButton btn =dialogView.findViewById(R.id.ok);
                TextInputLayout layout =dialogView.findViewById(R.id.input_layout);
                AppCompatEditText editText = dialogView.findViewById(R.id.input);
                TextView title = dialogView.findViewById(R.id.title);
                // setting the data in dialog box
                title.setText("Update Phone Number");
                layout.setHint("User Phone Number");
                title.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
                // getting the data from edit text
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new_number = editText.getText().toString();
                        new_number=trimStartEndSpaces(new_number);
                        if(validatePhoneNo(new_number).equalsIgnoreCase("empty"))
                        {
                            layout.setError("Field cannot be empty");
                            layout.requestFocus();
                        }
                        else if(validatePhoneNo(new_number).equalsIgnoreCase("same"))
                        {
                            layout.setError("Name cannot be same as before");
                            layout.requestFocus();
                        }
                        else if(validatePhoneNo(new_number).equalsIgnoreCase("invalid"))
                        {
                            layout.setError("Invalid phone number");
                            layout.requestFocus();
                        }
                        else
                        {

                            aLoadingDialog.show();
                            //setting data in database
                            Map<String, Object> updateNumber = new HashMap<>();
                            updateNumber.put("number", new_number);
                            userRef.child(firebaseUser.getUid()).updateChildren(updateNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //do nothing
                                        number_view.setText(new_number);
                                        alertDialog.dismiss();
                                        aLoadingDialog.cancel();
                                    }
                                    else
                                    {
                                        try{
                                            throw task.getException();
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        aLoadingDialog.cancel();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        dob_change.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_box, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(profile.this);
                dialogBuilder.setView(dialogView);
                // getting views
                AppCompatButton btn =dialogView.findViewById(R.id.ok);
                TextInputLayout layout =dialogView.findViewById(R.id.input_layout);
                AppCompatEditText editText = dialogView.findViewById(R.id.input);
                TextView title = dialogView.findViewById(R.id.title);
                // setting the data in dialog box
                title.setText("Update DOB");
                layout.setHint("User DOB");
                editText.setInputType(InputType.TYPE_NULL);
                // Fetch the color from resources
                int goldenColor = ContextCompat.getColor(profile.this, R.color.golden);
                editText.setTextColor(goldenColor);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
                // setting calender on edit text
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calender =Calendar.getInstance();
                        int day=calender.get(Calendar.DAY_OF_MONTH);
                        int months=calender.get(Calendar.MONTH);
                        int year=calender.get(Calendar.YEAR);
                        //date picker dialog
                        picker = new DatePickerDialog(profile.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dateOfMonth) {
                                if(month+1 < 10)
                                    editText.setText(year+ "/0" +(month+1)+ "/" +dateOfMonth);
                                else
                                    editText.setText(year+ "/" +(month+1)+ "/" +dateOfMonth);
                            }
                        }, year , months , day);
                        picker.show();
                    }
                });
                // getting the data from edit text
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_dob = editText.getText().toString();
                        if(validateDOB(new_dob).equalsIgnoreCase("empty"))
                        {
                            layout.setError("Field cannot be empty");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("same"))
                        {
                            layout.setError("DOB cannot be same as before");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("wrong"))
                        {
                            layout.setError("Please enter DOB Correctl");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("under"))
                        {
                            layout.setError("You are under aged to use this app");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("invalid"))
                        {
                            layout.setError("Please enter a valid year");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("invalidM"))
                        {
                            layout.setError("Please enter a valid month");
                            layout.requestFocus();
                        }
                        else if(validateDOB(new_dob).equalsIgnoreCase("invalidD"))
                        {
                            layout.setError("Please enter a valid date");
                            layout.requestFocus();
                        }
                        else
                        {
                            aLoadingDialog.show();
                            //setting data in database
                            Map<String, Object> updateDOB = new HashMap<>();
                            updateDOB.put("dateOfBirth", new_dob); // Replace "ne_dob" with the field where the dob is stored
                            userRef.child(firebaseUser.getUid()).updateChildren(updateDOB).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //do nothing
                                        dob_view.setText(new_dob);
                                        alertDialog.dismiss();
                                        aLoadingDialog.cancel();
                                    }
                                    else
                                    {
                                        try{
                                            throw task.getException();
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        aLoadingDialog.cancel();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        gender_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_box_for_gender, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(profile.this);
                dialogBuilder.setView(dialogView);
                // getting views
                AppCompatButton btn =dialogView.findViewById(R.id.ok);
                TextView title = dialogView.findViewById(R.id.title);
                radioGroup=dialogView.findViewById(R.id.gender);
                if(gender.equalsIgnoreCase("male"))
                {
                    int i = R.id.male_dialogbox;
                    radioGroup.check(i);
                }
                else if(gender.equalsIgnoreCase("female"))
                {
                    int i = R.id.female_dialogbox;
                    radioGroup.check(i);
                }
                else {
                    int i = R.id.others_dialogbox;
                    radioGroup.check(i);
                }
                // setting the data in dialog box
                title.setText("Update Your Gender");
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
                // getting the data from edit text
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
                        radioButton = dialogView.findViewById(selectedGenderId);
                        String new_gender = radioButton.getText().toString();
                        if(new_gender.equalsIgnoreCase(gender))
                        {
                            Toast.makeText(profile.this, "Same Gender", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            aLoadingDialog.show();
                            //setting data in database
                            Map<String, Object> updateGender = new HashMap<>();
                            updateGender.put("gender", new_gender);
                            userRef.child(firebaseUser.getUid()).updateChildren(updateGender).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //do nothing
                                        gender_view.setText(new_gender);
                                        alertDialog.dismiss();
                                        aLoadingDialog.cancel();
                                        gender=new_gender;
                                    }
                                    else
                                    {
                                        try{
                                            throw task.getException();
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        aLoadingDialog.cancel();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        circularImageView = findViewById(R.id.circularImageView);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarPickerDialog();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(profile.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        if(firebaseUser == null)
        {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show();
        }
        else
        {
            aLoadingDialog.show();
            showUserProfile(firebaseUser);
        }
    }

    private void showAvatarPickerDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.avatar, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialogBuilder.setView(dialogView);
        switch(previousAvatar)
        {
            case "avatar1":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar1);
                break;
            case "avatar2":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar2);
                break;
            case "avatar3":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar3);
                break;
            case "avatar4":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar4);
                break;
            case "avatar5":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar5);
                break;
            case "avatar6":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar6);
                break;
            case "avatar7":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar7);
                break;
            case "avatar8":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar8);
                break;
            case "avatar9":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar9);
                break;
            case "avatar10":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar10);
                break;
            case "avatar11":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar11);
                break;
            case "avatar12":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar12);
                break;
            case "avatar13":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar13);
                break;
            case "avatar14":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar14);
                break;
            case "avatar15":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar15);
                break;
            case "avatar16":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar16);
                break;
            case "avatar17":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar17);
                break;
            case "avatar18":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar18);
                break;
            case "avatar19":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar19);
                break;
            case "avatar20":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar20);
                break;
            case "avatar21":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar21);
                break;
            case "avatar22":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar22);
                break;
            case "avatar23":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar23);
                break;
            case "avatar24":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar24);
                break;
            case "avatar25":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar25);
                break;
            case "avatar26":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar26);
                break;
            case "avatar27":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar27);
                break;
            case "avatar28":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar28);
                break;
            case "avatar29":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar29);
                break;
            case "avatar30":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar30);
                break;
            case "avatar31":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar31);
                break;
            case "avatar32":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar32);
                break;
            case "avatar33":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar33);
                break;
            case "avatar34":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar34);
                break;
            case "avatar35":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar35);
                break;
            case "avatar36":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar36);
                break;
            case "avatar37":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar37);
                break;
            case "avatar38":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar38);
                break;
            case "avatar39":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar39);
                break;
            case "avatar40":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar40);
                break;
            case "avatar41":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar41);
                break;
            case "avatar42":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar42);
                break;
            case "avatar43":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar43);
                break;
            case "avatar44":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar44);
                break;
            case "avatar45":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar45);
                break;
            case "avatar46":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar46);
                break;
            case "avatar47":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar47);
                break;
            case "avatar48":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar48);
                break;
            case "avatar49":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar49);
                break;
            case "avatar50":
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar50);
                break;
            default:
                previousAvatarForStroke = dialogView.findViewById(R.id.avatar6);
        }
        previousAvatarForStroke.setBackgroundResource(R.drawable.background_image_view);
        avatar1 = dialogView.findViewById(R.id.avatar1);
        avatar2 = dialogView.findViewById(R.id.avatar2);
        avatar3 = dialogView.findViewById(R.id.avatar3);
        avatar4 = dialogView.findViewById(R.id.avatar4);
        avatar5 = dialogView.findViewById(R.id.avatar5);
        avatar6 = dialogView.findViewById(R.id.avatar6);
        avatar7 = dialogView.findViewById(R.id.avatar7);
        avatar8 = dialogView.findViewById(R.id.avatar8);
        avatar9 = dialogView.findViewById(R.id.avatar9);
        avatar10 = dialogView.findViewById(R.id.avatar10);

        avatar11 = dialogView.findViewById(R.id.avatar11);
        avatar12 = dialogView.findViewById(R.id.avatar12);
        avatar13 = dialogView.findViewById(R.id.avatar13);
        avatar14 = dialogView.findViewById(R.id.avatar14);
        avatar15 = dialogView.findViewById(R.id.avatar15);
        avatar16 = dialogView.findViewById(R.id.avatar16);
        avatar17 = dialogView.findViewById(R.id.avatar17);
        avatar18 = dialogView.findViewById(R.id.avatar18);
        avatar19 = dialogView.findViewById(R.id.avatar19);
        avatar20 = dialogView.findViewById(R.id.avatar20);

        avatar21 = dialogView.findViewById(R.id.avatar21);
        avatar22 = dialogView.findViewById(R.id.avatar22);
        avatar23 = dialogView.findViewById(R.id.avatar23);
        avatar24 = dialogView.findViewById(R.id.avatar24);
        avatar25 = dialogView.findViewById(R.id.avatar25);
        avatar26 = dialogView.findViewById(R.id.avatar26);
        avatar27 = dialogView.findViewById(R.id.avatar27);
        avatar28 = dialogView.findViewById(R.id.avatar28);
        avatar29 = dialogView.findViewById(R.id.avatar29);
        avatar30 = dialogView.findViewById(R.id.avatar30);

        avatar31 = dialogView.findViewById(R.id.avatar31);
        avatar32 = dialogView.findViewById(R.id.avatar32);
        avatar33 = dialogView.findViewById(R.id.avatar33);
        avatar34 = dialogView.findViewById(R.id.avatar34);
        avatar35 = dialogView.findViewById(R.id.avatar35);
        avatar36 = dialogView.findViewById(R.id.avatar36);
        avatar37 = dialogView.findViewById(R.id.avatar37);
        avatar38 = dialogView.findViewById(R.id.avatar38);
        avatar39 = dialogView.findViewById(R.id.avatar39);
        avatar40 = dialogView.findViewById(R.id.avatar40);

        avatar41 = dialogView.findViewById(R.id.avatar41);
        avatar42 = dialogView.findViewById(R.id.avatar42);
        avatar43 = dialogView.findViewById(R.id.avatar43);
        avatar44 = dialogView.findViewById(R.id.avatar44);
        avatar45 = dialogView.findViewById(R.id.avatar45);
        avatar46 = dialogView.findViewById(R.id.avatar46);
        avatar47 = dialogView.findViewById(R.id.avatar47);
        avatar48 = dialogView.findViewById(R.id.avatar48);
        avatar49 = dialogView.findViewById(R.id.avatar49);
        avatar50 = dialogView.findViewById(R.id.avatar50);

        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar1);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar1";
            }
        });
        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar2);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar2";
            }
        });
        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar3);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar3";
            }
        });
        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar4);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar4";
            }
        });
        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar5);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar5";
            }
        });
        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar6);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar6";
            }
        });
        avatar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar7);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar7";
            }
        });
        avatar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar8);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar8";
            }
        });
        avatar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar9);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar9";
            }
        });
        avatar10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar10);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar10";
            }
        });

        avatar11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar11);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar11";
            }
        });
        avatar12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar12);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar12";
            }
        });
        avatar13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar13);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar13";
            }
        });
        avatar14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar14);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar14";
            }
        });
        avatar15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar15);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar15";
            }
        });
        avatar16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar16);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar16";
            }
        });
        avatar17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar17);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar17";
            }
        });
        avatar18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar18);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar18";
            }
        });
        avatar19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar19);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar19";
            }
        });
        avatar20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar20);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar20";
            }
        });
        avatar21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar21);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar21";
            }
        });
        avatar22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar22);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar22";
            }
        });
        avatar23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar23);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar23";
            }
        });
        avatar24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar24);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar24";
            }
        });
        avatar25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar25);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar25";
            }
        });
        avatar26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar26);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar26";
            }
        });
        avatar27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar27);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar27";
            }
        });
        avatar28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar28);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar28";
            }
        });
        avatar29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar29);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar29";
            }
        });
        avatar30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar30);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar30";
            }
        });

        avatar31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar31);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar31";
            }
        });
        avatar32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar32);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar32";
            }
        });
        avatar33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar33);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar33";
            }
        });
        avatar34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar34);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar34";
            }
        });
        avatar35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar35);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar35";
            }
        });
        avatar36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar36);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar36";
            }
        });
        avatar37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar37);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar37";
            }
        });
        avatar38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar38);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar38";
            }
        });
        avatar39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar39);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar39";
            }
        });
        avatar40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar40);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar40";
            }
        });

        avatar41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar41);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar41";
            }
        });
        avatar42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar42);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar42";
            }
        });
        avatar43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar43);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar43";
            }
        });
        avatar44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar44);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar44";
            }
        });
        avatar45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar45);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar45";
            }
        });
        avatar46.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar46);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar46";
            }
        });
        avatar47.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar47);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar47";
            }
        });
        avatar48.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar48);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar48";
            }
        });
        avatar49.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar49);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar49";
            }
        });
        avatar50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAvatarForStroke = dialogView.findViewById(R.id.avatar50);
                setStrok(previousAvatarForStroke,currentAvatarForStroke);
                previousAvatarForStroke = currentAvatarForStroke;
                currentAvatar="avatar50";
            }
        });

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(currentAvatar)
                {
                    case "avatar1":
                        circularImageView.setImageResource(R.drawable.avatar1);
                        break;
                    case "avatar2":
                        circularImageView.setImageResource(R.drawable.avatar2);
                        break;
                    case "avatar3":
                        circularImageView.setImageResource(R.drawable.avatar3);
                        break;
                    case "avatar4":
                        circularImageView.setImageResource(R.drawable.avatar4);
                        break;
                    case "avatar5":
                        circularImageView.setImageResource(R.drawable.avatar5);
                        break;
                    case "avatar6":
                        circularImageView.setImageResource(R.drawable.avatar_six);
                        break;
                    case "avatar7":
                        circularImageView.setImageResource(R.drawable.avatar7);
                        break;
                    case "avatar8":
                        circularImageView.setImageResource(R.drawable.avatar8);
                        break;
                    case "avatar9":
                        circularImageView.setImageResource(R.drawable.avatar9);
                        break;
                    case "avatar10":
                        circularImageView.setImageResource(R.drawable.avatar10);
                        break;
                    case "avatar11":
                        circularImageView.setImageResource(R.drawable.avatar11);
                        break;
                    case "avatar12":
                        circularImageView.setImageResource(R.drawable.avatar12);
                        break;
                    case "avatar13":
                        circularImageView.setImageResource(R.drawable.avatar13);
                        break;
                    case "avatar14":
                        circularImageView.setImageResource(R.drawable.avatar14);
                        break;
                    case "avatar15":
                        circularImageView.setImageResource(R.drawable.avatar15);
                        break;
                    case "avatar16":
                        circularImageView.setImageResource(R.drawable.avatar16);
                        break;
                    case "avatar17":
                        circularImageView.setImageResource(R.drawable.avatar17);
                        break;
                    case "avatar18":
                        circularImageView.setImageResource(R.drawable.avatar18);
                        break;
                    case "avatar19":
                        circularImageView.setImageResource(R.drawable.avatar19);
                        break;
                    case "avatar20":
                        circularImageView.setImageResource(R.drawable.avatar20);
                        break;
                    case "avatar21":
                        circularImageView.setImageResource(R.drawable.avatar21);
                        break;
                    case "avatar22":
                        circularImageView.setImageResource(R.drawable.avatar22);
                        break;
                    case "avatar23":
                        circularImageView.setImageResource(R.drawable.avatar23);
                        break;
                    case "avatar24":
                        circularImageView.setImageResource(R.drawable.avatar24);
                        break;
                    case "avatar25":
                        circularImageView.setImageResource(R.drawable.avatar25);
                        break;
                    case "avatar26":
                        circularImageView.setImageResource(R.drawable.avatar26);
                        break;
                    case "avatar27":
                        circularImageView.setImageResource(R.drawable.avatar27);
                        break;
                    case "avatar28":
                        circularImageView.setImageResource(R.drawable.avatar28);
                        break;
                    case "avatar29":
                        circularImageView.setImageResource(R.drawable.avatar29);
                        break;
                    case "avatar30":
                        circularImageView.setImageResource(R.drawable.avatar30);
                        break;
                    case "avatar31":
                        circularImageView.setImageResource(R.drawable.avatar31);
                        break;
                    case "avatar32":
                        circularImageView.setImageResource(R.drawable.avatar32);
                        break;
                    case "avatar33":
                        circularImageView.setImageResource(R.drawable.avatar33);
                        break;
                    case "avatar34":
                        circularImageView.setImageResource(R.drawable.avatar34);
                        break;
                    case "avatar35":
                        circularImageView.setImageResource(R.drawable.avatar35);
                        break;
                    case "avatar36":
                        circularImageView.setImageResource(R.drawable.avatar36);
                        break;
                    case "avatar37":
                        circularImageView.setImageResource(R.drawable.avatar37);
                        break;
                    case "avatar38":
                        circularImageView.setImageResource(R.drawable.avatar38);
                        break;
                    case "avatar39":
                        circularImageView.setImageResource(R.drawable.avatar39);
                        break;
                    case "avatar40":
                        circularImageView.setImageResource(R.drawable.avatar40);
                        break;
                    case "avatar41":
                        circularImageView.setImageResource(R.drawable.avatar41);
                        break;
                    case "avatar42":
                        circularImageView.setImageResource(R.drawable.avatar42);
                        break;
                    case "avatar43":
                        circularImageView.setImageResource(R.drawable.avatar43);
                        break;
                    case "avatar44":
                        circularImageView.setImageResource(R.drawable.avatar44);
                        break;
                    case "avatar45":
                        circularImageView.setImageResource(R.drawable.avatar45);
                        break;
                    case "avatar46":
                        circularImageView.setImageResource(R.drawable.avatar46);
                        break;
                    case "avatar47":
                        circularImageView.setImageResource(R.drawable.avatar47);
                        break;
                    case "avatar48":
                        circularImageView.setImageResource(R.drawable.avatar48_2);
                        break;
                    case "avatar49":
                        circularImageView.setImageResource(R.drawable.avatar49);
                        break;
                    case "avatar50":
                        circularImageView.setImageResource(R.drawable.avatar50);
                        break;
                    default:
                        circularImageView.setImageResource(R.drawable.avatar4);
                }
                aLoadingDialog.show();
                Avatar avatar = new Avatar(currentAvatar);
                DatabaseReference referenceAvatar = FirebaseDatabase.getInstance().getReference("User Avatar");
                referenceAvatar.child(auth.getCurrentUser().getUid()).setValue(avatar)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    aLoadingDialog.cancel();
                                    previousAvatar=currentAvatar;
                                }
                                else {
                                    try{
                                        throw task.getException();
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    aLoadingDialog.cancel();
                                }
                            }
                        });
            }
        });
        dialogBuilder.create().show();
    }

    private void setStrok(ImageView previousAvatarForStroke, ImageView currentAvatarForStroke) {
        if(previousAvatarForStroke == currentAvatarForStroke)
            return;
        currentAvatarForStroke.setBackgroundResource(R.drawable.background_image_view);
        previousAvatarForStroke.setBackgroundResource(R.drawable.empty_background_image_view);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        // extracting user reference from db form class named "UserDetails" and node "User details"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User Details");
        DatabaseReference referenceAvatar = FirebaseDatabase.getInstance().getReference("User Avatar");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);
                if(userDetails != null)
                {
                    //fetching data from data base
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    dob = userDetails.dateOfBirth;
                    gender =userDetails.gender;
                    phone_number =userDetails.number;
                    // setting data in text view
                    name_view.setText(name);
                    email_view.setText(email);
                    dob_view.setText(dob);
                    gender_view.setText(gender);
                    number_view.setText(phone_number);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        referenceAvatar.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Avatar avatar = snapshot.getValue(Avatar.class);
                if(avatar != null)
                {
                    previousAvatar = avatar.avatar;
                    switch(previousAvatar)
                    {
                        case "avatar1":
                            circularImageView.setImageResource(R.drawable.avatar1);
                            break;
                        case "avatar2":
                            circularImageView.setImageResource(R.drawable.avatar2);
                            break;
                        case "avatar3":
                            circularImageView.setImageResource(R.drawable.avatar3);
                            break;
                        case "avatar4":
                            circularImageView.setImageResource(R.drawable.avatar4);
                            break;
                        case "avatar5":
                            circularImageView.setImageResource(R.drawable.avatar5);
                            break;
                        case "avatar6":
                            circularImageView.setImageResource(R.drawable.avatar_six);
                            break;
                        case "avatar7":
                            circularImageView.setImageResource(R.drawable.avatar7);
                            break;
                        case "avatar8":
                            circularImageView.setImageResource(R.drawable.avatar8);
                            break;
                        case "avatar9":
                            circularImageView.setImageResource(R.drawable.avatar9);
                            break;
                        case "avatar10":
                            circularImageView.setImageResource(R.drawable.avatar10);
                            break;
                        case "avatar11":
                            circularImageView.setImageResource(R.drawable.avatar11);
                            break;
                        case "avatar12":
                            circularImageView.setImageResource(R.drawable.avatar12);
                            break;
                        case "avatar13":
                            circularImageView.setImageResource(R.drawable.avatar13);
                            break;
                        case "avatar14":
                            circularImageView.setImageResource(R.drawable.avatar14);
                            break;
                        case "avatar15":
                            circularImageView.setImageResource(R.drawable.avatar15);
                            break;
                        case "avatar16":
                            circularImageView.setImageResource(R.drawable.avatar16);
                            break;
                        case "avatar17":
                            circularImageView.setImageResource(R.drawable.avatar17);
                            break;
                        case "avatar18":
                            circularImageView.setImageResource(R.drawable.avatar18);
                            break;
                        case "avatar19":
                            circularImageView.setImageResource(R.drawable.avatar19);
                            break;
                        case "avatar20":
                            circularImageView.setImageResource(R.drawable.avatar20);
                            break;
                        case "avatar21":
                            circularImageView.setImageResource(R.drawable.avatar21);
                            break;
                        case "avatar22":
                            circularImageView.setImageResource(R.drawable.avatar22);
                            break;
                        case "avatar23":
                            circularImageView.setImageResource(R.drawable.avatar23);
                            break;
                        case "avatar24":
                            circularImageView.setImageResource(R.drawable.avatar24);
                            break;
                        case "avatar25":
                            circularImageView.setImageResource(R.drawable.avatar25);
                            break;
                        case "avatar26":
                            circularImageView.setImageResource(R.drawable.avatar26);
                            break;
                        case "avatar27":
                            circularImageView.setImageResource(R.drawable.avatar27);
                            break;
                        case "avatar28":
                            circularImageView.setImageResource(R.drawable.avatar28);
                            break;
                        case "avatar29":
                            circularImageView.setImageResource(R.drawable.avatar29);
                            break;
                        case "avatar30":
                            circularImageView.setImageResource(R.drawable.avatar30);
                            break;
                        case "avatar31":
                            circularImageView.setImageResource(R.drawable.avatar31);
                            break;
                        case "avatar32":
                            circularImageView.setImageResource(R.drawable.avatar32);
                            break;
                        case "avatar33":
                            circularImageView.setImageResource(R.drawable.avatar33);
                            break;
                        case "avatar34":
                            circularImageView.setImageResource(R.drawable.avatar34);
                            break;
                        case "avatar35":
                            circularImageView.setImageResource(R.drawable.avatar35);
                            break;
                        case "avatar36":
                            circularImageView.setImageResource(R.drawable.avatar36);
                            break;
                        case "avatar37":
                            circularImageView.setImageResource(R.drawable.avatar37);
                            break;
                        case "avatar38":
                            circularImageView.setImageResource(R.drawable.avatar38);
                            break;
                        case "avatar39":
                            circularImageView.setImageResource(R.drawable.avatar39);
                            break;
                        case "avatar40":
                            circularImageView.setImageResource(R.drawable.avatar40);
                            break;
                        case "avatar41":
                            circularImageView.setImageResource(R.drawable.avatar41);
                            break;
                        case "avatar42":
                            circularImageView.setImageResource(R.drawable.avatar42);
                            break;
                        case "avatar43":
                            circularImageView.setImageResource(R.drawable.avatar43);
                            break;
                        case "avatar44":
                            circularImageView.setImageResource(R.drawable.avatar44);
                            break;
                        case "avatar45":
                            circularImageView.setImageResource(R.drawable.avatar45);
                            break;
                        case "avatar46":
                            circularImageView.setImageResource(R.drawable.avatar46);
                            break;
                        case "avatar47":
                            circularImageView.setImageResource(R.drawable.avatar47);
                            break;
                        case "avatar48":
                            circularImageView.setImageResource(R.drawable.avatar48_2);
                            break;
                        case "avatar49":
                            circularImageView.setImageResource(R.drawable.avatar49);
                            break;
                        case "avatar50":
                            circularImageView.setImageResource(R.drawable.avatar50);
                            break;
                        default:
                            circularImageView.setImageResource(R.drawable.avatar4);
                    }
                    aLoadingDialog.cancel();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "something went wrong", Toast.LENGTH_LONG).show();
                aLoadingDialog.cancel();
            }
        });
    }
    private String validateName(String val) {
        if (val.isEmpty())
            return "empty";
        else if(val.equalsIgnoreCase(name))
            return "same";
        else
            return "";
    }
    private String validateDOB(String val) {
        val = val.replace("/","");
        if (val.isEmpty()) {
            return "";
        }
        else if(val.equalsIgnoreCase(dob))
        {
            return "same";
        }
        else if (!val.matches("\\d{8}"))
        {
            return "wrong";
        }
        else
        {
            int dob = Integer.parseInt(val);
            Calendar calendar = Calendar.getInstance(); //Get an instance of the Calendar class
            Date currentDate = calendar.getTime();      // Get the current date:
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = simpleDateFormat.format(currentDate);
            int todayDate = Integer.parseInt(formattedDate);
            int year = todayDate / 10000;
            int month = (todayDate / 100) % 100;
            int day = todayDate % 100;
            boolean isValidDate = false;
            if (todayDate - dob < 8) {
                return "under";
            } else if (year < (todayDate / 10000) - 100) {
                return "invalid";
            } else if (month < 1 && month > 12) {
                return "invalidM";
            } else {
                if (day >= 1 && day <= 31) {
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                        isValidDate = false; // Months with 30 days
                    } else if (month == 2) {
                        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                            isValidDate = day <= 29; // Leap year February
                        } else {
                            isValidDate = day <= 28; // Non-leap year February
                        }
                    } else {
                        isValidDate = true;
                    }
                }
                if (!isValidDate) {
                    return "invalidD";
                } else {
                    return "valid";
                }
            }
        }
    }
    private String validatePhoneNo(String val) {
        if (val.isEmpty()) {
            return "empty";
        }
        else if(val.equalsIgnoreCase(phone_number))
        {
            return "same";
        }
        else if (!Patterns.PHONE.matcher(val).matches() || val.length() !=10) {
            return "invalid";
        }
        else {
            return "true";
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
}