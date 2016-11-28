package varunkashyap.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import varunkashyap.popularmovies2.Favorites.DealFavoritesTask;
import varunkashyap.popularmovies2.Favorites.IsFavoriteTask;
import varunkashyap.popularmovies2.model.AllReviews;
import varunkashyap.popularmovies2.model.AllTrailers;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements  View.OnClickListener {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    private Long movieID;
    private String backdropImagePath;
    private String posterPath;
    private String movieOverview;
    private String movieTitle;
    private String movieYear;
    private String vote_avg;
    public static MovieClass movie;

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    private Call<AllTrailers> callTrailer;
    private Call<AllReviews> callReviews;
    private AllTrailers allTrailers;
    private List<AllTrailers.MovieTrailer> trailerItems;
    private AllReviews allReviews;
    private List<AllReviews.MovieReview> reviewItems = new ArrayList<AllReviews.MovieReview>();
    private TMDBservice.TMDBApi tmdbApi;


    private Context mContext;
    static String DETAIL_MOVIE = "Detail_Movie";
    private Toast mToast;

    //For sharing the youtube trailer
    public static final String MOVIE_SHARE_HASHTAG = "#PopularMovies";
    private ShareActionProvider mShareActionProvider;
    public static String mShareStr;
    private AllTrailers.MovieTrailer mTrailer;

    //For the UI
    private View rootView;
    @Bind(R.id.detail_layout)
    ScrollView mDetailLayout;
    @Bind(R.id.fav_Button)
    Button favButton;
    @Bind(R.id.reviews)ViewGroup mReviewsView;
    @Bind(R.id.reviews_heading_textview)
    TextView mReviewsHeader;
    @Bind(R.id.trailer_container)
    HorizontalScrollView mTrailersScrollView;
    @Bind(R.id.trailers_heading_textview) TextView mTrailersHeader;
    @Bind(R.id.trailers) ViewGroup mTrailersView;

    @Bind(R.id.backdropPoster_image)
    ImageView backdropPosterView;
    @Bind(R.id.moviePoster) ImageView smallPosterView;
    @Bind(R.id.movie_overview_textView) TextView movieOverviewTextview;
    @Bind(R.id.movieName_textView) TextView movieTitleTextView;
    @Bind(R.id.movieYear_textView) TextView movieYearTextView;
    @Bind(R.id.rating_textView) TextView movieRatingTextView;







    public static final String MOVIE_BUNDLE = "Movie_Bundle";


    private volatile boolean onAttachDone = false;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_BUNDLE)) {
            movie = savedInstanceState.getParcelable(MOVIE_BUNDLE);
        }

    }


    /**********************  Share Intent to create a share action in AppBar   **/

    private Intent createShareMovieIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");

        // mShareStr used to share the url of first youtube trailer of movie
        mShareStr = movie.getTitle() + "\n " + "http://www.youtube.com/watch?v="+mTrailer.getKey();

        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareStr +" \n"+ MOVIE_SHARE_HASHTAG);
        return  shareIntent;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();


        if (arguments != null) {

            movie = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);

            movieID = movie.getMovieId();
            movieTitle = movie.getTitle();
            backdropImagePath = movie.getBackdropPath();
            posterPath = movie.getPosterPath();
            movieOverview = movie.getOverview();
            movieYear = movie.getYear();
            vote_avg = movie.getRating();
        }


        rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        // Use butterKnife library
        ButterKnife.bind(this,rootView);

        if (movie != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
            return rootView;
        }

        getActivity().setTitle(movieTitle);


        backdropPosterView.setAdjustViewBounds(true);
        backdropPosterView.setPadding(0, 0, 0, 0);

        Picasso.with(getContext()).load(backdropImagePath).placeholder(R.drawable.backdrop_placeholder).fit().into(backdropPosterView);
       Picasso.with(getContext()).load(posterPath).placeholder(R.drawable.small_poster_placeholder).fit().into(smallPosterView);

        movieOverviewTextview.setText(movieOverview);
        movieTitleTextView.setText(movieTitle);
        movieYearTextView.setText(movieYear);
        movieRatingTextView.setText(vote_avg);





        // Retrofit for detail movie calls
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbApi = retrofit.create(TMDBservice.TMDBApi.class);

        // Used to set The star for Favorite status

        try {
            IsFavoriteTask isFavoriteTask = new IsFavoriteTask(getContext(), rootView);
            isFavoriteTask.execute(movie);

        } catch (Exception e) {
            //do nothing
        }



        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DealFavoritesTask dealfavorites = new DealFavoritesTask(getContext(), rootView);
                dealfavorites.execute(movie);

            }
        });


        getTrailers();
        getReviews();

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mTrailer != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_BUNDLE, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        onAttachDone = true;
    }

    public void getTrailers() {


        // For fetching trailers
        callTrailer = tmdbApi.getTrailers(movieID);

        callTrailer.enqueue(new Callback<AllTrailers>() {
            @Override
            public void onResponse(Call<AllTrailers> call, Response<AllTrailers> response) {
                allTrailers = response.body();
                trailerItems = allTrailers.getTrailerList();


                mTrailer = trailerItems.get(0);



                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareMovieIntent());
                }




                boolean hasTrailers = !trailerItems.isEmpty();
                mTrailersHeader.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
                mTrailersScrollView.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
                if (hasTrailers) {
                    addTrailers(trailerItems);
                }


            }

            @Override
            public void onFailure(Call<AllTrailers> call, Throwable t) {

            }
        });


    }


    public void getReviews() {
        // For fetching reviews
        callReviews = tmdbApi.getReviews(movieID);

        callReviews.enqueue(new Callback<AllReviews>() {
            @Override
            public void onResponse(Call<AllReviews> call, Response<AllReviews> response) {

                allReviews = response.body();
                reviewItems = allReviews.getReviewsList();



                boolean hasReviews = !reviewItems.isEmpty();
                mReviewsHeader.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
                mReviewsView.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
                if (hasReviews) {
                    addReviews(reviewItems);
                }

            }

            @Override
            public void onFailure(Call<AllReviews> call, Throwable t) {

            }
        });
    }


    // On click listener for various views in the DetailActivityFragement
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.trailer_thumb) {  // clicked on the trailer thumbnail , launch youtube intent
            String trailerUrl = (String) v.getTag();
            Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
            startActivity(playVideoIntent);

        }

    }

    private void addTrailers(List<AllTrailers.MovieTrailer> trailers) {
        mTrailersView.removeAllViews();




        LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Picasso picasso = Picasso.with(getActivity());
        for (AllTrailers.MovieTrailer trailer : trailers) {
            ViewGroup trailerContainer = (ViewGroup) inflater.inflate(R.layout.trailer_display, mTrailersView, false);

            ImageView trailerThumbnail = (ImageView) trailerContainer.findViewById(R.id.trailer_thumb);
            trailerThumbnail.setTag(AllTrailers.MovieTrailer.getTrailerUrl(trailer));
            trailerThumbnail.setOnClickListener(this);

            picasso.load(AllTrailers.MovieTrailer.getThumbnailUrl(trailer))
                    .resizeDimen(R.dimen.trailer_width, R.dimen.trailer_height)
                    .centerCrop()
                    .into(trailerThumbnail);

            mTrailersView.addView(trailerContainer);

        }
    }

    private void addReviews(List<AllReviews.MovieReview> reviews) {
        mReviewsView.removeAllViews();


        LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (AllReviews.MovieReview review : reviews) {
            ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.reviews_display, mReviewsView,
                    false);

            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.review_author_textView);
            TextView reviewContent = (TextView) reviewContainer.findViewById(R.id.review_content_textView);

            reviewContent.setText(review.getContent());
            reviewAuthor.setText(review.getAuthor());
            mReviewsView.addView(reviewContainer);


        }
    }





}
