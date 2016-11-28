package varunkashyap.popularmovies2.Favorites;

/**
 * Created by varun on 21-04-2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import varunkashyap.popularmovies2.Data.MovieContract;
import varunkashyap.popularmovies2.MovieClass;
import varunkashyap.popularmovies2.R;




public class DealFavoritesTask extends AsyncTask<MovieClass, Integer, Integer> {

    private Context mContext;
    private MovieClass mMovie;
    private int numRows;
    private int isFavorite;
    private View rootView;
    private Button favButton;
    private Toast mToast;



    public DealFavoritesTask(Context context, View view) {
        mContext = context;
        this.rootView = view;

    }

    @Override
    protected Integer doInBackground(MovieClass... params) {

        mMovie = params[0];

         /*
          Check  if MovieClass is in DB
         */

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   //projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " =?",
                new String[]{String.valueOf(mMovie.getMovieId())},
                null

        );

        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }


        if (numRows == 1) {    // Inside db so delete


            int delete = mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(mMovie.getMovieId())}
            );

            isFavorite = 0;
        } else {             // Not inside db so insert

            ContentValues values = new ContentValues();

            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_BACKDROP, mMovie.getBackdropPath());
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
            values.put(MovieContract.MovieEntry.COLUMN_DATE, mMovie.getYear());

            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);


            isFavorite = 1;
        }
        return  isFavorite;

    }


    @Override
    protected void onPostExecute(Integer isFav) {
        super.onPostExecute(isFav);


        isFavorite = isFav;

        favButton = (Button) rootView.findViewById(R.id.fav_Button);

        if (isFavorite == 0) {

            if (mToast != null) {
                mToast.cancel();
            }
            mToast.makeText(mContext, R.string.movie_removed_from_favorites, Toast.LENGTH_SHORT).show();
            favButton.setBackgroundResource(R.drawable.ribbon_white);


        } else if (isFavorite == 1) {

            if (mToast != null) {
                mToast.cancel();
            }
            mToast.makeText(mContext, R.string.movie_add_to_favorites, Toast.LENGTH_SHORT).show();

            favButton.setBackgroundResource(R.drawable.ribbon_red);


        }

    }

}

