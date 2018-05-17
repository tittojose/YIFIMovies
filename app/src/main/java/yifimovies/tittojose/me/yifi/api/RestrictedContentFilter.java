package yifimovies.tittojose.me.yifi.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Iterator;

import yifimovies.tittojose.me.yifi.Constants;
import yifimovies.tittojose.me.yifi.api.model.MovieAPIResponse;

public class RestrictedContentFilter implements JsonDeserializer<MovieAPIResponse> {

    public static final String TAG = RestrictedContentFilter.class.getSimpleName();

    @Override
    public MovieAPIResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        // Get the "content" element from the parsed JSON
        try {
            JsonObject jsonObject = je.getAsJsonObject();
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            JsonArray movies = data.get("movies").getAsJsonArray();
            Iterator<JsonElement> iterator = movies.iterator();
            while (iterator.hasNext()) {

                JsonObject movieItem = iterator.next().getAsJsonObject();
                long movieId = movieItem.get("id").getAsLong();
                String[] split = Constants.RESTRICTED_MOVIES.split(",");
                for (int i = 0; i < split.length; i++) {
                    if (movieId == Long.valueOf(split[i])) {
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deserialize: " + e.toString());
        }

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(je, MovieAPIResponse.class);

    }
}