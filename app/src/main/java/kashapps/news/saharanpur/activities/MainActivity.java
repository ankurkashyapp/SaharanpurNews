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

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, View.OnClickListener, NewsFeedAdapter.FeedItemClickListener, ViewPagerAdapter.PagerItemClickListener {

    private int page;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SuperRecyclerView feedsView;
    private Button logout;
    private TextView headerContent;
    private ImageView headerImage;

    private NewsFeedResponse newsFeedResponse;
    private NewsFeedAdapter feedAdapter;
    private FeedHeaderContentResponse feedHeaderContent;

    private String messageType;
    private String thought;
    private String author;

    private RelativeLayout drawerView;
    private LinearLayout jokesMenu;


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
        drawerView = (RelativeLayout)findViewById(R.id.drawer_view);
        jokesMenu = (LinearLayout)findViewById(R.id.jokes);
        headerContent = (TextView)findViewById(R.id.header_text);
        headerImage = (ImageView)findViewById(R.id.header_image);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedsView.setLayoutManager(layoutManager);
        headerContent.setSelected(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        drawerLayout.setDrawerListener(this);
        jokesMenu.setOnClickListener(this);
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
        /*News.getFeedHeaderContent("Saharanpur News", "1", new News.FeedHeaderLoad() {
            @Override
            public void onFeedHeaderContentLoaded(FeedHeaderContentResponse contentResponse) {
                headerContent.setText(contentResponse.getThought().getThought_of_day());
            }

            @Override
            public void onFeedNotLoaded() {
                Log.e("********************", "Failre in header");
            }
        });*/
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
        FeedContent feedContent = newsFeedResponse.getContent().get(position + NewsFeedAdapter.PAGER_ITEMS_COUNT - 1);
        Toast.makeText(MainActivity.this, feedContent.getTitle(), Toast.LENGTH_SHORT).show();

        openNewsArticle(feedContent);

    }

    @Override
    public void onPagerItemClick(View view, int position) {
        Toast.makeText(MainActivity.this, newsFeedResponse.getContent().get(position).getTitle(), Toast.LENGTH_SHORT).show();
        FeedContent feedContent = newsFeedResponse.getContent().get(position);
        openNewsArticle(feedContent);
    }

    private void openNewsArticle(FeedContent feedContent) {
        String link = feedContent.getLink();
        String newsId = link.substring(link.lastIndexOf('-')+1, link.lastIndexOf('.'));
        Intent intent = new Intent(MainActivity.this, NewsArticleViewActivity.class);
        intent.putExtra(NewsArticleViewActivity.NEWS_ID, newsId);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == jokesMenu) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(MainActivity.this, JokesActivity.class);
            startActivity(intent);
        }
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
