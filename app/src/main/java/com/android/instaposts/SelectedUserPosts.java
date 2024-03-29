package com.android.instaposts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectedUserPosts extends Fragment {

    public static final String USER_ID = "userId";
    public static final String PICTURES_NODE = "pictures";
    public static final String NO_POST_MESSAGE = "User conatins no post";
    private RecyclerView recyclerView;
    private List<ImageMetadata> images;
    private ImageAdapter imageAdapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        Bundle bundle = getArguments();
        userId = bundle.getString(USER_ID);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(PICTURES_NODE).child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageMetadata imageMetadata = snapshot.getValue(ImageMetadata.class);
                    images.add(imageMetadata);
                }
                if (images.size() == 0) {
                    Toast.makeText(getContext(), NO_POST_MESSAGE, Toast.LENGTH_LONG).show();
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

}
