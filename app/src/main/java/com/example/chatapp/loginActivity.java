package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class loginActivity extends AppCompatActivity {

    EditText email,password;
    Button btn;

    FirebaseAuth auth;
    DatabaseReference ref;

    private String TAG="check";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        btn=findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        View rootView = findViewById(android.R.id.content);

        final List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(
                rootView, TextInputLayout.class);



         btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
                    boolean noErrors = true;
                    for (TextInputLayout textInputLayout : textInputLayouts) {
                        String editTextString = textInputLayout.getEditText().getText().toString();
                        if (editTextString.isEmpty()) {
                            textInputLayout.setError(getResources().getString(R.string.error_string));
                            FancyToast.makeText(loginActivity.this, "All fields are required", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            noErrors = false;
                        } else {
                            textInputLayout.setError(null);
                        }
                    }

                    if (noErrors) {
                        // All fields are valid!
                    }


                }
                else {
                    auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                               // FirebaseUser user = auth.getCurrentUser();

                                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                FancyToast.makeText(loginActivity.this, "Authentication Failed!", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }

                        }

                  });
                }
            }
         });
    }
}


