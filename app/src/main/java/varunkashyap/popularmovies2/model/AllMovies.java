package varunkashyap.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import varunkashyap.popularmovies2.MovieClass;

/**
 * Created by varun on 21-04-2016.
 */
public class AllMovies {
    @SerializedName("results")
    ArrayList<MovieClass> movieList;

    public ArrayList<MovieClass> getMovieList() {
        return movieList;
    }
}

