package com.outbit.scrollablelist;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaTimestamp;
import android.media.SubtitleData;
import android.os.Build;
import android.os.Bundle;

// Access Public Storage
import android.os.Environment;

// Allow the use of LogCat
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Used for getting music directory
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

// Put songs and details into a list
import java.io.IOException;
import java.util.ArrayList;

// Help sort songs
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    //RecyclerView recyclerView;
    public static RecyclerAdapter recyclerAdapter;

    private static int SPLASH_TIMEOUT = 4000;
    public static MediaPlayer mp;
    public static SongInfo remember;
    View previousViewT;
    View previousViewO;
    private Handler handler;
    Runnable runnable;
    public static SeekBar seekBar;
    private TextView bottomTitle;

    private int songPosition;

    public static int selectSong;

    public static RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //check for permission
        if (ContextCompat.checkSelfPermission( this,
                Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE );
        }




        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        handler = new Handler();
        seekBar = findViewById( R.id.seekBar );
        bottomTitle = findViewById( R.id.bottomTitle );

        // Get directory to the Android Music folder
        File directory = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_MUSIC );

        // Log the path of the directory
        Log.v( "BEFORE", directory.getPath() );

        // Create a song list
        // Stores information about each song
        final ArrayList<SongInfo> songList = new ArrayList<>();

        // For every file in the directory...
        for (File i : directory.listFiles()) {

            // Get path of the file
            String path = i.getAbsolutePath();

            // If the file naming ends with ".mp3"
            if (path.endsWith( ".mp3" )) {

                // Create new data for songInfo
                SongInfo data = new SongInfo();

                // Create a retriever
                MediaMetadataRetriever extractMusicInfo = (MediaMetadataRetriever) new MediaMetadataRetriever();
                extractMusicInfo.setDataSource( directory + "/" + i.getName() );

                // Retrieve the artist name
                String artist = extractMusicInfo.extractMetadata( MediaMetadataRetriever.METADATA_KEY_ARTIST );
                if (artist == null || artist.equals( "" )) {
                    artist = "Artist Unknown";
                }

                // Retrieve the artist name
                String durationStr = extractMusicInfo.extractMetadata( MediaMetadataRetriever.METADATA_KEY_DURATION );
                int millSecond = Integer.parseInt(durationStr);

                // Setters for songInfo and add it into the songList
                data.setTitle( i.getName() );
                data.setArtist( artist );
                data.setFileInfo( i );
                data.setDuration(millSecond);
                songList.add( data );
            }

        }

        // Sort the song list using compareTo() and comparable
        Collections.sort( songList );

        recyclerView = findViewById( R.id.recyclerView );

        // Put songList into RecyclerAdapter (to put in the information into UI)
        recyclerAdapter = new RecyclerAdapter( songList );

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter( recyclerAdapter );






        recyclerAdapter.setOnItemClickListener( new RecyclerAdapter.OnItemClickListener() {


            public void onItemClick(final TextView t, final TextView o, View v, SongInfo obj, int position) {
                selectSong = position;
                remember = new SongInfo();
                remember.setTitle( obj.getTitle() );
                remember.setDuration(obj.getDuration());
                remember.setFileInfo(obj.getFileInfo());
                String title = obj.getTitle();

                if(title.length() <= 30) { // → In case
                    bottomTitle.setText( "Now Playing   >   " + title.substring( 0, title.lastIndexOf( '.' ) ) );
                } else {
                    bottomTitle.setText( "Now Playing   >   " + title.substring( 0, 29 ) + "...");
                }

                bottomTitle.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        openActivity2();

                    }

                });

                // Prevent memory leak and allow song to switch
                songPosition = 0;
                seekBar.setMax(obj.getDuration());
                seekBar.setProgress(0);

                if (mp != null) {
                    mp.stop();
                    mp.reset();
                    mp.release();
                    mp = null;
                }

                try {
                    mp = new MediaPlayer();

                    mp.setDataSource( obj.getFileInfo().toString() );
                    //mp.prepareAsync();
                    mp.prepare();

                    mp.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                final int songDuration = obj.getDuration();
                seekBar.setMax( songDuration );

                TextView previousTextT = (TextView) previousViewT;
                TextView previousTextO = (TextView) previousViewO;
                TextView curTextT = (TextView) t;
                TextView curTextO = (TextView) o;
                // If the clicked view is selected, re-select it again
                if (curTextT.isSelected() || curTextO.isSelected()) {
                    curTextT.setSelected( true );
                    curTextO.setSelected( true );
                    curTextT.setTextColor( Color.rgb( 200, 11, 200 ) );
                    curTextO.setTextColor( Color.rgb( 200, 11, 200 ) );
                } else { // If this isn't selected, deselect  the previous one (if any)
                    if ((previousTextT != null && previousTextT.isSelected()) || (previousTextO != null && previousTextO.isSelected())) {
                        // CHANGE WHEN SWITCHING COLOR THEME
                        previousTextT.setSelected( false );
                        previousTextT.setTextColor( Color.WHITE );
                        previousTextO.setSelected( false );
                        previousTextO.setTextColor( Color.WHITE );

                    }
                    t.setSelected( true );
                    o.setSelected( true );
                    t.setTextColor( Color.rgb( 200, 11, 200 ) );
                    o.setTextColor( Color.rgb( 200, 11, 200 ) );
                    previousViewT = t;
                    previousViewO = o;
                }



                //mp.setDataSource( obj.getFileInfo().toString() );


                mp.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(final MediaPlayer mp) {
                        //setContentView( R.layout.seekbar );
                        //seekBar.setVisibility( View.VISIBLE );
                        bottomTitle.setVisibility( View.VISIBLE );

                        seekBar.getProgressDrawable().setColorFilter(
                                Color.rgb(200,11,200), android.graphics.PorterDuff.Mode.SRC_IN);
                        seekBar.setMax(mp.getDuration());
                        mp.start();
                        changeSeekBar();

                    }

                } );

                seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser) {
                            mp.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                    }

                } );
            }

            public void changeSeekBar() {
                seekBar.setProgress( mp.getCurrentPosition() );
                //if(mp.isPlaying()) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            changeSeekBar();
                        }
                    };
                    handler.postDelayed( runnable, 1000 );

                //}
            }
        } );


    }

    public void openActivity2() {
        Intent intent = new Intent( this, PlayerActivity.class );
        intent.putExtra("remember", remember.getFileInfo());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        mp.reset();
        mp.release();
        mp = null;
    }



}








/*

                       seekBar.setVisibility( View.VISIBLE );
                        mp.start();

                        new Thread() {
                            @Override
                            public void run() {

                                songPosition = 0;

                                while(songPosition < songDuration) {
                                    try {
                                        Thread.sleep( 1000 );

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    songPosition += 1000; // in terms of milliseconds

                                    runOnUiThread( new Runnable() {


                                        public void run() {


                                        }
                                    } );

                                }

                            }

                        }.start();


 */




class SongInfo implements Comparable<SongInfo> {
    private String title;
    private String artist;
    private File fileInfo;
    private int duration;

    public SongInfo() {
        title = "Unknown Title";
        artist = "Unknown Artist";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setFileInfo(File fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void setDuration(int duration) { this.duration = duration; }



    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public File getFileInfo() {
        return fileInfo;
    }

    public int getDuration() {
        return duration;
    }

    // Alphabetically sort the song list
    public int compareTo(SongInfo otherSongInfo) {
        int result = 0;
        String titleA = this.title.toLowerCase();
        String titleB = otherSongInfo.getTitle().toLowerCase();
        if(titleA.length() <= titleB.length()) {
            for(int i = 0; i < titleA.length(); i++) {
                if(titleA.charAt(i)-titleB.charAt(i) != 0) {
                    result = titleA.charAt(i)-titleB.charAt(i);
                    break;
                }
            }
        } else {
            for(int i = 0; i < titleA.length(); i++) {
                if(titleA.charAt(i)-titleB.charAt(i) != 0) {
                    result = titleA.charAt(i)-titleB.charAt(i);
                    break;
                }
            }
        }
        return result;
    }
}
