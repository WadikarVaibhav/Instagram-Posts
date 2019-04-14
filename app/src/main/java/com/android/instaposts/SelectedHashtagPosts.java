package com.android.instaposts;

import android.os.Bundle;
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

public class SelectedHashtagPosts extends Fragment {

    private RecyclerView recyclerView;
    private List<ImageMetadata> images;
    private ImageAdapter imageAdapter;
    private String hashtag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        Bundle bundle = getArguments();
        hashtag = bundle.getString("hashtag");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pictures");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userIdSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot imageMetadataSnapshot: userIdSnapshot.getChildren()) {
                        ImageMetadata imageMetadata = imageMetadataSnapshot.getValue(ImageMetadata.class);
                        if (isHashtagExists(imageMetadata.getHashtags())) {
                            images.add(imageMetadata);
                        }
                    }
                }
                imageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        images = new ArrayList<>();
        imageAdapter = new ImageAdapter(getActivity(), images);
        recyclerView.setAdapter(imageAdapter);
    }

    private boolean isHashtagExists(List<String> hashtags) {
        for (String tag: hashtags) {
            if (tag.equals(hashtag)) {
                return true;
            }
        }
        return  false;
    }

}
