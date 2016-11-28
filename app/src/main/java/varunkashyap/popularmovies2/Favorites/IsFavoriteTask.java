package varunkashyap.popularmovies2.Favorites;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import varunkashyap.popularmovies2.Data.MovieContract;
import varunkashyap.popularmovies2.MovieClass;
import varunkashyap.popularmovies2.R;

/**
 * Created by varun on 21-04-2016.
 */
public class IsFavoriteTask extends AsyncTask<MovieClass, Integer, Integer> {

    private Context mContext;
    private int isFavoriteMovie;
    private MovieClass mMovie;
    private Button favButton;
    private int numRows;
    private  int  mIsFavorite;
    private View rootView;

    public IsFavoriteTask(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;

    }


    @Override
    protected Integer doInBackground(MovieClass... params) {

        mMovie = params[0];


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

        if (numRows == 1) {    // Inside db
            isFavoriteMovie = 1;
        } else {             // Not inside db
            isFavoriteMovie = 0;
        }

        return isFavoriteMovie;
    }


    @Override
    protected void onPostExecute(Integer isFav) {
        super.onPostExecute(isFav);



        //Set the icon of Floating action button based on if move in favorites or not


        mIsFavorite = isFav;
        favButton = (Button) rootView.findViewById(R.id.fav_Button);

        if (mIsFavorite == 1) {

            favButton.setBackgroundResource(R.drawable.ribbon_red);
        } else if (mIsFavorite == 0) {

            favButton.setBackgroundResource(R.drawable.ribbon_white);
        }


    }

}

