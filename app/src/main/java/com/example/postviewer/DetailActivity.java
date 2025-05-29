package com.example.postviewer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.postviewer.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView headerTextView, titleTextView, bodyTextView, userIdTextView, idTextView;
    private ProgressBar progressBar;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inisialisasi views
        headerTextView = findViewById(R.id.headerTextView);
        titleTextView = findViewById(R.id.titleTextView);
        bodyTextView = findViewById(R.id.bodyTextView);
        userIdTextView = findViewById(R.id.userIdTextView);
        idTextView = findViewById(R.id.idTextView);
        progressBar = findViewById(R.id.progressBar);

        // GANTI DENGAN NIM DAN NAMA ANDA!
        headerTextView.setText("NIM: 235150707111009\nNama: Antike Rahma Safira");

        // Get post ID dari intent
        int postId = getIntent().getIntExtra("POST_ID", 0);
        Log.d(TAG, "Post ID diterima: " + postId);

        if (postId != 0) {
            loadPostDetail(postId);
        } else {
            Toast.makeText(this, "Error: Post ID tidak valid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadPostDetail(int postId) {
        Log.d(TAG, "Loading detail untuk post ID: " + postId);
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Post> call = apiService.getPostById(postId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    Log.d(TAG, "Detail post berhasil dimuat: " + post.getTitle());
                    displayPost(post);
                } else {
                    Log.e(TAG, "Error loading post detail: " + response.code());
                    Toast.makeText(DetailActivity.this,
                            "Error loading post detail",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(DetailActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPost(Post post) {
        titleTextView.setText(post.getTitle());
        bodyTextView.setText(post.getBody());
        userIdTextView.setText("User ID: " + post.getUserId());
        idTextView.setText("Post ID: " + post.getId());
    }
}