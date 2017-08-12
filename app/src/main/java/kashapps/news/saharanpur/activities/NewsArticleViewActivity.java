package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.models.News;

public class NewsArticleViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NEWS_ID = "news_id";
    private String newsId;

    private FeedContent newsArticle;

    private Toolbar toolbar;
    private ProgressBar loading;
    private TextView headline;
    private ImageView shareNews;
    private TextView articleDate;
    private ImageView articleImage;
    private TextView articleDetail;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_view);
        newsId = getIntent().getStringExtra(NEWS_ID);
        initViews();
        loadNewsArticle();
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

    private void initViews()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        loading = (ProgressBar)findViewById(R.id.loading);
        shareNews = (ImageView)findViewById(R.id.share_news);
        headline = (TextView)findViewById(R.id.feed_headline);
        articleDate = (TextView)findViewById(R.id.feed_date);
        articleImage = (ImageView) findViewById(R.id.feed_image);
        articleDetail = (TextView)findViewById(R.id.feed_detail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsArticleViewActivity.super.onBackPressed();
            }
        });

        loading.setVisibility(View.VISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        shareNews.setOnClickListener(this);
    }

    private void loadNewsArticle() {
        News.getSingleNews("Saharanpur", newsId, new News.SingleNewsLoad() {
            @Override
            public void onSingleNewsSuccess(FeedContent feedContent) {
                newsArticle = feedContent;
                setupArticleData();
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onSingleNewsFailure() {
                loading.setVisibility(View.GONE);
            }
        });
    }

    private void setupArticleData() {
        headline.setText(newsArticle.getTitle());
        articleDate.setText(newsArticle.getDate());
        if (!"http://www.jagranimages.com/images/jagran_logo.jpg".equals(newsArticle.getImage()))
            Picasso.with(getApplicationContext()).load(newsArticle.getImage()).into(articleImage);
        articleDetail.setText(newsArticle.getSummary());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_news: if (newsArticle != null) openSharingDialog(); break;
        }
    }

    private void openSharingDialog() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = newsArticle.getTitle() + "\n\n" + newsArticle.getSummary() + "\n\n" + getResources().getString(R.string.app_url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"न्यूज़ शेयर करें");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "न्यूज़ शेयर करें"));
    }
}
