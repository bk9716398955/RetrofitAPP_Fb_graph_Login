package com.techasylum.retrofitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookActivity;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    String picture;

    String Token="";
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = findViewById(R.id.text_view_result);




        Token= Objects.requireNonNull(getIntent().getExtras()).getString("token");



       accessToken=AccessToken.getCurrentAccessToken();




        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {   //add header through interceptor for all function
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyddddddddz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPost();
        // getComment();

        // createPost();
        // UpdatePost();
        //deletePost();
    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(7);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "response" + response.code(), Toast.LENGTH_SHORT).show();

                textViewResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "t error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void UpdatePost() {
        Post post = new Post(265, null, "New Text");


        Call<Post> call = jsonPlaceHolderApi.putPost("heloo", 8, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "response not yet rescieved ", Toast.LENGTH_SHORT).show();

                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Toast.makeText(MainActivity.this, "response rescieved ", Toast.LENGTH_SHORT).show();
                Post postResponse = response.body();
                String content = "";

                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";
                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MainActivity.this, "response not  rescieved ", Toast.LENGTH_SHORT).show();

                textViewResult.setText(t.getMessage());
            }
        });


    }

    private void createPost() {
        Post post = new Post(265, "New Titlelkjk", "New Text");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "response not yet rescieved ", Toast.LENGTH_SHORT).show();

                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Toast.makeText(MainActivity.this, "response rescieved ", Toast.LENGTH_SHORT).show();
                Post postResponse = response.body();


                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";
                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MainActivity.this, "response not  rescieved ", Toast.LENGTH_SHORT).show();

                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComment() {

        Call<List<Comment>> call = jsonPlaceHolderApi.getComment("https://jsonplaceholder.typicode.com/comments?postId=1");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";
                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getPost() {


        String url="https://graph.facebook.com/me?fields=feed.limit(30){picture,created_time,story}&access_token=";





        Call<JsonObject> call = jsonPlaceHolderApi.getPosts((url+""+Token));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                  textViewResult.setText("Code: " + response.code());
                    return;
                }


                JsonObject mainObject = response.body();
                assert mainObject != null;
                JsonObject second = mainObject.getAsJsonObject("feed");
                JsonArray jsonArray = second.getAsJsonArray("data");
                try {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject innerResult = (JsonObject) jsonArray.get(i);
                        String content = "";

                        content += "ID: " + innerResult.get("id").toString() + "\n";

                        content += "date" + innerResult.get("created_time").toString() + "\n";
                        textViewResult.append(i+"\n"+content);
                       picture= innerResult.get("picture").toString() + "\n\n";
                       if(!picture.isEmpty()) {
                           textViewResult.append(picture);
                       }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                @Override
                public void onFailure (Call < JsonObject > call, Throwable t){
                    textViewResult.setText(t.getMessage());
                }

        });


    }


}
