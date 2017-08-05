package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
import kashapps.news.saharanpur.models.News;

public class SplashActivity extends AppCompatActivity {

    private FeedHeaderContentResponse feedHeaderContentResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MobileAds.initialize(this, "ca-app-pub-9635370788019972~2952124061");
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
                //Thread thread = new Thread();
                /*try {

                    //thread.start();
                    //thread.wait(5000l);
                }catch (InterruptedException e) {

                }
                finally {
                    //thread.stop();
                }*/
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
