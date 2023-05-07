package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterAnnouncements extends RecyclerView.Adapter<MyAdapterAnnouncements.MyViewHolder>{

    Context context;

    ArrayList<Announcements> list;

    public MyAdapterAnnouncements(Context context, ArrayList<Announcements> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_announcements, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Announcements ann = list.get(position);

        holder.headLine.setText(ann.getHeadline());
        holder.content.setText(ann.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView headLine, content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            headLine = (TextView) itemView.findViewById(R.id.headLine);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
