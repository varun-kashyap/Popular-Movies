package varunkashyap.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by varun on 21-04-2016.
 */
public class AllReviews {


    public void setReviewsList(List<MovieReview> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @SerializedName("results")
    List<MovieReview> reviewsList;

    public List<MovieReview> getReviewsList() {
        return reviewsList;
    }




    public static class MovieReview{


        @SerializedName("id")
        private String mId;

        @SerializedName("author")
        private String mAuthor;

        @SerializedName("content")
        private String mContent;

        @SerializedName("url")
        private String mUrl;

        public String getId() {
            return mId;
        }

        public String getUrl() {
            return mUrl;
        }

        public String getContent(){
            return mContent;
        }

        public String getAuthor(){
            return  mAuthor;
        }


    }
}
