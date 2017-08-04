package kashapps.news.saharanpur.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import kashapps.news.saharanpur.R;

public class ComingSoonActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
        textDescription = (TextView)findViewById(R.id.textDescription);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComingSoonActivity.super.onBackPressed();
            }
        });

        Intent intent = getIntent();
        toolbarTitle.setText(intent.getStringExtra("TITLE"));
        textDescription.setText(intent.getStringExtra("DESCRIPTION"));
    }
}
