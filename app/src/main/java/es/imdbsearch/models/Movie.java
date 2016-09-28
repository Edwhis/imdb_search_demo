package es.imdbsearch.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;


public class Movie {

    @SerializedName("Title") private String title;
    @SerializedName("Year") private String year;
    @SerializedName("imdbID") private String imdbId;
    @SerializedName("Type") private String type;
    @SerializedName("Poster") private String poster;

    public static Movie fromJson(JsonElement json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Movie.class);
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getType() {
        if(type.length() > 1){
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
        }
        return type;
    }

    public String getPoster() {
        return poster;
    }
}
