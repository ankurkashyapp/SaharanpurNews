package kashapps.news.saharanpur.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.adapters.NewsFeedAdapter;
import kashapps.news.saharanpur.adapters.ViewPagerAdapter;
import kashapps.news.saharanpur.api.RestClient;
import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
import kashapps.news.saharanpur.api.responses.NewsFeedResponse;
import kashapps.news.saharanpur.models.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, View.OnClickListener, NewsFeedAdapter.FeedItemClickListener, ViewPagerAdapter.PagerItemClickListener{

    private int page;
    private int startAdFrequency = 1;
    private int endAdFrequency = 5;
    private int currentAdFrequency = 1;
    private boolean shouldIncrease = true;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private SuperRecyclerView feedsView;
    private TextView headerContent;
    private ImageView headerImage;
    private ImageView appsAds;

    private NewsFeedResponse newsFeedResponse;
    private NewsFeedAdapter feedAdapter;
    private FeedContent feedContent;
    private FeedHeaderContentResponse feedHeaderContent;

    private String messageType;
    private String thought;
    private String author;

    private LinearLayout jokesMenu;
    private LinearLayout entertainmentMenu;
    private LinearLayout photoGalleryMenu;
    private LinearLayout appShareMenu;
    private LinearLayout rateAppMenu;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromSplashScreen();
        if (messageType.equals("ALERT"))
            setTheme(R.style.RedTheme);
        setContentView(R.layout.activity_main);
        initViews();
        loadFeeds();
        loadFeedHeaderContent();
        initAds();
    }

    private void initAds() {
        adView = (AdView) findViewById(R.id.bannerAdView);
        displayBannerAd();
    }

    private void displayBannerAd() {
        adView.loadAd(getAdRequest());
        initInterstitialAd();
    }

    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(getAdRequest());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(getAdRequest());
                openNewsArticle();
            }
        });
    }

    private AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("404823443BAEF9FEA7ACD240FE2A003C")
                .build();
        return adRequest;
    }

    private void getDataFromSplashScreen() {
        Intent intent = getIntent();
        messageType = intent.getStringExtra("MESSAGE_TYPE");
        thought = intent.getStringExtra("THOUGHT");
        author = intent.getStringExtra("author");
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        feedsView = (SuperRecyclerView)findViewById(R.id.feeds);
        jokesMenu = (LinearLayout)findViewById(R.id.jokes);
        entertainmentMenu = (LinearLayout)findViewById(R.id.entertainment);
        photoGalleryMenu = (LinearLayout)findViewById(R.id.photo_gallery);
        appShareMenu = (LinearLayout)findViewById(R.id.share_app);
        rateAppMenu = (LinearLayout)findViewById(R.id.rate_app);
        headerContent = (TextView)findViewById(R.id.header_text);
        headerImage = (ImageView)findViewById(R.id.header_image);
        appsAds = (ImageView)findViewById(R.id.apps_ads);

        if (messageType.equals("ALERT")) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                appsAds.setBackgroundDrawable( getResources().getDrawable(R.drawable.ripple_effect_red_color) );
            } else {
                appsAds.setBackground( getResources().getDrawable(R.drawable.ripple_effect_red_color));
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedsView.setLayoutManager(layoutManager);
        headerContent.setSelected(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        drawerLayout.setDrawerListener(this);
        jokesMenu.setOnClickListener(this);
        entertainmentMenu.setOnClickListener(this);
        photoGalleryMenu.setOnClickListener(this);
        appShareMenu.setOnClickListener(this);
        rateAppMenu.setOnClickListener(this);
        appsAds.setOnClickListener(this);
        feedsView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeeds();
            }
        });
    }

    private void loadFeeds() {
        page = 1;
        News.loadNewsFeeds("Saharanpur", String.valueOf(page), new News.FeedsLoaded() {
            @Override
            public void onFeedsLoadSuccess(NewsFeedResponse newsFeedResponse) {
                MainActivity.this.newsFeedResponse = newsFeedResponse;
                feedAdapter = new NewsFeedAdapter(getApplicationContext(), newsFeedResponse, MainActivity.this, MainActivity.this);
                feedsView.setAdapter(feedAdapter);
            }

            @Override
            public void onNewsLoadFailure() {
                Log.e("*********Failure", "Failure");
            }
        });


        feedsView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                News.loadNewsFeeds("Saharanpur", String.valueOf(++page), new News.FeedsLoaded() {
                    @Override
                    public void onFeedsLoadSuccess(NewsFeedResponse newsFeedResponse) {
                        MainActivity.this.newsFeedResponse.getContent().addAll(newsFeedResponse.getContent());
                        feedAdapter.setNewsFeedResponse(MainActivity.this.newsFeedResponse);
                        feedAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNewsLoadFailure() {
                        Log.e("*********Failure", "Failure");
                    }
                });
            }
        }, 1);
    }

    private void loadFeedHeaderContent() {
        if (messageType.equals("ALERT")) {
            headerImage.setVisibility(View.VISIBLE);
            headerContent.setText("ध्यान दें! आप इस ऐप के पुराने संस्करण चला रहे हैं, कृपया ऐप अपडेट करे..");
        }
        else {
            headerContent.setText("आज का शुभ विचार: " + thought);
        }
    }

    void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
    }

    @Override
    public void onFeedItemClick(View view) {
        int position = feedsView.getRecyclerView().getChildLayoutPosition(view);
        this.feedContent = newsFeedResponse.getContent().get(position + NewsFeedAdapter.PAGER_ITEMS_COUNT - 1);
        Toast.makeText(MainActivity.this, feedContent.getTitle(), Toast.LENGTH_SHORT).show();
        showInterstitialAd();

    }

    @Override
    public void onPagerItemClick(View view, int position) {
        Toast.makeText(MainActivity.this, newsFeedResponse.getContent().get(position).getTitle(), Toast.LENGTH_SHORT).show();
        this.feedContent = newsFeedResponse.getContent().get(position);
        showInterstitialAd();
    }

    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            if (currentAdFrequency == startAdFrequency || currentAdFrequency == endAdFrequency)
                mInterstitialAd.show();
            else
                openNewsArticle();

            if (currentAdFrequency == startAdFrequency)
                shouldIncrease = true;
            else if (currentAdFrequency == endAdFrequency)
                shouldIncrease = false;

            if (shouldIncrease)
                currentAdFrequency++;
            else
                currentAdFrequency--;

        } else {
            openNewsArticle();
        }
    }

    private void openNewsArticle() {
        String link = feedContent.getLink();
        String newsId = link.substring(link.lastIndexOf('-')+1, link.lastIndexOf('.'));
        Intent intent = new Intent(MainActivity.this, NewsArticleViewActivity.class);
        intent.putExtra(NewsArticleViewActivity.NEWS_ID, newsId);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jokes: openJokesActivity();break;
            case R.id.entertainment: openEntertainmentActivity(); break;
            case R.id.photo_gallery: openPhotoGalleryActivity(); break;
            case R.id.share_app: openSharingDialog(); break;
            case R.id.apps_ads: openAppsAdsActivity(); break;
        }
    }

    private void openJokesActivity() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        Intent intent = new Intent(MainActivity.this, JokesActivity.class);
        startActivity(intent);
    }

    private void openEntertainmentActivity() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        Intent intent = new Intent(MainActivity.this, ComingSoonActivity.class);
        intent.putExtra("TITLE", "मनोरंजन");
        intent.putExtra("DESCRIPTION", "जुड़े रहिये हमारे साथ। जल्द आ रहा है मनोरंजन का खजाना अब सहारनपुर न्यूज ऐप में");
        startActivity(intent);
    }

    private void openPhotoGalleryActivity() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        Intent intent = new Intent(MainActivity.this, ComingSoonActivity.class);
        intent.putExtra("TITLE", "फोटो गैलरी");
        intent.putExtra("DESCRIPTION", "जुड़े रहिये हमारे साथ। फ़ोटो गैलरी में अब आप अपने सहारनपुर की सुंदर फोटोज को देख व अपलोड कर सकते है");
        startActivity(intent);
    }

    private void openSharingDialog() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Check it out. Your message goes here";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"ऐप शेयर करें");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "ऐप शेयर करें"));
    }

    private void openAppsAdsActivity() {
        startActivity(new Intent(MainActivity.this, AppsAdsActivity.class));
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
