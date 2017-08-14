package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.ads.MobileAds;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
import kashapps.news.saharanpur.models.News;

import com.facebook.FacebookSdk;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = (ProgressBar)findViewById(R.id.progressBar);
        MobileAds.initialize(this, "ca-app-pub-9635370788019972~2952124061");
        loading.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        loadFeedHeaderContent();
    }

    private void loadFeedHeaderContent() {
        News.getFeedHeaderContent("Saharanpur News", getCurrentVersionCode(), new News.FeedHeaderLoad() {
            @Override
            public void onFeedHeaderContentLoaded(FeedHeaderContentResponse contentResponse) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("MESSAGE_TYPE", contentResponse.getMessageType());
                intent.putExtra("THOUGHT", contentResponse.getThought().getThought_of_day());
                intent.putExtra("AUTHOR", contentResponse.getThought().getAuthor());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFeedNotLoaded() {

                Log.e("********************", "Failre in header");
            }

        });
    }

    private String getCurrentVersionCode() {
        int versionCode = 1;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(versionCode);
    }
}
