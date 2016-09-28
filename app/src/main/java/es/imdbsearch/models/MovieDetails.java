package es.imdbsearch.models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class MovieDetails {

    @SerializedName("Title") private String title;
    @SerializedName("Year") private String year;
    @SerializedName("Genre") private String genre;
    @SerializedName("Plot") private String plot;
    @SerializedName("Poster") private String poster;
    @SerializedName("Type") private String type;
    @SerializedName("imdbRating") private float imdbRating;

    public static MovieDetails fromJson(JsonElement json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, MovieDetails.class);
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getType() {
        if(type.length() > 1){
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
        }
        return type;
    }

    public float getImdbRating() {
        return imdbRating;
    }
}
