package kashapps.news.saharanpur.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;

import kashapps.news.saharanpur.R;

public class AppsAdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_ads);
        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        adView.loadAd(getAdRequest());
        adView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());


    }

    private AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("404823443BAEF9FEA7ACD240FE2A003C")
                .build();
        return adRequest;
    }
}
