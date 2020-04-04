package com.rosario.hp.espaciojahiel.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.rosario.hp.espaciojahiel.Entidades.videoYoutube;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listaVideosPrograma extends Fragment implements YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener, YouTubePlayer.OnInitializedListener ,
        YouTubePlayer.OnFullscreenListener{
    YouTubePlayerSupportFragmentX playerFragment;
    public static final String ARG_ARTICLES_NUMBER = "videos";
    YouTubePlayer Player;
    YouTubeThumbnailView thumbnailView;
    YouTubeThumbnailLoader thumbnailLoader;
    RecyclerView VideoList;
    VideoListAdapter adapter;
    List<Drawable> thumbnailViews;
    List<String> VideoId;
    List<String>  videosId;
    List<String> titulos;
    String video;

    List<VideoListAdapter.MyView> mItems;
    private ArrayList<videoYoutube> videos;
    boolean lb_principal;

    public Context context;
    private static final String TAG = listaVideosPrograma.class.getSimpleName();

    private boolean mAutoRotation = false;
    private YouTubePlayer.OnFullscreenListener fullScreenListener = null;
    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lista_videos, container, false);
        context = getContext();
        mAutoRotation = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        fullScreenListener = this;
        thumbnailViews = new ArrayList<>();

        mItems = new ArrayList<>();
        VideoId = new ArrayList<>();
        titulos = new ArrayList<>();
        videosId = new ArrayList<>();
        VideoList = v.findViewById(R.id.VideoList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        VideoList.setLayoutManager(manager);
        VideoList.setHasFixedSize(true);
        adapter=new VideoListAdapter();
        videos = new ArrayList<>();
        VideoList.setAdapter(adapter);
        thumbnailView = new YouTubeThumbnailView(context);
        thumbnailView.initialize("AIzaSyBNmQNhSq4h1AAoS2iz5fSFxDhEfwrFYic", this);
        playerFragment = YouTubePlayerSupportFragmentX.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_fragment, playerFragment);
        transaction.commit();
        playerFragment.initialize("AIzaSyBNmQNhSq4h1AAoS2iz5fSFxDhEfwrFYic", this);
        return v;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        Player=youTubePlayer;
        Player.setShowFullscreenButton(false);
        Player.setOnFullscreenListener(fullScreenListener);

        if (mAutoRotation) {
            youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        } else {
            youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }
        if (!b) {
            VideoList.setVisibility(b?View.GONE:View.VISIBLE);
        }

        Player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                VideoList.setVisibility(b?View.GONE:View.VISIBLE);
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
        thumbnailViews.add(youTubeThumbnailView.getDrawable());
        VideoId.add(s);
        add();
    }


    public void add() {
        adapter.notifyDataSetChanged();
        if (thumbnailLoader.hasNext())
            thumbnailLoader.next();
    }


    @Override
    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
        Log.d("error carga",errorReason.toString());
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        thumbnailLoader = youTubeThumbnailLoader;
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(listaVideosPrograma.this);
        thumbnailLoader.setPlaylist("PLGTOah0usxhHULVKpey8oow_d1jCJwwag",1);
        sacarJsonInfoVideo();

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d("error inicio",youTubeInitializationResult.toString());
    }

    @Override
    public void onFullscreen(boolean b) {
        if (b) {
            //getActivity().setRequestedOrientation(LANDSCAPE_ORIENTATION);

            Player.cueVideo(video);
        } else {
            //getActivity().setRequestedOrientation(PORTRAIT_ORIENTATION);
            Player.cueVideo(video);
        }


    }

    public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyView>{



        public class MyView extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView texto_titulo;
            public MyView(View itemView) {
                super(itemView);

                imageView=  itemView.findViewById(R.id.thumbnailView);
                texto_titulo = itemView.findViewById(R.id.texto_titulo);
            }

        }


        @Override
        public VideoListAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
            return new MyView(itemView);
        }

        @Override
        public void onBindViewHolder(VideoListAdapter.MyView holder, final int position) {
            if(!lb_principal){
                Player.cueVideo(VideoId.get(position));
                lb_principal = true;
            }else {
                mItems.add(holder);
                holder.imageView.setImageDrawable(thumbnailViews.get(position));
            }

            if(titulos.size() > 0) {

                Drawable post;

                post = thumbnailViews.get(position);

                if(post != null) {
                    holder.texto_titulo.setText(titulos.get(position));
                }
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player.cueVideo(VideoId.get(position));
                    video = VideoId.get(position);

                }
            });
        }

        @Override
        public int getItemCount() {
            return thumbnailViews.size();
        }

        public VideoListAdapter.MyView getItem(int position) {

            return mItems.get(position);
        }
    }

    public void sacarJsonInfoVideo() {
        // Petición GET

        String newURL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLGTOah0usxhHULVKpey8oow_d1jCJwwag&key=AIzaSyBNmQNhSq4h1AAoS2iz5fSFxDhEfwrFYic" ;
        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
                                null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }
    private void procesarRespuesta(JSONObject response) {
        try {

            String titulo = "";
            JSONObject jsonObject = new JSONObject(response.toString(0));

            JSONArray jsonArray=jsonObject.getJSONArray("items");
            for(int i = 0; i<= jsonArray.length();i++) {

                titulo = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("title");
                titulos.add(titulo);

               // String video1 = jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONArray("thumbnails").getJSONArray("thumbnails").getString("url");
                // videosId.add(video1);



            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }
}