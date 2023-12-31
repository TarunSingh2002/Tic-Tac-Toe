package com.example.texter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private LinearLayout player1Layout , player2Layout;
    private ImageView image1,image2,image3,image4,image5,image6,image7,image8,image9;
    private TextView player1TV , player2TV;
    private final List<int[]> combinationsList=new ArrayList<>();
    private final List<String> doneBoxes=new ArrayList<>(); // done boxes positions by users so users so users won't select the box again

    //getting firebase database reference from URL
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Lobby");
    //Player unique id
    private boolean opponentFound = false;
    private String playerUniqueId ="0";
    private String opponentUniqueId ="0";
    private String status ="matching";
    private String playerTurn ="";
    private String connectionId="";
    ValueEventListener wonEventListener,turnsEventListener;
    int getPlayersCount;
    private final String[] boxesSelectedBy ={"","","","","","","","",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        final String name= getIntent().getStringExtra("playerName");
        player1Layout=findViewById(R.id.player1Layout);
        player2Layout=findViewById(R.id.player2Layout);
        player1TV=findViewById(R.id.player1TV);
        player2TV=findViewById(R.id.player2TV);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        image5=findViewById(R.id.image5);
        image6=findViewById(R.id.image6);
        image7=findViewById(R.id.image7);
        image8=findViewById(R.id.image8);
        image9=findViewById(R.id.image9);

        combinationsList.add(new int[]{0,1,2});
        combinationsList.add(new int[]{3,4,5});
        combinationsList.add(new int[]{6,7,8});

        combinationsList.add(new int[]{0,3,6});
        combinationsList.add(new int[]{1,4,7});
        combinationsList.add(new int[]{2,5,8});

        combinationsList.add(new int[]{2,4,6});
        combinationsList.add(new int[]{0,4,8});

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting for opponent");
        progressDialog.show();

        //genertae player unique id,
        playerUniqueId = String.valueOf(System.currentTimeMillis());
        // setting player name to the TextView
        player1TV.setText(name);
        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if opponent found or not if not then look for the opponent
                if(!opponentFound){
                    // checking if there are others in the firebase realtime database
                    if(snapshot.hasChildren())
                    {
                        // checking all connections if other users are also waiting for a user to play the match
                       for(DataSnapshot connections : snapshot.getChildren())
                       {
                           //getting connection unique id
                           String conId = connections.getKey();
                           // 2 players are required to play the game
                           // if getPlayersCount is 1 it means other player is waiting for a opponent to play the game
                           // else if getPlayersCount is 2 it means this connection has completed with 2 players
                           getPlayersCount = (int)connections.getChildrenCount();
                           //after creating a new connection waiting for other to join
                           if(status.equals("waiting")){
                               // if getPlayersCount is 2 means other player joined the match
                               if(getPlayersCount==2)
                               {
                                   playerTurn = playerUniqueId;
                                   applyPlayerTurn(playerTurn);
                                   // true when opponent found in connections
                                   boolean playerFound = false;
                                   // getting players in connection
                                   for(DataSnapshot players : connections.getChildren())
                                   {
                                       String getPlayerUniqueID = players.getKey();
                                       //check if player id match with who created connection(this user). If match then got opponent details
                                       if(getPlayerUniqueID.equals(playerUniqueId))
                                       {
                                           playerFound = true;
                                       }
                                       else if(playerFound){
                                           String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                           opponentUniqueId= players.getKey();
                                           //set opponent playername to the textView
                                           player2TV.setText(getOpponentPlayerName);
                                           // assigning connection id
                                           connectionId=conId;
                                           opponentFound =true;
                                           // adding turns listener and won listener to the database reference
                                           databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                           databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);
                                           if(progressDialog.isShowing())
                                           {
                                               progressDialog.dismiss();
                                           }
                                           databaseReference.child("connections").removeEventListener(this);
                                       }
                                   }
                               }
                           }
                           else{
                               if(getPlayersCount == 1)
                               {
                                   connections.child(playerUniqueId).child("player_name").getRef().setValue(name);
                                   for(DataSnapshot players : connections.getChildren()){
                                       String getOpponentName = players.child("player_name").getValue(String.class);
                                       opponentUniqueId = players.getKey();

                                       playerTurn = opponentUniqueId;
                                       applyPlayerTurn(playerTurn);
                                       player2TV.setText(getOpponentName);
                                       connectionId = conId;
                                       opponentFound =true;
                                       // adding turns listener and won listener to the database reference
                                       databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                       databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);
                                       if(progressDialog.isShowing())
                                       {
                                           progressDialog.dismiss();
                                       }
                                       databaseReference.child("connections").removeEventListener(this);
                                       break;
                                   }
                               }
                           }
                       }
                        // check if opponent is not found and user is not waiting for the opponent anymore then create a new connection
                        if(!opponentFound && !status.equals("waiting"))
                        {
                            // generating unique id for the connection
                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                            // adding first player to the connection and waiting for other to complete the connection and play the game
                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(name);
                            status ="waiting";
                        }
                    }

                    // if there is no connections available in the firebase database then create a new connection
                    //It is like creating a room/looby and waiting for other player to join the room
                    else{
                        // generating unique id for the connection
                        String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                        // adding first player to the connection and waiting for other to complete the connection and play the game
                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(name);
                        status ="waiting";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        turnsEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getChildrenCount() ==2)
                    {
                        final int getBoxPosition = Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));

                        final  String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);
                        if(!doneBoxes.contains(String.valueOf(getBoxPosition)))
                        {
                            doneBoxes.add(String.valueOf(getBoxPosition));
                            if(getBoxPosition == 1)
                            {
                                selectBox(image1,getBoxPosition,getPlayerId);
                            }
                            else if(getBoxPosition == 2)
                            {
                                selectBox(image2,getBoxPosition,getPlayerId);
                            }
                            if(getBoxPosition == 3)
                            {
                                selectBox(image3,getBoxPosition,getPlayerId);
                            }
                            else if(getBoxPosition == 4)
                            {
                                selectBox(image4,getBoxPosition,getPlayerId);
                            }
                            if(getBoxPosition == 5)
                            {
                                selectBox(image5,getBoxPosition,getPlayerId);
                            }
                            else if(getBoxPosition == 6)
                            {
                                selectBox(image6,getBoxPosition,getPlayerId);
                            }
                            if(getBoxPosition == 7)
                            {
                                selectBox(image7,getBoxPosition,getPlayerId);
                            }
                            else if(getBoxPosition == 8)
                            {
                                selectBox(image8,getBoxPosition,getPlayerId);
                            }
                            else if(getBoxPosition == 9)
                            {
                                selectBox(image9,getBoxPosition,getPlayerId);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        wonEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("player_id"))
                {
                    String getWinPlayerId =  snapshot.child("player_id").getValue(String.class);
                    /*final WinDialog winDialog;
                    if(getWinPlayerId.equals(playerUniqueId))
                    {
                        winDialog=new WinDialog(MainActivity3.this,"You won the game");
                    }
                    else{
                        winDialog=new WinDialog(MainActivity3.this,"You lost the game");
                    }
                    winDialog.setCancelable(false);
                    winDialog.show();*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                    builder.setCancelable(false);
                    if(getWinPlayerId.equals(playerUniqueId))
                    {
                        builder.setTitle("You won the game");
                    }
                    else{
                        builder.setTitle("You lost the game");
                    }
                    // open Email App if User click/taps Continue button
                    builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    });
                    //Create the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    //Show the  Alert Dialog
                    alertDialog.show();

                    databaseReference.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    databaseReference.child("won").child(connectionId).removeEventListener(wonEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("1") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("1");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("2") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("2");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("3") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("3");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("4") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("4");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("5") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("5");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("6") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("6");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("7") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("7");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("8") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("8");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });
        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doneBoxes.contains("9") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.x);
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("9");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                    playerTurn=opponentUniqueId;
                }
            }
        });
    }

    private void applyPlayerTurn(String playerUniqueId2) {
        if(playerUniqueId2.equals(playerUniqueId))
        {
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_storke);
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        }
        else{
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_storke);
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        }
    }
    private  void selectBox(ImageView imageView , int selectedBoxPosition , String selectedByPlayer){
        boxesSelectedBy[selectedBoxPosition-1] = selectedByPlayer;
        if(selectedByPlayer.equals(playerUniqueId))
        {
            imageView.setImageResource(R.drawable.x);
            playerTurn=opponentUniqueId;
        }
        else{
            imageView.setImageResource(R.drawable.zero);
            playerTurn= playerUniqueId;
        }
        applyPlayerTurn(playerTurn);
        //checking
        if(checkPlayerWin(selectedByPlayer))
        {
            databaseReference.child("won").child(connectionId).child("player_id").setValue(selectedByPlayer);
        }
        if(doneBoxes.size()==9)
        {
            AlertDialog.Builder builderNew = new AlertDialog.Builder(MainActivity3.this);
            builderNew.setTitle("It is a Draw");
            builderNew.setCancelable(false);
            // open Email App if User click/taps Continue button
            builderNew.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
            //Create the AlertDialog
            AlertDialog alertDialog = builderNew.create();
            //Show the  Alert Dialog
            alertDialog.show();
        }
    }
    private boolean checkPlayerWin(String playerId){
        boolean isPlayerWon = false;
        for(int i=0; i< combinationsList.size(); i++)
        {
            final int[] combination = combinationsList.get(i);
            if(boxesSelectedBy[combination[0]].equals(playerId) &&
                    boxesSelectedBy[combination[1]].equals(playerId) &&
                    boxesSelectedBy[combination[2]].equals(playerId))
            {
                isPlayerWon=true;
            }
        }
        return isPlayerWon;
    }
}