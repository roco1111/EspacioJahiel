package com.rosario.hp.espaciojahiel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerFragment myYouTubePlayerFragment;
    private YouTubePlayerView youTubePlayerView;
    String clave_youtube = "AIzaSyBNmQNhSq4h1AAoS2iz5fSFxDhEfwrFYic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youTubePlayerView = findViewById(R.id.youtube_view);

        youTubePlayerView.initialize(clave_youtube, VideoActivity.this);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fue_restaurado) {
        if(!fue_restaurado){
            youTubePlayer.cueVideo("azxDhcKYku4");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else{
            String error = "Error al inicializar el Youtuube " + youTubeInitializationResult.toString();

            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getYoutubePlayerProvider().initialize(clave_youtube, VideoActivity.this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider(){
        return youTubePlayerView;
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }
}