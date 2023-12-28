package com.example.texter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up_user extends AppCompatActivity {
    boolean isChecked = false;
    private ALoadingDialog aLoadingDialog;
    private LottieAnimationView checkBoxAnimation;
    private TextView login_page, login_page_two, dialogbox2,dialogbox1;
    private EditText name_string, email_string, dateOfBirth_string, password_string, number_string, password_again_string;
    private TextInputLayout name, email, dateOfBirth, password, number, password_again;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private AppCompatButton register;
    private String login_email , login_pwd;
    private static final String TAG ="Sign_up_user";  // register User function
    private DatePickerDialog picker;
    private boolean isRegistered =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        checkBoxAnimation = findViewById(R.id.anima);
        login_page = findViewById(R.id.login_page_transfer);
        login_page_two = findViewById(R.id.login_page_transfer_two);
        aLoadingDialog = new ALoadingDialog(this);

        name_string = findViewById(R.id.name_string);
        email_string = findViewById(R.id.email_string);
        dateOfBirth_string = findViewById(R.id.dateOfBirth_string);
        password_string = findViewById(R.id.password_string);
        number_string = findViewById(R.id.number_string);
        password_again_string = findViewById(R.id.password_again_string);
        radioGroup = findViewById(R.id.gender);
        register = findViewById(R.id.register);

        dialogbox1=findViewById(R.id.dialogbox1);
        dialogbox2=findViewById(R.id.dialogbox2);
        dialogbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                View dialogView = getLayoutInflater().inflate(R.layout.termsandcondition, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Sign_up_user.this);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
            }

        });
        dialogbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                View dialogView = getLayoutInflater().inflate(R.layout.termsandcondition, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Sign_up_user.this);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Change the dialog box background color
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box_with_edit_text);
                    }
                });
                alertDialog.show();
            }

        });
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        password = findViewById(R.id.password);
        number = findViewById(R.id.number);
        password_again = findViewById(R.id.password_again);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedGenderId);
                String name = name_string.getText().toString(),
                        email = email_string.getText().toString(),
                        dateOfBirth = dateOfBirth_string.getText().toString(),
                        password = password_string.getText().toString(),
                        number = number_string.getText().toString(),
                        password_again = password_again_string.getText().toString();
                String gender = radioButton.getText().toString();
                name = trimStartEndSpaces(name);
                email=trimStartEndSpaces(email);
                dateOfBirth=trimStartEndSpaces(dateOfBirth);
                password=trimStartEndSpaces(password);
                number=trimStartEndSpaces(number);
                password_again=trimStartEndSpaces(password_again);
                gender=trimStartEndSpaces(gender);
                login_email=email;
                login_pwd=password;
                if (validateName(name) && validateEmail(email) && validatePhoneNo(number) && validateDOB(dateOfBirth) && validatePassword(password) && validateConfirmPassword(password, password_again)) {
                    if (!isChecked) {
                        Toast.makeText(Sign_up_user.this, "Please first accept the term's and conditions", Toast.LENGTH_LONG).show();
                    } else {
                        aLoadingDialog.show();
                        registerUser(name,email,dateOfBirth,gender,number,password);
                    }
                }
            }
        });
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_up_user.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        login_page_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_up_user.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        dateOfBirth_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month =  calendar.get(Calendar.MONTH);
                int year =  calendar.get(Calendar.YEAR);
                //Date Picker Dialog
                picker = new DatePickerDialog(Sign_up_user.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int days) {
                        if(month+1 < 10)
                            dateOfBirth_string.setText(year+ "/0" +(month+1)+ "/" +days);
                        else
                            dateOfBirth_string.setText(year+ "/" +(month+1)+ "/" +days);
                    }
                }, year , month , day);
                picker.show();
            }
        });

        checkBoxAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    checkBoxAnimation.setSpeed(-2);
                    checkBoxAnimation.playAnimation();
                    isChecked = !isChecked;
                } else {
                    checkBoxAnimation.setSpeed(2);
                    checkBoxAnimation.playAnimation();
                    isChecked = !isChecked;
                }
            }
        });
    }

    private void registerUser(String name, String email_string, String dateOfBirth, String gender, String number, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance(); //obtain the instance of firebase authentication and save that instance into auth variable
        auth.createUserWithEmailAndPassword(email_string,password) //with that instance we gona create a new user with email and password
                .addOnCompleteListener(Sign_up_user.this, new OnCompleteListener<AuthResult>()
                {  // we gonna use this method to make sure that user is created properly and also this method invoke when the user creation process is successful
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // once this function invoke that mean task is completed , but we still dont know it is successful or not so we use task.isSuccessful(}
                        if(task.isSuccessful())
                        {
                            // one user is created successfully we can do what we want like move to next screen(inside the app) OR add a dialog box for email verification etc
                            FirebaseUser firebaseUser =   auth.getCurrentUser(); //Now we use FirebaseUser --> get the current user using auth veriable

                            //Saving a user name in -> firebase user name object
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            firebaseUser.updateProfile(profileChangeRequest); // this piece of code will change the existing uer node

                            //save user data also into firebase
                            UserDetails writeUserDetails = new UserDetails(dateOfBirth , gender , number); // UserDetails is a new class which gonna add data into firebase
                            String avatarID="avatar23";
                            if(gender.equalsIgnoreCase("Male"))
                                avatarID="avatar6";
                            Avatar avatar = new Avatar(avatarID);
                            DatabaseReference referenceAvatar = FirebaseDatabase.getInstance().getReference("User Avatar");
                            referenceAvatar.child(firebaseUser.getUid()).setValue(avatar)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                //do nothing
                                            }
                                            else {
                                                try{
                                                    throw task.getException();
                                                }
                                                catch (Exception e)
                                                {
                                                    Log.e(TAG ,e.getMessage());
                                                    Toast.makeText(Sign_up_user.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                            //Creating a Parent node(named User Details) in data base and getting the reference
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User Details");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails)  //creating child nodes with name == uid (user id given by firebase) + setValue function will set the value in the child node(in it we will send object of our java class)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() { // adding a onCompleteListener + task.isSuccessful
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                //sending verification email
                                                firebaseUser.sendEmailVerification();
                                                isRegistered =true;
                                                auth.signOut();
                                                showAlertDialog();
                                            }
                                            else
                                            { // doing exception handling for the task of saving data in db
                                                try{
                                                    throw task.getException();
                                                }
                                                catch (Exception e)
                                                {
                                                    Log.e(TAG ,e.getMessage());
                                                    Toast.makeText(Sign_up_user.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });

                        }else{ // exception handling for the task of creating/signing up the user
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException e)
                            {
                                email.setError("User already exists");
                                email.requestFocus();
                            }
                            catch (Exception e)
                            {
                                Log.e(TAG ,e.getMessage());
                                Toast.makeText(Sign_up_user.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        aLoadingDialog.cancel(); //stopping the progress bar
                    }
                });

    }
    private boolean validateConfirmPassword(String val, String val2) {
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
            password_again.setError("Password Field cannot be empty");
            password_again.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            password_again.setError("Password is too weak");
            password_again.requestFocus();
            return false;
        } else if (!val2.equals(val)) {
            password_again.setError("Password confirmation is required");
            password_again.requestFocus();
            return false;
        } else {
            password_again.setError(null);
            password_again.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName(String val) {
        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            name.requestFocus();
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(String val) {
        if (val.isEmpty()) {
            email.setError("Email address is required");
            email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError("Invalid email address");
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNo(String val) {
        if (val.isEmpty()) {
            number.setError("Phone number is required");
            number.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(val).matches()) {
            number.setError("Invalid phone number");
            number.requestFocus();
            return false;
        } else {
            number.setError(null);
            return true;
        }
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
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDOB(String val) {
        val = val.replace("/","");
        if (val.isEmpty()) {
            dateOfBirth.setError("Field cannot be empty");
            dateOfBirth.requestFocus();
            return false;
        }
        else if (!val.matches("\\d{8}"))
        {
            dateOfBirth.setError("Please enter DOB Correctly");
            dateOfBirth.requestFocus();
            return false;
        } else
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
                dateOfBirth.setError("You are under aged to use this app");
                dateOfBirth.requestFocus();
                return false;
            } else if (year < (todayDate / 10000) - 100) {
                dateOfBirth.setError("Please enter a valid year");
                dateOfBirth.requestFocus();
                return false;
            } else if (month < 1 && month > 12) {
                dateOfBirth.setError("Please enter a valid month");
                dateOfBirth.requestFocus();
                return false;
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
                    dateOfBirth.setError("Please enter a valid date");
                    dateOfBirth.requestFocus();
                    return false;
                } else {

                    dateOfBirth.setError(null);
                    dateOfBirth.setErrorEnabled(false);
                    return true;
                }
            }
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

    private void showAlertDialog() {  // showing dialog box for email verification
        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up_user.this);
        builder.setTitle("Email Not Verified");
        builder.setCancelable(false);
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

    @Override
    protected void onStart() {
        super.onStart();
        if(isRegistered)
        {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(login_email,login_pwd).addOnCompleteListener(Sign_up_user.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                            Intent intent = new Intent(Sign_up_user.this, MainActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            showAlertDialog();
                        }
                    }
                    else
                    {

                    }
                }
            });

        }
    }

}