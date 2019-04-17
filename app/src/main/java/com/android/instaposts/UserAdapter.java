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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public static final String USER_ID = "userId";
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        final User user = users.get(i);
        userViewHolder.username.setText(user.getName());
        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedUserPosts(user.getId());
            }
        });
    }

    private void showSelectedUserPosts(String userId) {
        Bundle clickedUserDetails = new Bundle();
        clickedUserDetails.putString(USER_ID, userId);
        SelectedUserPosts selectedUserPosts = new SelectedUserPosts();
        selectedUserPosts.setArguments(clickedUserDetails);
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, selectedUserPosts);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }

}
