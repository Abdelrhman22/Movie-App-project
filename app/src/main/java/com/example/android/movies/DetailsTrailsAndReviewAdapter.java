package com.example.android.movies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abd elrhman Arafa on 29/08/2015.
 */
public class DetailsTrailsAndReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public LayoutInflater inflater = null;

    Activity activity;
    ArrayList<ParentItemInterface> listItems;


    public DetailsTrailsAndReviewAdapter(Activity activity, ArrayList<ParentItemInterface> listItems) {
        this.activity = activity;
        this.listItems = listItems;
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View vi = inflater.inflate(R.layout.overviewitem, parent, false);
            RecyclerView.ViewHolder vh = new OverViewHolder(vi);
            return vh;
        } else if (viewType == ReviewItem.ReviewItem) {
            View vi = inflater.inflate(R.layout.review_single_item, parent, false);
            RecyclerView.ViewHolder vh = new ReviewHolder(vi);
            return vh;

        } else {
            View vi = inflater.inflate(R.layout.trail_single_item, parent, false);
            RecyclerView.ViewHolder vh = new TrailHolder(vi);
            return vh;

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

        if (viewHolder instanceof OverViewHolder) {
            OverViewHolder holder = (OverViewHolder) viewHolder;
            holder.overview.setText(((OverView) listItems.get(pos)).getOverView());
        } else if (viewHolder instanceof TrailHolder) {
            TrailHolder holder = (TrailHolder) viewHolder;
            TrailsItem item = (TrailsItem) listItems.get(pos);
            holder.trailDesc.setText(item.getName());
        } else {
            ReviewHolder holder = (ReviewHolder) viewHolder;
            ReviewItem item = (ReviewItem) listItems.get(pos);
            holder.reviewby.setText(item.getAuthor());
            holder.content.setText(item.getContent());
        }

    }

    @Override
    public int getItemViewType(int pos) {
        if (listItems.get(pos) instanceof OverView) {

            return OverView.overViewType;
        } else if (listItems.get(pos) instanceof ReviewItem) {

            return ReviewItem.ReviewItem;

        } else {
            return TrailsItem.TrailType;

        }
    }

    @Override
    public int getItemCount() {
        if (listItems == null)
            return 0;
        return (listItems.size());
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView reviewby, content, seeDetails;

        public ReviewHolder(View vi) {
            super(vi);
            vi.setOnClickListener(this);
            reviewby = (TextView) vi.findViewById(R.id.reviewby);
            content = (TextView) vi.findViewById(R.id.content);
            seeDetails = (TextView) vi.findViewById(R.id.seeDetails);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClickListener(getPosition(), v);
        }
    }


    public class TrailHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        ImageView trail_play_icon;
        TextView trailDesc;

        public TrailHolder(View vi) {
            super(vi);
            vi.setOnClickListener(this);
            trail_play_icon = (ImageView) vi.findViewById(R.id.trail_play_icon);
            //  line = (ImageView) vi.findViewById(R.id.line);
            trailDesc = (TextView) vi.findViewById(R.id.trailDesc);
            trail_play_icon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                int pos = getPosition();
                String key = ((TrailsItem) listItems.get(pos)).getKey();
                watchYoutubeVideo(key);
                mItemClickListener.onItemClickListener(getPosition(), v);
            }

        }
    }

    public class OverViewHolder extends RecyclerView.ViewHolder {
        TextView overview;

        public OverViewHolder(View vi) {
            super(vi);
            overview = (TextView) vi.findViewById(R.id.overview);

        }


    }

    public void setOnItemClickListener(ItemClickListener itemClick) {
        this.mItemClickListener = itemClick;
    }

    public ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        public void onItemClickListener(int pos, View v);
    }

    public void updateList(ArrayList<ParentItemInterface> items) {
        this.listItems = items;
        notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public  void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
           activity. startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
          activity.startActivity(intent);
        }
    }
}
