package com.example.mahes_000.moviesapp_udacity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mahes_000 on 7/11/2016.
 */
public class VideosAdapter extends ArrayAdapter<VideoItem>
{

    private String LOG_TAG = VideosAdapter.class.getSimpleName();

    String youtube_thumbnail = "https://img.youtube.com/vi/";
    String Video_URL = "https://www.youtube.com/watch?v=";

    public VideosAdapter(Context context, ArrayList<VideoItem> videoItems)
    {
        super(context,0, videoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final VideoItem videoItem = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.video_item,parent,false);

            viewHolder.thumbNail = (ImageView) convertView.findViewById(R.id.video_thumb);
            viewHolder.thumbNail.setClickable(true);
            Video_URL = Video_URL + videoItem.Key_value;
            viewHolder.thumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Toast.makeText(getContext(), "Clicked on " + Video_URL, Toast.LENGTH_SHORT).show();
*/
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Video_URL));
                    getContext().startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Setting the ImageThumb Nail URL
        String ThumbNail_URL = youtube_thumbnail+videoItem.Key_value+"/0.jpg";
        Log.d(LOG_TAG, "ThumbNail URL is " + ThumbNail_URL);

        // Loading the Image Thumbnail
        Picasso.with(getContext()).load(ThumbNail_URL).fit().into(viewHolder.thumbNail);

        return convertView;
    }

    private static class ViewHolder
    {
        ImageView thumbNail;
    }
}
