package com.example.mahes_000.moviesapp_udacity;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mahesh Babu Gorantla on 6/30/2016.
 *
 * This is used to display Poster Image ThumbNails for each Movie
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem>
{
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<>();

    public GridViewAdapter(Context context, int resource, ArrayList<ImageItem> data)
    {
        super(context, resource, data);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.data = data;
    }

    public void setGridData(ArrayList<ImageItem> mGridData)
    {
        this.data = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View row = convertView;
        ViewHolder viewHolder;

        // Reusing the View
        if(row == null)
        {
            row = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageTitle = (TextView) row.findViewById(R.id.imageText);
            viewHolder.imageIcon = (ImageView) row.findViewById(R.id.imageIcon);

            // This is helpful to retrieve the childViews which can be reused later
            row.setTag(viewHolder);
        }
        else
        {
            // Reusing the available TextViews
            viewHolder = (ViewHolder) row.getTag();
        }

        ImageItem imageItem = data.get(position);

        viewHolder.imageTitle.setText(Html.fromHtml(imageItem.getTitle()));
        Picasso.with(mContext).load(imageItem.getImage()).fit().into(viewHolder.imageIcon);
        return row;
    }

    public static class ViewHolder
    {
        TextView imageTitle;
        ImageView imageIcon;
    }

}