package com.outbit.scrollablelist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import static com.outbit.scrollablelist.MainActivity.recyclerAdapter;
import static com.outbit.scrollablelist.MainActivity.remember;
import static com.outbit.scrollablelist.MainActivity.seekBar;
import static com.outbit.scrollablelist.MainActivity.mp;
import static com.outbit.scrollablelist.MainActivity.selectSong;
import static com.outbit.scrollablelist.MainActivity.recyclerView;


public class PlayerActivity extends AppCompatActivity {


    Runnable runnable;
    Handler handler;
    public static ImageButton displayNB;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_player );
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int SCREEN_HEIGHT = displayMetrics.heightPixels;
        int SCREEN_WIDTH = displayMetrics.widthPixels;


        Intent intent = getIntent();

        TextView displaySongTitle = (TextView) findViewById(R.id.textView123);
        final ImageButton displayPP = (ImageButton) findViewById( R.id.imageButtonPP );
        displayNB = (ImageButton) findViewById( R.id.imageButtonNext );
        final ImageButton displayPB = (ImageButton) findViewById( R.id.imageButtonPrevious );
        String title = remember.getTitle();
        displaySongTitle.setText(remember.getTitle().substring( 0, title.lastIndexOf( '.' ) ) );
        displaySongTitle.setTranslationY(((SCREEN_HEIGHT * 100) / 220));
        displayPP.setTranslationY(((SCREEN_HEIGHT * 100) / 150));
        displayNB.setTranslationY(((SCREEN_HEIGHT * 100) / 150));
        displayPB.setTranslationY(((SCREEN_HEIGHT * 100) / 150));


        TextView displayBack = (TextView) findViewById(R.id.bottomExit);
        //Rect bounds = new Rect();
        //displayBack.getPaint().getTextBounds(displayBack.getText(), 0,displayBack.getText().length(), bounds);
        //bounds.height(); //This should give you the height of the wrapped_content
        //displayBack.setTranslationY(SCREEN_HEIGHT - bounds.height()*100);


        String songPath = intent.getStringExtra( remember.getFileInfo().getName() );
        File directory = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_MUSIC );

        // Create a retriever
        MediaMetadataRetriever extractMusicInfo = (MediaMetadataRetriever) new MediaMetadataRetriever();
        extractMusicInfo.setDataSource( directory + "/" + remember.getTitle() );

        // Retrieve the artist name
        String artist = extractMusicInfo.extractMetadata( MediaMetadataRetriever.METADATA_KEY_ARTIST );
        if (artist == null || artist.equals( "" )) {
            artist = "Artist Unknown";
        }

        // Retrieve the artist name
        String durationStr = extractMusicInfo.extractMetadata( MediaMetadataRetriever.METADATA_KEY_DURATION );
        int millSecond = Integer.parseInt( durationStr );
        if(mp.isPlaying()) {
            displayPP.setImageResource( R.drawable.pauseicon96 );
        } else {
            displayPP.setImageResource( R.drawable.playicon96 );
        }
        seekBar = findViewById( R.id.seekBar );
        seekBar.getProgressDrawable().setColorFilter(
                Color.rgb(255,255,255), android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.setTranslationY( ((SCREEN_HEIGHT * 100) / 180));
        seekBar.setMax(mp.getDuration());
        seekBar.setProgress(0);
        seekBar.setProgress( mp.getCurrentPosition() );




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
//recyclerView.findViewHolderForAdapterPosition(currentPosition + 1).itemView.performClick();
        displayPP.setOnClickListener( new ImageButton.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()) {
                    mp.pause();
                    displayPP.setImageResource( R.drawable.playicon96 );


                } else {
                    mp.start();
                    displayPP.setImageResource( R.drawable.pauseicon96 );


                }
            }


        });

        displayBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openActivity1();

            }

        });


    }
    public void openActivity1() {
        onBackPressed();
    }

}
