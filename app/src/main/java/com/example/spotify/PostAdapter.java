package com.example.spotify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.rvViewHolderClass> {

    private final OnClick onclick;
    ArrayList<PostModel> PostList;
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

    @Override
    public void onBindViewHolder(@NonNull rvViewHolderClass holder, int position) {
        PostModel postObject = PostList.get(position);


        holder.textViewCaption.setVisibility(View.VISIBLE);
        if (postObject.img != null) {
            holder.p_image.setImageBitmap(getImage(postObject.img));
            holder.p_image.setVisibility(View.VISIBLE);
        } else holder.p_image.setVisibility(View.GONE);


        if (postObject.getCaption() != null && !postObject.getCaption().isEmpty()) {
            holder.textViewCaption.setText(postObject.getCaption());
            holder.textViewCaption.setVisibility(View.VISIBLE);
        } else holder.textViewCaption.setVisibility(View.GONE);

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
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);

    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }

    public static class rvViewHolderClass extends RecyclerView.ViewHolder {

        TextView textViewCaption;
        ImageView p_image;
        ImageView bt_delete, bt_edit;

        public rvViewHolderClass(@NonNull View itemView) {
            super(itemView);
            textViewCaption = itemView.findViewById(R.id.textViewCaption); // Use the correct ID
            p_image = itemView.findViewById(R.id.imageViewPost);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            bt_edit = itemView.findViewById(R.id.bt_edit);

        }
    }

    public void ischnagelist(ArrayList<PostModel> viewList) {
        PostList.clear();
        notifyDataSetChanged();
        this.PostList = viewList;
        notifyDataSetChanged();
    }

    interface OnClick {
        public void OnEditClick(PostModel postObject);

        public void OnDeleteClick(PostModel postObject);

    }
}
