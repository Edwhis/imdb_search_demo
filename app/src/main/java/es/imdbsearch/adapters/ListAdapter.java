package es.imdbsearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.imdbsearch.R;
import es.imdbsearch.models.Movie;

public class ListAdapter extends BaseAdapter {

    private List<Movie> movies = new ArrayList<>();
    private Context mContext;

    public ListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if(view != null){
            holder = (ViewHolder)view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        int posterDim = (int) mContext.getResources().getDimension(R.dimen.poster_size);
        final Movie movie = movies.get(i);
        if(movie != null){
            Picasso.with(mContext)
                    .load(movie.getPoster())
                    .resize(posterDim, posterDim)
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .into(holder.poster);
            holder.title.setText(movie.getTitle());
            holder.description.setText(String.format("%s (%s)", movie.getType(), movie.getYear()));
        }



        return view;
    }

    static class ViewHolder {

        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.year) TextView description;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
