package com.example.android.movies;

/**
 * Created by Abd elrhman Arafa on 29/08/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GridViewActivity extends Fragment {

    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

    private final String review = "http://api.themoviedb.org/3/movie/<Key_ID>/reviews?api_key=a8ba45fdc7f6436d145ff8a892938e41";
    //http://api.themoviedb.org/3/movie/135397/videos?api_key=a8ba45fdc7f6436d145ff8a892938e41
    private final String videos = "http://api.themoviedb.org/3/movie/<Key_ID>/videos?api_key=a8ba45fdc7f6436d145ff8a892938e41";
    //http://api.themoviedb.org/3/movie/76341/reviews?api_key=a8ba45fdc7f6436d145ff8a892938e41
    //http://api.themoviedb.org/3/movie/76341/videos?api_key=a8ba45fdc7f6436d145ff8a892938e41
    private String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a8ba45fdc7f6436d145ff8a892938e41";
    private String FEED_URL_sort_by_popularity = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a8ba45fdc7f6436d145ff8a892938e41";
    private String FEED_URL2_highest_rated_movies = "http://api.themoviedb.org/3/discover/movie?certification_country=US&certification=R&sort_by=vote_average.desc&api_key=a8ba45fdc7f6436d145ff8a892938e41";
    private String FEED_URL_movies_in_threats = "http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22&api_key=9b46881c3edd6f85a8011834d3d491cb";
    private String FEED_URL_tom_crusie_science_fuction = "http://api.themoviedb.org/3/discover/movie?with_genres=878&with_cast=500&sort_by=vote_average.desc&primary_release_date.lte=2014-10-22&api_key=9b46881c3edd6f85a8011834d3d491cb";

    boolean twoPane;

    public static GridViewActivity getInstance(boolean twoPane) {
        GridViewActivity frag = new GridViewActivity();
        Bundle b = new Bundle();
        b.putBoolean("twopane", twoPane);
        frag.setArguments(b);
        return frag;
    }

    View v;
    boolean isInflated = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.activity_gridview, container, false);
            isInflated = true;
        } else {
            if (v.getParent() != null) {
                ((ViewGroup) v.getParent()).removeView(v);
            }
            isInflated = false;
        }
        return v;
    }

    MainActivity activity;
    String trailJson = "", reviewJson = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isInflated) {

            activity = (MainActivity) getActivity();
            twoPane = getArguments().getBoolean("twopane");
            mGridView = (GridView) v.findViewById(R.id.gridView);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

            //Initialize with empty data
            mGridData = new ArrayList<>();
            mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);

            //Start download
            new AsyncHttpTask().execute(FEED_URL);
            mProgressBar.setVisibility(View.VISIBLE);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                    //Get item at position

                    final GridItem item = (GridItem) parent.getItemAtPosition(position);
                    String tempreviewURL = review;

                    String tempVideoURL = videos;
//
                   tempreviewURL = tempreviewURL.replace("<Key_ID>", item.getId()
                    );
                    tempVideoURL = tempVideoURL.replace("<Key_ID>", item.getId());

                    new AsyncHttpTask1(new OnFinishAsync() {
                        @Override
                        public void doParse(String data) {
                            reviewJson = data;


                        }
                    }).execute(tempreviewURL);
                    new AsyncHttpTask1(new OnFinishAsync() {
                        @Override
                        public void doParse(String data) {
                            trailJson = data;
                            if (twoPane) {
                                //   mGridData.get(0).get
                                activity.replaceCurrentFragment(R.id.detailsfrag, DetailsActivity.getInstance(item.getTitle(), item.getImage(), item.getRelease_date(), item.getVote_average(), item.getOverview(), item.getId(), trailJson, reviewJson), position + "", false, false);

                            } else {

                                activity.replaceCurrentFragment(R.id.listFram, DetailsActivity.getInstance(item.getTitle(), item.getImage(), item.getRelease_date(), item.getVote_average(), item.getOverview(), item.getId(), trailJson, reviewJson), DetailsActivity.class.getName(), true, true);
                            }

                        }
                    }).execute(tempVideoURL);



                }
            });


        }
    }


    public interface OnFinishAsync {
        public void doParse(String data);
    }

    public class AsyncHttpTask1 extends AsyncTask<String, Void, String> {

        private OnFinishAsync onFinish;

        public AsyncHttpTask1(OnFinishAsync onFinish) {
            this.onFinish = onFinish;
        }

        @Override
        protected String doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    return  response;
                } else {
                   return  null;

                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
                return  null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            // Download complete. Let us update UI
            if (result !=null) {
                onFinish.doParse(result);
            } else {
                onFinish.doParse(null);
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);

                if (twoPane) {
                    String tempreviewURL = review;

                    String tempVideoURL = videos;

                    tempreviewURL = tempreviewURL.replace("<Key_ID>", mGridData.get(0).getId()
                    );
                    tempVideoURL = tempVideoURL.replace("<Key_ID>", mGridData.get(0).getId());

                    new AsyncHttpTask1(new OnFinishAsync() {
                        @Override
                        public void doParse(String data) {
                            reviewJson = data;


                        }
                    }).execute(tempreviewURL);
                    new AsyncHttpTask1(new OnFinishAsync() {
                        @Override
                        public void doParse(String data) {
                            trailJson = data;
                            activity.replaceCurrentFragment(R.id.detailsfrag, DetailsActivity.getInstance(mGridData.get(0).getTitle(), mGridData.get(0).getImage(), mGridData.get(0).getRelease_date(), mGridData.get(0).getVote_average(), mGridData.get(0).getOverview(), mGridData.get(0).getId(),trailJson,reviewJson), "0", false, false);


                        }
                    }).execute(tempVideoURL);
                    // will open details fragment


                }

            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.getJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                String title = post.getString("original_title");
                item = new GridItem();
                item.setTitle(title);
                item.setImage(post.getString("poster_path"));
                item.setOverview(post.getString("overview"));
                item.setVote_average(post.getString("vote_average"));
                item.setRelease_date(post.getString("release_date"));
                item.setId(post.getString("id"));
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.High_Rate) {
            mGridData.clear();
            new AsyncHttpTask().execute(FEED_URL2_highest_rated_movies);
            return true;
        }
        if (id == R.id.popular) {
            mGridData.clear();
            new AsyncHttpTask().execute(FEED_URL_sort_by_popularity);
            return true;
        }
        if (id == R.id.movies_in_threats) {
            mGridData.clear();
            new AsyncHttpTask().execute(FEED_URL_movies_in_threats);
            return true;
        }
        if (id == R.id.Tom_Cruise) {
            mGridData.clear();
            new AsyncHttpTask().execute(FEED_URL_tom_crusie_science_fuction);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
