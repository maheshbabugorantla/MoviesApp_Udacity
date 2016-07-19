package com.example.mahes_000.moviesapp_udacity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mahes_000.moviesapp_udacity.DataModels.ReviewItem;
import com.example.mahes_000.moviesapp_udacity.R;

import java.util.List;


/**
 * Created by Mahesh Babu Gorantla on 7/10/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private List<ReviewItem> reviews;

    public ReviewsAdapter(Context context, List<ReviewItem> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflating the Custom Layout
        View Review = layoutInflater.inflate(R.layout.review_item, parent, false);

        // Return a new Holder Instance
        ViewHolder viewHolder = new ViewHolder(Review);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the Data Model based on the Position
        ReviewItem reviewItem = reviews.get(position);

        // Set Review Content based on the Data Model
        holder.author.setText(reviewItem.getAuthor());
        holder.content.setText(reviewItem.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.Author);
            content = (TextView) itemView.findViewById(R.id.Content);
        }
    }
}