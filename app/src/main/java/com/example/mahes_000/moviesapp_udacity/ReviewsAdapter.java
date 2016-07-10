package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mahes_000 on 7/10/2016.
 */
public class ReviewsAdapter extends ArrayAdapter<ReviewItem> {


    public ReviewsAdapter(Context context, ArrayList<ReviewItem> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReviewItem review = getItem(position);

        ViewHolder viewHolder;

        // Creating an new Item if not present at the position
        if(convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.review_item,parent, false);

            viewHolder.author = (TextView) convertView.findViewById(R.id.Author);
            viewHolder.content = (TextView) convertView.findViewById(R.id.Content);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(review.author);
        viewHolder.content.setText(review.content);

        return convertView;
    }

    private static class ViewHolder {
        TextView author;
        TextView content;
    }
}
