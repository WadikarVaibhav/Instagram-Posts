package com.android.instaposts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        final String hashtag = hashtags.get(i);
        hashtagViewHolder.hashtag.setText("#"+hashtag);
        hashtagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedUserPosts(hashtag);
            }
        });
    }

    private void showSelectedUserPosts(String hashtag) {
        Bundle clickedUserDetails = new Bundle();
        clickedUserDetails.putString("hashtag", hashtag);
        SelectedHashtagPosts selectedHashtagPosts = new SelectedHashtagPosts();
        selectedHashtagPosts.setArguments(clickedUserDetails);
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, selectedHashtagPosts);
        transaction.addToBackStack("user_posts");
        transaction.commit();
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
