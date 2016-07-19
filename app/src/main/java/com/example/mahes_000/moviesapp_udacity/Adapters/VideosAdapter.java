package com.example.mahes_000.moviesapp_udacity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mahes_000.moviesapp_udacity.DataModels.VideoItem;
import com.example.mahes_000.moviesapp_udacity.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mahes_000 on 7/11/2016.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>
{
    private String LOG_TAG = VideosAdapter.class.getSimpleName();

    String youtube_thumbnail = "https://img.youtube.com/vi/";
    String Video_URL = "https://www.youtube.com/watch?v=";

    private Context mContext;
    private List<VideoItem> mVideos;

    public VideosAdapter(Context context, List<VideoItem> videoItems)
    {
        mVideos = videoItems;
        mContext = context;
    }

    private Context getContext()
    {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflating the Custom Layout.
        View Video_Thumb = inflater.inflate(R.layout.video_item, parent, false);

        // Return a new Holder Instance
        ViewHolder viewHolder = new ViewHolder(Video_Thumb);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the Data Model based on the Position
        VideoItem videoItem = mVideos.get(position);

        // Set Video Image ThumbNails based on the Data Model
        ImageView imageView = holder.thumbNail;

        holder.thumbNail.setClickable(true);
        Video_URL = Video_URL + videoItem.getURL();

        holder.thumbNail.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Video_URL));
                getContext().startActivity(intent);
            }
        });

        String ThumbNail_URL = youtube_thumbnail+videoItem.getURL()+"/0.jpg";
        Log.d(LOG_TAG, "ThumbNail URL is " + ThumbNail_URL);

        // Loading the Image Thumbnail
        Picasso.with(getContext()).load(ThumbNail_URL).fit().into(imageView);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView thumbNail;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbNail = (ImageView) itemView.findViewById(R.id.video_thumb);
        }
    }
}
