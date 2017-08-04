package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.SingleJokeResponse;
import kashapps.news.saharanpur.models.News;

public class JokeViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private ImageView shareJoke;
    private ProgressBar loading;

    private TextView title;
    private TextView jokeDescription;

    private String jokeTitle;
    private String jokeId;

    private SingleJokeResponse singleJokeResponse;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_view);
        initViews();
        loadJoke();
        initAds();
    }

    private void initAds() {
        adView = (AdView) findViewById(R.id.bannerAdView);
        displayBannerAd();
    }

    private void displayBannerAd() {
        adView.loadAd(getAdRequest());
    }

    private AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("404823443BAEF9FEA7ACD240FE2A003C")
                .build();
        return adRequest;
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imageView = (ImageView)findViewById(R.id.img_pager_item);
        shareJoke = (ImageView)findViewById(R.id.share_joke);
        loading = (ProgressBar)findViewById(R.id.loading);
        title = (TextView)findViewById(R.id.title_pager_item);
        jokeDescription = (TextView)findViewById(R.id.joke_description);
        Intent intent = getIntent();
        jokeTitle = intent.getStringExtra("JOKE_TITLE");
        jokeId = intent.getStringExtra("JOKE_ID");
        title.setText(jokeTitle);

        shareJoke.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JokeViewActivity.super.onBackPressed();
            }
        });
        loading.setVisibility(View.VISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    private void loadJoke() {
        News.getSingleJoke(jokeId, new News.SingleJokeLoaded() {
            @Override
            public void onSingleJokeLoadSuccess(SingleJokeResponse singleJokeResponse) {
                JokeViewActivity.this.singleJokeResponse = singleJokeResponse;
                Picasso.with(getApplicationContext()).load(singleJokeResponse.getImage()).into(imageView);
                jokeDescription.setText(singleJokeResponse.getJoke_content());
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onSingleJokeLoadFailure() {
                Log.e("*********Failure", "Failure in loading single joke");
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_joke: if (singleJokeResponse != null) openSharingDialog(); break;
        }
    }

    private void openSharingDialog() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = singleJokeResponse.getJoke_content();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"ऐप शेयर करें");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "ऐप शेयर करें"));
    }
}
