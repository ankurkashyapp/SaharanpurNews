package kashapps.news.saharanpur.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.models.News;

public class NewsArticleViewActivity extends AppCompatActivity {
    public static final String NEWS_ID = "news_id";
    private String newsId;

    private FeedContent newsArticle;

    private Toolbar toolbar;
    private ProgressBar loading;
    private TextView headline;
    private TextView articleDate;
    private ImageView articleImage;
    private TextView articleDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_view);
        newsId = getIntent().getStringExtra(NEWS_ID);
        initViews();
        loadNewsArticle();

    }

    private void initViews()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        loading = (ProgressBar)findViewById(R.id.loading);
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
        Picasso.with(getApplicationContext()).load(newsArticle.getImage()).into(articleImage);
        articleDetail.setText(newsArticle.getSummary());
    }
}
