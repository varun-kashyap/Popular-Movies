package varunkashyap.popularmovies2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import varunkashyap.popularmovies2.model.AllMovies;
import varunkashyap.popularmovies2.model.AllReviews;
import varunkashyap.popularmovies2.model.AllTrailers;

/**
 * Created by varun on 21-04-2016.
 */
public class TMDBservice {

    public interface TMDBApi{

        // To get all the movies and store them as MovieModel objects

        @GET("movie/{sort}?api_key=" + BuildConfig.TMDB_API_KEY)
        Call<AllMovies> getMovies(
                @Path("sort") String sortCriteria
        );


        // To get the trailers and store them as MovieTrailer objects

        @GET("movie/{id}/videos?api_key=" + BuildConfig.TMDB_API_KEY)
        Call<AllTrailers> getTrailers(
                @Path("id") long movieID
        );

        // To get the trailers and store them as MovieTrailer objects

        @GET("movie/{id}/reviews?api_key=" + BuildConfig.TMDB_API_KEY)
        Call<AllReviews> getReviews(
                @Path("id") long movieID
        );




    }

}
