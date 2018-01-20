package yifimovies.tittojose.me.yifi;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by titto.jose on 16-01-2018.
 */

public class MovieSearchCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
//    private SearchView searchView;

    public MovieSearchCursorAdapter(Context context, Cursor cursor, SearchView sv) {
        super(context, cursor, false);
        mContext = context;
//        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_movie_suggestion_list, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image"));

        TextView movieTitle = view.findViewById(R.id.tvTitleMovieListItem);
        ImageView movieCoverImage = view.findViewById(R.id.imageViewMovieListItem);

        movieTitle.setText(title);
        Glide.with(context)
                .load(imageUrl)
                .into(movieCoverImage);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take next action based user selected item
                TextView title = (TextView) view.findViewById(R.id.tvTitleMovieListItem);
//                searchView.setIconified(true);
                Toast.makeText(context, "Selected suggestion " + title.getText(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}