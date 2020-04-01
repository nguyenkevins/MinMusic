package com.outbit.scrollablelist;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import static com.outbit.scrollablelist.PlayerActivity.displayNB;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<SongInfo> songList;

    MediaPlayer mp;

    OnItemClickListener onItemClickListener;

    int row_index = -1;
    View previousViewT;
    View previousViewO;



    public RecyclerAdapter(ArrayList<SongInfo> songList) {
        this.songList = songList;
    }

    public interface  OnItemClickListener {
        void onItemClick(TextView t, TextView o, View v, SongInfo obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(true);
        String title = songList.get(position).getTitle();
        String artist = songList.get(position).getArtist();
        if(title.length() <= 30) {
            holder.primaryTitle.setText( "   " + title.substring( 0, title.lastIndexOf( '.' ) ) );
        } else {
            holder.primaryTitle.setText( "   " + title.substring( 0, 29 ) + "...");
        }
        holder.secondaryTitle.setText("   " + artist);

        holder.primaryTitle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick( holder.primaryTitle, holder.secondaryTitle, v, songList.get(position), position);

                }
            }
        } );

        holder.secondaryTitle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick( holder.primaryTitle, holder.secondaryTitle, v, songList.get(position), position);
                }
            }
        } );

    }

    public void selectMusic(int position) {

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

            primaryTitle = itemView.findViewById(R.id.bottomTitle );
            secondaryTitle = itemView.findViewById(R.id.secondaryTitle);



        }

        public void onClick() {

        }


    }

}
