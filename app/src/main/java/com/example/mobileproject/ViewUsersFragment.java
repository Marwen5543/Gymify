package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.User;

import java.util.List;

public class ViewUsersFragment extends Fragment {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        // Initialize RecyclerView
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load users
        loadUsers();

        return view;
    }

    private void loadUsers() {
        // Get the database instance
        AppDataBase db = AppDataBase.getAppDataBase(requireContext());

        // Retrieve the users from the database
        List<User> users = db.UserDAO().getAll();

        // Set up the adapter and assign it to the RecyclerView
        userAdapter = new UserAdapter(users);
        recyclerViewUsers.setAdapter(userAdapter);
    }
}
