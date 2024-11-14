package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class EditProfileFragment extends Fragment {

    private SharedPreferences mPreferences;
    private EditText etEmail, etUsername, etOldPassword, etPassword, etConfirmPassword, etPhoneNumber;
    private Button btnUpdate;
    private UserDAO userDAO;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Apply Edge to Edge for the fragment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.btnUpdate), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etOldPassword = view.findViewById(R.id.etOldPassword);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        // Initialize database and user DAO
        AppDataBase db = AppDataBase.getAppDataBase(requireContext());
        userDAO = db.UserDAO();

        // Initialize shared preferences
        mPreferences = requireContext().getSharedPreferences("my_shared_pref", getContext().MODE_PRIVATE);

        // Get the stored user details
        String email = mPreferences.getString("login", null);
        String password = mPreferences.getString("password", null);

        if (email != null && password != null) {
            currentUser = userDAO.login(email, password);
            if (currentUser != null) {
                etEmail.setText(currentUser.getEmail());
                etUsername.setText(currentUser.getUserName());
                etPhoneNumber.setText(currentUser.getPhoneNumber());
            }
        }

        // Set OnClickListener for update button
        btnUpdate.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            // Validate inputs
            if (TextUtils.isEmpty(etEmail.getText()) || TextUtils.isEmpty(etUsername.getText()) ||
                    TextUtils.isEmpty(etPhoneNumber.getText())) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password change
            if (!oldPassword.equals(currentUser.getPassword())) {
                Toast.makeText(getActivity(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update user details
            currentUser.setEmail(etEmail.getText().toString());
            currentUser.setUserName(etUsername.getText().toString());
            currentUser.setPhoneNumber(etPhoneNumber.getText().toString());

            if (!TextUtils.isEmpty(newPassword)) {
                currentUser.setPassword(newPassword);  // Update password only if a new one is provided
                currentUser.setConfirmPassword(confirmPassword);
            }

            userDAO.updateUSer(currentUser);
            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.clear();
            preferencesEditor.apply();

            Toast.makeText(getActivity(), "You are logging out", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            transaction.replace(R.id.fragment, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set OnClickListener for logout button
        ImageButton btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.clear();
            preferencesEditor.apply();
            Toast.makeText(getActivity(), "You are logging out", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });

        // Set OnClickListener for back button
        Button btnBack = view.findViewById(R.id.btnBackEdit);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            ProfileFragment profileFragment = new ProfileFragment();
            transaction.replace(R.id.fragment, profileFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
