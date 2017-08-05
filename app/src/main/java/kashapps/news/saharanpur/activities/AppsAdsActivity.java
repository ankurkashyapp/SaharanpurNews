package kashapps.news.saharanpur.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;

import kashapps.news.saharanpur.R;

public class AppsAdsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout adViewer;
    private NativeExpressAdView adView;
    private NativeExpressAdView adView1;
    private NativeExpressAdView adView2;
    private NativeExpressAdView adView3;
    private NativeExpressAdView adView4;
    private NativeExpressAdView adView5;
    private NativeExpressAdView adView6;
    private NativeExpressAdView adView7;
    private NativeExpressAdView adView8;
    private NativeExpressAdView adView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_ads);
        initViews();
        loadAds();
    }
    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        adViewer = (RelativeLayout)findViewById(R.id.ad_viewer);
        adView = (NativeExpressAdView)findViewById(R.id.adView);
        adView1 = (NativeExpressAdView)findViewById(R.id.adView1);
        adView2 = (NativeExpressAdView)findViewById(R.id.adView2);
        adView3 = (NativeExpressAdView)findViewById(R.id.adView3);
        adView4 = (NativeExpressAdView)findViewById(R.id.adView4);
        adView5 = (NativeExpressAdView)findViewById(R.id.adView5);
        adView6 = (NativeExpressAdView)findViewById(R.id.adView6);
        adView7 = (NativeExpressAdView)findViewById(R.id.adView7);
        adView8 = (NativeExpressAdView)findViewById(R.id.adView8);
        adView9 = (NativeExpressAdView)findViewById(R.id.adView9);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppsAdsActivity.super.onBackPressed();
            }
        });
        /*adView.setAdUnitId(getResources().getString(R.string.native_add_unit_id));
        adView1.setAdUnitId(getResources().getString(R.string.native_add_unit_id));
        adView2.setAdUnitId(getResources().getString(R.string.native_add_unit_id));
        adView3.setAdUnitId(getResources().getString(R.string.native_add_unit_id));
        adView4.setAdUnitId(getResources().getString(R.string.native_add_unit_id));
        adView5.setAdUnitId(getResources().getString(R.string.native_add_unit_id));

        adView.setAdSize(getAdSize());
        adView1.setAdSize(getAdSize());
        adView2.setAdSize(getAdSize());
        adView3.setAdSize(getAdSize());
        adView4.setAdSize(getAdSize());
        adView5.setAdSize(getAdSize());*/
    }

    private void loadAds() {
        adView.loadAd(getAdRequest());
        adView1.loadAd(getAdRequest());
        adView2.loadAd(getAdRequest());
        adView3.loadAd(getAdRequest());
        adView4.loadAd(getAdRequest());
        adView5.loadAd(getAdRequest());
        adView6.loadAd(getAdRequest());
        adView7.loadAd(getAdRequest());
        adView8.loadAd(getAdRequest());
        adView9.loadAd(getAdRequest());
    }

    private AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("404823443BAEF9FEA7ACD240FE2A003C")
                .build();
        return adRequest;
    }

    private AdSize getAdSize() {
        float density = AppsAdsActivity.this.getResources().getDisplayMetrics().density;
        AdSize adSize = new AdSize((int)(adViewer.getWidth()/density), 100);
        return adSize;
    }
}
