package com.example.postviewer;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView textTitle, textBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textTitle = findViewById(R.id.textTitle);
        textBody = findViewById(R.id.textBody);

        int postId = getIntent().getIntExtra("postId", -1);

        if (postId != -1) {
            RetrofitClient.getApiService().getPostById(postId).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        Post post = response.body();
                        textTitle.setText(post.getTitle());
                        textBody.setText(post.getBody());
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
