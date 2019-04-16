package com.android.instaposts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HashTagsFragment extends Fragment {

    public static final String HASHTAGS_NODE = "hashtags";
    private RecyclerView hashtagRecyclerView;
    private List<String> hashtags;
    private HashtagAdapter hashtagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hashtags_list, container, false);
        hashtagRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_hashtags);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(HASHTAGS_NODE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String hashtag = snapshot.getValue().toString();
                    hashtags.add(hashtag);
                }
                hashtagAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        hashtagRecyclerView.setHasFixedSize(true);
        hashtagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hashtags = new ArrayList<>();
        hashtagAdapter = new HashtagAdapter(getActivity(), hashtags);
        hashtagRecyclerView.setAdapter(hashtagAdapter);
    }

}
