package varunkashyap.popularmovies2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by varun on 21-04-2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieClass>
{

    public MovieAdapter(Activity context, List<MovieClass> moviePosters){

        super(context,0, moviePosters);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieClass movie = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        String posterUrl = movie.getPosterPath();


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.poster_display, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.posterView  =(ImageView) convertView.findViewById(R.id.moviePoster_image);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.posterView.setAdjustViewBounds(true);
        viewHolder.posterView.setPadding(0,0,0,0);
        Picasso.with(getContext()).load(posterUrl).fit().placeholder(R.drawable.poster_placeholder).into(viewHolder.posterView);




        return convertView;
    }



    static class ViewHolder{
        ImageView posterView;


    }
}

