package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
import kashapps.news.saharanpur.models.News;

public class SplashActivity extends AppCompatActivity {

    private TextView cityTitle;
    private FeedHeaderContentResponse feedHeaderContentResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Typeface hindiFont = Typeface.createFromAsset(getAssets(), "fonts/CVKunjBt.ttf");
        cityTitle = (TextView)findViewById(R.id.city_title);
        cityTitle.setTypeface(hindiFont);
        loadFeedHeaderContent();
    }

    private void loadFeedHeaderContent() {
        News.getFeedHeaderContent("Saharanpur News", "1", new News.FeedHeaderLoad() {
            @Override
            public void onFeedHeaderContentLoaded(FeedHeaderContentResponse contentResponse) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("MESSAGE_TYPE", contentResponse.getMessageType());
                intent.putExtra("THOUGHT", contentResponse.getThought().getThought_of_day());
                intent.putExtra("AUTHOR", contentResponse.getThought().getAuthor());
                try {
                    Thread.sleep(5000l);
                }catch (InterruptedException e) {

                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onFeedNotLoaded() {

                Log.e("********************", "Failre in header");
            }

        });
    }
}
