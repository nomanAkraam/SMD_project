package com.example.chatapp.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Model.user;
import com.example.chatapp.R;
import com.example.chatapp.messageActivity;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context context;
   private List<user> users;


    public UserAdapter(Context context, List<user> users) {

        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.items,parent,false);
        return  new UserAdapter.ViewHolder(view);

    }


    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final user user=users.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default"))
        {
            holder.profile_img.setImageResource(R.mipmap.ic_launcher_round);

        }
        else {
            Glide.with(context)
                    .load(user.getImageURL())
                    .into(holder.profile_img);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, messageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView profile_img;

        public ViewHolder(View itemView)
        {


          super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_img=itemView.findViewById(R.id.profile_image);

        }
    }
}
