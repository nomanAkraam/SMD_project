package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Fragments.chatsFragment;
import com.example.chatapp.Fragments.profileFragment;
import com.example.chatapp.Fragments.usersFragment;
import com.example.chatapp.Model.user;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleImageView = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user use = dataSnapshot.getValue(user.class);
                username.setText(use.getUsername());


                if (use.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.mipmap.ic_launcher_round);
                } else {
                    Glide
                            .with(MainActivity.this)
                            .load(use.getImageURL())
                            .into(circleImageView);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        TabLayout tabLayout=findViewById(R.id.tablayout);
        ViewPager viewPager=findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
         viewPagerAdapter.addFragments(new chatsFragment(),"Chats");
         viewPagerAdapter.addFragments(new usersFragment(),"Users");
        viewPagerAdapter.addFragments(new profileFragment(),"Profile");

         viewPager.setAdapter(viewPagerAdapter);
         tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,startActivity.class));
            finish();

            return true;

        }

        return false;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment>fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragments(Fragment fragment,String title)
        {
           fragments.add(fragment);
           titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
