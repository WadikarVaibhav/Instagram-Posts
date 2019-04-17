package com.android.instaposts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String PICTURES_NODE = "pictures";
    private RecyclerView recyclerView;
    private List<ImageMetadata> images;
    private ImageAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        initWidgets();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Query query = FirebaseDatabase.getInstance().getReference(PICTURES_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("uploadTime");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initWidgets();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageMetadata imageMetadata = snapshot.getValue(ImageMetadata.class);
                    images.add(imageMetadata);
                }
                Collections.reverse(images);
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
        imageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(imageAdapter);
    }

}
