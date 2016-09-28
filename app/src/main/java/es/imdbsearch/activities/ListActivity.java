package es.imdbsearch.activities;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.imdbsearch.R;
import es.imdbsearch.adapters.ListAdapter;
import es.imdbsearch.models.Movie;
import es.imdbsearch.services.MovieService;

public class ListActivity extends AppCompatActivity implements MovieService.ListSearchCallback {

    @BindView(R.id.list) ListView mList;
    @BindView(R.id.progress) ProgressBar mProgressBar;
    @BindView(R.id.search_text) TextView mSearchText;
    @BindView(R.id.list_container) RelativeLayout mListContainer;
    @BindView(R.id.no_results) TextView mNoResults;
    private ListAdapter mAdapter;
    private String mSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        mAdapter = new ListAdapter(this);
        mList.setAdapter(mAdapter);
        mList.setEmptyView(mNoResults);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String imdbId = mAdapter.getItem(i).getImdbId();
                startActivity(DetailsActivity.newIntent(ListActivity.this, imdbId));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                MenuItemCompat.collapseActionView(searchItem);
                mSearchText.setVisibility(View.GONE);
                mSearchQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void doSearch(String searchQuery){
        showProgress(true);
        MovieService.getList(searchQuery, this);
    }

    private void showProgress(boolean show){
        if(show){
            mProgressBar.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        if(movies.size() > 0) {
            mAdapter.setData(movies);
        } else {
            mNoResults.setText(String.format(getString(R.string.no_results), mSearchQuery));
        }
        showProgress(false);
    }

    @Override
    public void onError() {
        mAdapter.clear();
        Toast.makeText(this, getString(R.string.search_error), Toast.LENGTH_LONG).show();
        showProgress(false);
    }
}
