package com.xxxgreen.mvx.golfdotzio;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_2;

public class SearchResultsActivity extends AppCompatActivity {
    private List<Style> styleResults;
    private RecyclerView recyclerView;
    private XRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            styleResults = new ArrayList<>();

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerAdapter = new XRecyclerAdapter(styleResults, this, COL_2, query);
            recyclerAdapter.setOnItemClickListener(new XRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, String s) {
                    Style style = recyclerAdapter.getItem(position);
                    Intent intent = new Intent(SearchResultsActivity.this, StyleActivity.class);
                    intent.putExtra("style", style);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(recyclerAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
