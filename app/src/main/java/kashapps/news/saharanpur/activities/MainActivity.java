package kashapps.news.saharanpur.activities;

import android.app.ProgressDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.adapters.NewsFeedAdapter;
import kashapps.news.saharanpur.api.RestClient;
import kashapps.news.saharanpur.api.responses.NewsFeedResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int page;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SuperRecyclerView feedsView;
    private Button logout;

    private ProgressDialog progressDialog;

    private String imageUrl, imageName;

    private NewsFeedResponse newsFeedResponse;
    private NewsFeedAdapter feedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadFeeds();

    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        //recyclerView = (RecyclerView)findViewById(R.id.drawer_view);
        feedsView = (SuperRecyclerView)findViewById(R.id.feeds);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedsView.setLayoutManager(layoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
    }

    private void loadFeeds() {
        page = 2;
        Call<NewsFeedResponse> call = RestClient.get().getNewsFeed("Saharanpur", String.valueOf(page));
        call.enqueue(new Callback<NewsFeedResponse>() {
            @Override
            public void onResponse(Call<NewsFeedResponse> call, Response<NewsFeedResponse> response) {
                newsFeedResponse = response.body();
                Log.e("**************Response", response.body().getMessage());
                Log.e("**************Response", response.body().getContent().get(0).getTitle());
                feedAdapter = new NewsFeedAdapter(getApplicationContext(), response.body());
                feedsView.setAdapter(feedAdapter);
            }

            @Override
            public void onFailure(Call<NewsFeedResponse> call, Throwable t) {
                Log.e("*********Failure", "Failure");
            }
        });
        feedsView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                Call<NewsFeedResponse> call = RestClient.get().getNewsFeed("Saharanpur", String.valueOf(page++));
                call.enqueue(new Callback<NewsFeedResponse>() {
                    @Override
                    public void onResponse(Call<NewsFeedResponse> call, Response<NewsFeedResponse> response) {
                        newsFeedResponse.getContent().addAll(response.body().getContent());
                        feedAdapter.setNewsFeedResponse(newsFeedResponse);
                        feedAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<NewsFeedResponse> call, Throwable t) {
                        Log.e("*********Failure", "Failure");
                    }
                });
            }
        }, 1);
    }

    void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
    }
}
