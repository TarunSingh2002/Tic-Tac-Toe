package com.example.texter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imageViewHome,circularImageView;
    private TextView textViewHome , textViewName;
    private AppCompatButton profile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ALoadingDialog aLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageViewHome=findViewById(R.id.imageViewHome);
        textViewHome=findViewById(R.id.textViewHome);
        textViewName=findViewById(R.id.textViewName);
        textViewHome.setTextColor(getResources().getColor(R.color.golden));
        imageViewHome.setImageResource(R.drawable.home_focused);
        aLoadingDialog = new ALoadingDialog(MainActivity2.this);
        circularImageView=findViewById(R.id.imageViewAvatar);
        final EditText editTextName=findViewById(R.id.editTextEnter);
        final AppCompatButton buttonStartGame=findViewById(R.id.buttonEnter);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity2.this, profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        else {
            aLoadingDialog.show();
            String name=firebaseUser.getDisplayName();
            textViewName.setText(name);
            DatabaseReference referenceAvatar = FirebaseDatabase.getInstance().getReference("User Avatar");
            referenceAvatar.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Avatar avatar = snapshot.getValue(Avatar.class);
                    if(avatar != null)
                    {
                        String avatarId = avatar.avatar;
                        switch(avatarId)
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
                    Toast.makeText(MainActivity2.this, "something went wrong", Toast.LENGTH_LONG).show();
                    aLoadingDialog.cancel();
                }
            });
            circularImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity2.this, profile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextName.getText().toString();
                if(name.isEmpty())
                    Toast.makeText(MainActivity2.this, "enter name", Toast.LENGTH_SHORT).show();
                else
                {
                    Intent intent=new Intent(MainActivity2.this,MainActivity3.class);
                    intent.putExtra("playerName" , name);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity2.this, profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        else {
            aLoadingDialog.show();
            String name=firebaseUser.getDisplayName();
            textViewName.setText(name);
            DatabaseReference referenceAvatar = FirebaseDatabase.getInstance().getReference("User Avatar");
            referenceAvatar.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Avatar avatar = snapshot.getValue(Avatar.class);
                    if(avatar != null)
                    {
                        String avatarId = avatar.avatar;
                        switch(avatarId)
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
                    Toast.makeText(MainActivity2.this, "something went wrong", Toast.LENGTH_LONG).show();
                    aLoadingDialog.cancel();
                }
            });
            circularImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity2.this, profile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }
    }
}

