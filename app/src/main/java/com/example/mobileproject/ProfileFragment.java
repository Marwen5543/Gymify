package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.DAO.UserDAO;
import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.User;

public class ProfileFragment extends Fragment {

    private SharedPreferences mPreferences;
    private UserDAO userDAO;
    private TextView tvLastName, tvUsername, tvPhoneNumber, tvEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Apply Edge to Edge for the fragment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.ProfileDetails), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView editProfile = view.findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            EditProfileFragment editprofileFragment = new EditProfileFragment();
            transaction.replace(R.id.fragment, editprofileFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        ImageButton btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.clear();
            preferencesEditor.apply();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            transaction.replace(R.id.fragment, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            Toast.makeText(getActivity(), "You are logging out", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });

        // Initialize database and user DAO
        AppDataBase db = AppDataBase.getAppDataBase(requireContext());
        userDAO = db.UserDAO();

        // Initialize shared preferences
        mPreferences = requireActivity().getSharedPreferences("my_shared_pref", getActivity().MODE_PRIVATE);

        // Initialize UI components
        tvLastName = view.findViewById(R.id.tvLastName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvEmail = view.findViewById(R.id.tvEmail);

        // Get the stored user details
        String email = mPreferences.getString("login", null);
        String password = mPreferences.getString("password", null);

        // Check if email and password are stored
        if (email != null && password != null) {
            // Fetch the user from the database using email and password
            User user = userDAO.login(email, password);

            if (user != null) {
                // Update the UI with the user data
                tvLastName.setText("Last Name: " + user.getUserName());
                tvUsername.setText("Username: " + user.getUserName());
                tvPhoneNumber.setText("Number: " + user.getPhoneNumber());
                tvEmail.setText(user.getEmail());
            }
        }

        ImageButton btnUpgrade = view.findViewById(R.id.btnUpgrade);
        btnUpgrade.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buy.stripe.com/test_00geXPezifWufM44gg"));
            startActivity(intent);
        });

        return view;
    }
}
