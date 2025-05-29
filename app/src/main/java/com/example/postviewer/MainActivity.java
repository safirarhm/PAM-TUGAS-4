package com.example.postviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.postviewer.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView headerTextView;
    private ProgressBar progressBar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi views
        headerTextView = findViewById(R.id.headerTextView);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // GANTI DENGAN NIM DAN NAMA ANDA!
        headerTextView.setText("NIM: 235150707111009\nNama: Antike Rahma Safira");

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data dari API
        loadPostsFromApi();
    }

    private void loadPostsFromApi() {
        Log.d(TAG, "Memulai load posts dari API");
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Post>> call = apiService.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "Response diterima. Success: " + response.isSuccessful());
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    Log.d(TAG, "Jumlah posts diterima: " + posts.size());

                    // Debug: tampilkan beberapa post pertama
                    for (int i = 0; i < Math.min(3, posts.size()); i++) {
                        Post post = posts.get(i);
                        Log.d(TAG, "Post " + i + ": ID=" + post.getId() +
                                ", Title=" + post.getTitle().substring(0, Math.min(50, post.getTitle().length())));
                    }

                    setupRecyclerView(posts);
                } else {
                    Log.e(TAG, "Response tidak berhasil atau body null");
                    Toast.makeText(MainActivity.this,
                            "Error loading posts: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView(List<Post> posts) {
        PostAdapter adapter = new PostAdapter(posts, post -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("POST_ID", post.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}