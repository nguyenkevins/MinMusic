package com.outbit.scrollablelist;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<SongInfo> songList;

    public RecyclerAdapter(ArrayList<SongInfo> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = songList.get(position).getTitle();
        String artist = songList.get(position).getArtist();
        holder.primaryTitle.setText(title.substring(0, title.lastIndexOf('.')));
        holder.secondaryTitle.setText(artist);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView primaryTitle;
        TextView secondaryTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            primaryTitle = itemView.findViewById(R.id.primaryTitle);
            secondaryTitle = itemView.findViewById(R.id.secondaryTitle);


        }
    }
}
