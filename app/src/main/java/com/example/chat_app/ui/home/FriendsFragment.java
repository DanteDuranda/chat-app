package com.example.chat_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.dao.UserFetcherAsyncTask;
import com.example.chat_app.databinding.FragmentFriendsBinding;
import com.example.chat_app.model.User;
import com.example.chat_app.utilities.UserAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private UserAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewUsers;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchUsers();

        return root;
    }

    private void fetchUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        UserFetcherAsyncTask userFetcherAsyncTask = new UserFetcherAsyncTask(usersCollection, new UserFetcherAsyncTask.OnFetchUsersListener() {
            @Override
            public void onFetchSuccess(List<User> userList) {
                // Update adapter with fetched user list
                adapter.setUserList(userList);
            }

            @Override
            public void onFetchFailure(Exception e) {
                // Handle fetch failure
                Toast.makeText(getContext(), "Failed to fetch users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        userFetcherAsyncTask.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
