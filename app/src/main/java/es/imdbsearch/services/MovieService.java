package es.imdbsearch.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.imdbsearch.models.Movie;
import es.imdbsearch.models.MovieDetails;


public class MovieService {

    private static final String LIST_URL =  "http://www.omdbapi.com/?s=%s";
    private static final String DETAILS_URL =  "http://www.omdbapi.com/?i=%s";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getList(String searchText, final ListSearchCallback callback) {
        final String URL = String.format(LIST_URL, searchText);
        client.get(URL, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                List<Movie> movies = new ArrayList<>();

                try {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject)jsonParser.parse(response.toString());
                    for (JsonElement movieJson : gsonObject.getAsJsonArray("Search")) {
                        Movie movie = Movie.fromJson(movieJson);
                        if (movie != null) {
                            movies.add(movie);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onSuccess(movies);

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onError();
            }

        });


    }

    public static void getDetails(String imdbId, final DetailsCallback callback){
        final String URL = String.format(DETAILS_URL, imdbId);
        client.get(URL, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject)jsonParser.parse(response.toString());

                    MovieDetails details = MovieDetails.fromJson(gsonObject);
                    callback.onSuccess(details);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onError();
            }

        });
    }

    public interface ListSearchCallback {
        void onSuccess(List<Movie> movies);
        void onError();
    }

    public interface DetailsCallback {
        void onSuccess(MovieDetails details);
        void onError();
    }

}
