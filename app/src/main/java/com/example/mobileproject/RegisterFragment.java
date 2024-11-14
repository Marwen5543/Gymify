package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.User;

public class RegisterFragment extends Fragment {

    private EditText etEmail, etUsername, etPassword, etConfirmPassword, etPhoneNumber;
    private Button btnRegister;
    private AppDataBase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Apply Edge to Edge for the fragment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.ivProfileImage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        btnRegister = view.findViewById(R.id.btnRegister);

        // Initialize Database
        db = AppDataBase.getAppDataBase(requireContext());

        // Set OnClickListener for Register Button
        btnRegister.setOnClickListener(v -> {
            // Get Input Values
            String email = etEmail.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();

            // Validate User Input
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Create New User
                User newUser = new User(email, password, confirmPassword, username, phoneNumber);

                try {
                    // Insert User into Database
                    db.UserDAO().addUSer(newUser);
                    Toast.makeText(getActivity(), "User Registered Successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to ProfileFragment
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    transaction.replace(R.id.fragment, profileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    requireActivity().finish();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error registering user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnBack = view.findViewById(R.id.btnBackRegister);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            transaction.replace(R.id.fragment, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
