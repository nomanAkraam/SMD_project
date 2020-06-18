package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.List;


public class registerActivity extends AppCompatActivity {

    EditText useriname,email,password;
    Button butn;

    FirebaseAuth auth;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        useriname=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        butn=(Button) findViewById(R.id.signup);
        auth=FirebaseAuth.getInstance();


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));



        View rootView = findViewById(android.R.id.content);

        final List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(
                rootView, TextInputLayout.class);



        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=useriname.getText().toString().trim();
                String mail=email.getText().toString().trim();
                String pass= password.getText().toString().trim();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(mail)|| TextUtils.isEmpty(pass) )
                {
                    boolean noErrors = true;
                    for (TextInputLayout textInputLayout : textInputLayouts) {
                        String editTextString = textInputLayout.getEditText().getText().toString();
                        if (editTextString.isEmpty()) {
                            textInputLayout.setError(getResources().getString(R.string.error_string));
                            noErrors = false;
                        } else {
                            textInputLayout.setError(null);
                        }
                    }

                    if (noErrors) {
                        // All fields are valid!
                    }
                    FancyToast.makeText(registerActivity.this, "All fields are required", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }

                else if(pass.length()<6){
                    FancyToast.makeText(registerActivity.this, "password must be strong", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                }
                else
                {
                    register(user,mail,pass);
                }

            }
        });


    }



    private void register (final String username, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    String userid=firebaseUser.getUid();


                    ref= FirebaseDatabase.getInstance().getReference("users").child(userid);

                    HashMap<String,String > hashMap=new HashMap<>();

                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("ImageURL", "default");

                    ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent=new Intent(registerActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                }else{
                    Toast.makeText(registerActivity.this, "You can't register with this email and password", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}
