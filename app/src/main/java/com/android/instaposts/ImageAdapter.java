package com.android.instaposts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<ImageMetadata> images;

    public ImageAdapter(Context context, List<ImageMetadata> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item_layout, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder imageViewHolder, int i) {
        ImageMetadata imageMetadata = images.get(i);
        imageViewHolder.imageCaption.setText(imageMetadata.getCaption());
        Picasso.get().load(imageMetadata.getUrl()).fit().centerCrop().into(imageViewHolder.imageItem, new Callback() {
            @Override
            public void onSuccess() {
                imageViewHolder.imageLoadProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView imageCaption;
        public ImageView imageItem;
        public ProgressBar imageLoadProgress;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageCaption = itemView.findViewById(R.id.image_caption);
            imageItem = itemView.findViewById(R.id.image_item);
            imageLoadProgress = itemView.findViewById(R.id.load_image_progress);
        }
    }

}
