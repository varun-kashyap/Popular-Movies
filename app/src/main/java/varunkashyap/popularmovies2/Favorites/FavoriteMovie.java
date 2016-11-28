package varunkashyap.popularmovies2.Favorites;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import varunkashyap.popularmovies2.Data.MovieContract;
import varunkashyap.popularmovies2.MovieAdapter;
import varunkashyap.popularmovies2.MovieClass;
import varunkashyap.popularmovies2.R;

/**
 * Created by varun on 21-04-2016.
 */
public class FavoriteMovie extends AsyncTask<Void ,Void,ArrayList<MovieClass>> {

    private Context mContext;
    private Activity mActivity;

    private  ArrayList<MovieClass> mMovieList = new ArrayList<MovieClass>();
    private MovieAdapter movieAdapter;
    private  String[] Movie_COLUMNS;

    public FavoriteMovie(Context context, Activity activity, String[] Movie_COLUMNS, MovieAdapter movieAdapter ) {
        this.mContext = context;
        this.mActivity = activity;
        this.Movie_COLUMNS = Movie_COLUMNS;

        this.movieAdapter = movieAdapter;

    }

    @Override
    protected ArrayList<MovieClass> doInBackground(Void... params) {



        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, Movie_COLUMNS, null,null,null);



        if(cursor != null) {
            while (cursor.moveToNext()) {
                MovieClass movie = new MovieClass(cursor);
                mMovieList.add(movie);
            }
        }
        cursor.close();

        return mMovieList;

    }


    @Override
    protected void onPostExecute(ArrayList<MovieClass> movieList) {
        super.onPostExecute(movieList);

        if(movieList !=null) {

            movieAdapter.clear();

            MovieClass curMovie;
            for (int i = 0; i < movieList.size(); i++) {
                curMovie = movieList.get(i);
                movieAdapter.add(curMovie);
            }

        }
        else{
            // Let the user know that some problem has occurred via a toast
            Toast.makeText(mContext,mContext.getString(R.string.no_movie_data_error) ,Toast.LENGTH_SHORT).show();
        }


    }

}