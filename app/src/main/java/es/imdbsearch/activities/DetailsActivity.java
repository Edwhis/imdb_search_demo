package es.imdbsearch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.imdbsearch.R;
import es.imdbsearch.models.MovieDetails;
import es.imdbsearch.services.MovieService;

public class DetailsActivity extends AppCompatActivity implements MovieService.DetailsCallback {

    private final static String EXTRA_IMDB_ID = "imdb_id";

    @BindView(R.id.poster) ImageView mPoster;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.year) TextView mYear;
    @BindView(R.id.genre) TextView mGenre;
    @BindView(R.id.type) TextView mType;
    @BindView(R.id.plot) TextView mPlot;
    @BindView(R.id.content) LinearLayout mContent;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;
    @BindView(R.id.rating) TextView mRating;

    public static Intent newIntent(Context context, String imdbId) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(EXTRA_IMDB_ID, imdbId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showProgress(true);
        MovieService.getDetails(getIntent().getStringExtra(EXTRA_IMDB_ID), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            mContent.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mContent.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(MovieDetails details) {
        showProgress(false);
        Picasso.with(this)
                .load(details.getPoster())
                .error(R.drawable.no_image)
                .into(mPoster);

        mTitle.setText(details.getTitle());
        mYear.setText(details.getYear());
        mGenre.setText(details.getGenre());
        mType.setText(details.getType());
        mPlot.setText(details.getPlot());
        mRatingBar.setRating(details.getImdbRating());
        mRating.setText(String.format("%s/10", details.getImdbRating()));
    }

    @Override
    public void onError() {
        Toast.makeText(this, getString(R.string.details_error), Toast.LENGTH_LONG).show();
        showProgress(false);
    }
}
