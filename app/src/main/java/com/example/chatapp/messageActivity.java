package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.messageAdapter;
import com.example.chatapp.Model.chat;
import com.example.chatapp.Model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;


    ImageButton btn_send;
    EditText text_send;


    messageAdapter messageAdapter;
    List<chat>mchat;

    RecyclerView recyclerView;


    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar=findViewById(R.id.toolbar);

        recyclerView=findViewById(R.id.recycleView);



        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        btn_send =findViewById(R.id.btn_send);


        text_send=findViewById(R.id.text_send);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        circleImageView=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);



        intent=getIntent();

        final String userid=intent.getStringExtra("userid");

        fuser= FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg=text_send.getText().toString();

                if(!msg.equals(""))
                {
                    sendMsg(fuser.getUid(),userid,msg);
                }
                else
                {
                    FancyToast.makeText(messageActivity.this, "You cant send empty message", FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                }
                text_send.setText("");

            }
        });





        reference= FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user=dataSnapshot.getValue(user.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default"))
                {
                    circleImageView.setImageResource(R.mipmap.ic_launcher_round);
                }
                else {
                    Glide.with(messageActivity.this).load(user.getImageURL()).into(circleImageView);
                }

                readMesagges(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendMsg(String sender,String receiver,String msg)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",msg);

        reference.child("Chats").push().setValue(hashMap);


    }

    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    chat chat = snapshot.getValue(chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                            mchat.add(chat);
                    }

                    messageAdapter = new messageAdapter(messageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
