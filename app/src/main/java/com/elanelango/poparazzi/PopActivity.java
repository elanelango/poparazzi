package com.elanelango.poparazzi;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PopActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Photo> photos;
    private PhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        photos = new ArrayList<>();
        aPhotos = new PhotosAdapter(this, photos);

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aPhotos.clear();
                fetchPopularPhotos();
            }
        });
        fetchPopularPhotos();
    }

    // Used for testing
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("test_response.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    /*String jsonStr = loadJSONFromAsset();
                    photosJSON = new JSONObject(jsonStr).getJSONArray("data");*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("PopActivity", "data not found");
                }

                for (int i = 0; i < photosJSON.length(); i++) {
                    try {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        Photo photo = new Photo();

                        photo.profilePicURL = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.createdTime = photoJSON.getInt("created_time");
                        photo.caption = !photoJSON.isNull("caption") ? photoJSON.getJSONObject("caption").getString("text") : "";
                        photo.type = photoJSON.getString("type");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        if (photo.type.equals("video")) {
                            photo.videoUrl = photoJSON.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                            photo.videoWidth = photoJSON.getJSONObject("videos").getJSONObject("standard_resolution").getInt("width");
                            photo.videoHeight = photoJSON.getJSONObject("videos").getJSONObject("standard_resolution").getInt("height");
                        }

                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                        for (int j = 0; j < commentsJSON.length(); j++) {
                            JSONObject commentJSON = commentsJSON.getJSONObject(j);
                            Comment comment = new Comment();
                            comment.time = commentJSON.getInt("created_time");
                            comment.username = commentJSON.getJSONObject("from").getString("username");
                            comment.text = commentJSON.getString("text");
                            photo.comments.add(comment);
                        }

                        photos.add(photo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("INFO", "Fetch timeline error: " + throwable.toString());
            }
        });
    }
}
