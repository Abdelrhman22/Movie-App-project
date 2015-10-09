package com.example.android.movies;

/**
 * Created by Abd elrhman Arafa on 29/08/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends Fragment {
    private TextView titleTextView;
    private ImageView imageView;
    private TextView overviewTextView;
    private TextView release_dateTextView;
    private TextView vote_averageTextView;

    RecyclerView listView;
    String trailJson,reviewJson;

    View v;
    boolean isInflated = false;

    public static DetailsActivity getInstance(String title, String image, String release_date, String vote_average, String overview, String
            ID,String trailJson,String reviewJson) {
        DetailsActivity frag = new DetailsActivity();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("image", image);
        b.putString("release_date", release_date);
        b.putString("vote_average", vote_average);
        b.putString("overview", overview);
        b.putString("ID", ID);
        b.putString("trail",trailJson);
        b.putString("review",reviewJson);
        frag.setArguments(b);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.activity_details_view, container, false);
            isInflated = true;
        } else {
            if (v.getParent() != null) {
                ((ViewGroup) v.getParent()).removeView(v);

            }
            isInflated = false;
        }
        return v;
    }


    ArrayList<ParentItemInterface> listItem = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // if (isInflated) {


        trailJson=getArguments().getString("trail");
        reviewJson=getArguments().getString("review");
        String title = getArguments().getString("title");
        String image = getArguments().getString("image");
        String release_date = getArguments().getString("release_date");
        String vote_average = getArguments().getString("vote_average");
        String overview = getArguments().getString("overview");
        final String id = getArguments().getString("ID");
        titleTextView = (TextView) v.findViewById(R.id.title);
        listView = (RecyclerView) v.findViewById(R.id.listView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);


        titleTextView.setText(title);
        // Image
        imageView = (ImageView) v.findViewById(R.id.grid_item_image);
        Picasso.with(getActivity()).load(image).into(imageView);
        // Relase Date
        release_dateTextView = (TextView) v.findViewById(R.id.release_date);
        release_dateTextView.setText("Release " + release_date + "\n");
        //Vote average
        vote_averageTextView = (TextView) v.findViewById(R.id.vote_average);
        vote_averageTextView.setText("Rate: " + vote_average);
        OverView overView = new OverView();
        overView.setOverView(overview);
        listItem.add(overView);




        try {
            JSONArray mainArr = (new JSONObject(trailJson)).getJSONArray("results");
            for (int i = 0; i < mainArr.length(); i++) {
                TrailsItem item = new TrailsItem();
                item.setId(mainArr.getJSONObject(i).getString("id"));
                item.setIso(mainArr.getJSONObject(i).getString("iso_639_1"));
                item.setKey(mainArr.getJSONObject(i).getString("key"));
                item.setName(mainArr.getJSONObject(i).getString("name"));
                item.setSite(mainArr.getJSONObject(i).getString("site"));
                item.setSize(mainArr.getJSONObject(i).getString("size"));
                item.setType(mainArr.getJSONObject(i).getString("type"));
                listItem.add(item);
            }
            //  ((DetailsTrailsAndReviewAdapter) listView.getAdapter()).updateList(listItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONArray mainArr = (new JSONObject(reviewJson)).getJSONArray("results");
            for (int i = 0; i < mainArr.length(); i++) {
                ReviewItem item = new ReviewItem();
                item.setId(mainArr.getJSONObject(i).getString("id"));
                item.setAuthor(mainArr.getJSONObject(i).getString("author"));
                item.setContent(mainArr.getJSONObject(i).getString("content"));
                item.setUrl(mainArr.getJSONObject(i).getString("url"));
                listItem.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        listView.setAdapter(new DetailsTrailsAndReviewAdapter(getActivity(), listItem));

        ((DetailsTrailsAndReviewAdapter) listView.getAdapter()).setOnItemClickListener(new DetailsTrailsAndReviewAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int pos, View v) {

            }
        });
        //}
    }




}