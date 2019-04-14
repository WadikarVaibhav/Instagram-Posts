package com.android.instaposts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.HashtagViewHolder> {

    List<String> hashtags;
    Context context;

    public HashtagAdapter(Context context, List<String> hashtags) {
        this.context = context;
        this.hashtags = hashtags;
    }

    @Override
    public HashtagAdapter.HashtagViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.hashtag_item_layout, viewGroup, false);
        return new HashtagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HashtagAdapter.HashtagViewHolder hashtagViewHolder, int i) {
        hashtagViewHolder.hashtag.setText(hashtags.get(i));
    }

    @Override
    public int getItemCount() {
        return hashtags.size();
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder {

        public TextView hashtag;

        public HashtagViewHolder(View itemView) {
            super(itemView);
            hashtag = itemView.findViewById(R.id.hashtag);
        }
    }
}
