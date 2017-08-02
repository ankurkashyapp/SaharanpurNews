package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.adapters.JokesListAdapter;
import kashapps.news.saharanpur.api.responses.JokesResponse;
import kashapps.news.saharanpur.models.News;

public class JokesActivity extends AppCompatActivity implements JokesListAdapter.JokeItemClickListener{
    private int page;

    private Toolbar toolbar;
    private SuperRecyclerView jokesList;

    private List<JokesResponse> jokesResponses;
    private JokesListAdapter jokesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);
        initViews();
        loadAllJokes();

    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        jokesList = (SuperRecyclerView)findViewById(R.id.jokes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        jokesList.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JokesActivity.super.onBackPressed();
            }
        });
        jokesList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllJokes();
            }
        });
    }

    private void loadAllJokes() {
        page = 1;
        News.getAllJokes(String.valueOf(page), new News.JokesLoaded() {
            @Override
            public void onJokesLoadSuccess(List<JokesResponse> jokesResponses) {
                JokesActivity.this.jokesResponses = jokesResponses;
                jokesListAdapter = new JokesListAdapter(JokesActivity.this, jokesResponses, JokesActivity.this);
                jokesList.setAdapter(jokesListAdapter);
            }

            @Override
            public void onJokesLoadFailure() {

            }
        });

        jokesList.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                News.getAllJokes(String.valueOf(++page), new News.JokesLoaded() {
                    @Override
                    public void onJokesLoadSuccess(List<JokesResponse> jokesResponses) {
                        JokesActivity.this.jokesResponses.addAll(jokesResponses);
                        jokesListAdapter.setJokesList(JokesActivity.this.jokesResponses);
                        jokesListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onJokesLoadFailure() {
                        Log.e("*********Failure", "Failure in loading the jokes");
                    }
                });
            }
        }, 1);
    }

    @Override
    public void onJokeClick(View itemView) {
        int position = jokesList.getRecyclerView().getChildLayoutPosition(itemView);
        JokesResponse jokesResponse = jokesResponses.get(position);
        String link = jokesResponse.getLink();
        String jokeId = link.substring(link.lastIndexOf('-')+1, link.lastIndexOf('.'));
        String title = jokesResponse.getTitle();
        Intent intent = new Intent(JokesActivity.this, JokeViewActivity.class);
        intent.putExtra("JOKE_ID", jokeId);
        intent.putExtra("JOKE_TITLE", title);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), jokesResponse.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
