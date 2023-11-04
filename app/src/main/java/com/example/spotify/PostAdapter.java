package com.example.spotify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spotify.HelperClass.Constant;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.rvViewHolderClass> {

    private final OnClick onclick;
    ArrayList<PostModel> PostList;
    DataBaseHelper DBHelper;
    Context context;

    public PostAdapter(Context context, ArrayList<PostModel> postList, OnClick onclick) {
        this.context = context;
        PostList = postList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public rvViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view, parent, false);
        return new rvViewHolderClass(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull rvViewHolderClass holder, int position) {
        PostModel postObject = PostList.get(position);
        holder.postByUser.setText("Posted by " + postObject.getUsername());

        if (postObject.getUserid() == Constant.userdata.User_ID) {
            holder.bt_delete.setVisibility(View.VISIBLE);
            holder.bt_edit.setVisibility(View.VISIBLE);
        } else {
            holder.bt_delete.setVisibility(View.GONE);
            holder.bt_edit.setVisibility(View.GONE);
        }

        holder.textViewCaption.setVisibility(View.VISIBLE);
        if (postObject.img != null && !postObject.img.isEmpty()) {
            Glide.with(context).load(postObject.img).into(holder.p_image);
            holder.p_image.setVisibility(View.VISIBLE);
        } else holder.p_image.setVisibility(View.GONE);

        if (postObject.getCaption() != null && !postObject.getCaption().isEmpty()) {
            holder.textViewCaption.setText(postObject.getCaption());
            holder.textViewCaption.setVisibility(View.VISIBLE);
        } else holder.textViewCaption.setVisibility(View.GONE);

        String[] likes = postObject.getLike().split(",");
        if (likes.length > 1) {
            holder.txt_likeCount.setText("" + (likes.length-1));
        } else {
            holder.txt_likeCount.setText("0");
        }

        if (postObject.getLike().contains(String.valueOf(Constant.userdata.User_ID))) {
            holder.bt_like.setImageResource(R.drawable.logoball);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.bt_like.setImageResource(R.drawable.ic_liked);
                }
            }, 1500);
        } else {
            holder.bt_like.setImageResource(R.drawable.ic_like);
        }

        holder.bt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.OnLikeClick(postObject);
            }
        });


        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.OnDeleteClick(postObject);
            }
        });

        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.OnEditClick(postObject);
            }
        });

        holder.p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.OnPostClick(postObject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }

    public void isChangeList(ArrayList<PostModel> viewList) {
        PostList.clear();
        notifyDataSetChanged();
        this.PostList = viewList;
        notifyDataSetChanged();
    }

    interface OnClick {
        void OnEditClick(PostModel postObject);
        void OnDeleteClick(PostModel postObject);
        void OnLikeClick(PostModel postObject);
        void OnPostClick(PostModel postObject);
    }

    public static class rvViewHolderClass extends RecyclerView.ViewHolder {

        TextView textViewCaption, txt_likeCount, postByUser;
        ImageView p_image;
        ImageView bt_delete, bt_edit, bt_like;

        public rvViewHolderClass(@NonNull View itemView) {
            super(itemView);
            textViewCaption = itemView.findViewById(R.id.textViewCaption);
            p_image = itemView.findViewById(R.id.imageViewPost);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            bt_edit = itemView.findViewById(R.id.bt_edit);
            bt_like = itemView.findViewById(R.id.bt_like);
            txt_likeCount = itemView.findViewById(R.id.txt_likecount);
            postByUser = itemView.findViewById(R.id.postby_user);
        }
    }
}
