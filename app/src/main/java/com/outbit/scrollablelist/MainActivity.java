package com.outbit.scrollablelist;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;

// Access Public Storage
import android.os.Environment;

// Allow the use of LogCat
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

// Used for getting music directory
import java.io.File;

// Put songs and details into a list
import java.util.ArrayList;

// Help sort songs
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    private static int SPLASH_TIMEOUT = 4000;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check for permission
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get directory to the Android Music folder
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // Log the path of the directory
        Log.v("BEFORE", directory.getPath());

        // Create a song list
        // Stores information about each song
        ArrayList<SongInfo> songList= new ArrayList<>();

        // For every file in the directory...
        for(File i : directory.listFiles()) {

            // Get path of the file
            String path = i.getAbsolutePath();

            // If the file naming ends with ".mp3"
            if(path.endsWith(".mp3")) {

                // Create new data for songInfo
                SongInfo data = new SongInfo();

                // Create a retriever
                MediaMetadataRetriever extractMusicInfo = (MediaMetadataRetriever) new MediaMetadataRetriever();
                extractMusicInfo.setDataSource(directory+ "/" + i.getName());

                // Retrieve the artist name
                String artist = extractMusicInfo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                if (artist == null || artist.equals("")) {
                    artist = "Artist Unknown";
                }

                // Setters for songInfo and add it into the songList
                data.setTitle(i.getName());
                data.setArtist(artist);
                data.setFileInfo(i);
                songList.add(data);
            }
        }

        // Sort the song list using compareTo() and comparable
        Collections.sort(songList);

        recyclerView = findViewById(R.id.recyclerView);

        // Put songList into RecyclerAdapter (to put in the information into UI)
        recyclerAdapter = new RecyclerAdapter(songList);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerAdapter);
    }
}

class SongInfo implements Comparable<SongInfo> {
    private String title;
    private String artist;
    private File fileInfo;

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


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public File getFileInfo() {
        return fileInfo;
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
