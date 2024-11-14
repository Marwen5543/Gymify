package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.mobileproject.DAO.UserDAO;
import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.User;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordFragment extends Fragment {

    private EditText etEmail;
    private Button btnSend;
    private FirebaseAuth firebaseAuth;
    private UserDAO userDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        // Apply Edge to Edge for the fragment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        etEmail = view.findViewById(R.id.etEmail);
        btnSend = view.findViewById(R.id.btnSend);

        Button btnBack = view.findViewById(R.id.btnBackFg);
        btnBack.setOnClickListener(v -> {
            // Navigate back to LoginActivity or previous fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            transaction.replace(R.id.fragment, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Initialize FirebaseAuth and Database
        firebaseAuth = FirebaseAuth.getInstance();
        AppDataBase db = AppDataBase.getAppDataBase(requireContext());
        userDao = db.UserDAO();

        // Set OnClickListener for Send Button
        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            // Validate Email
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // Check if email exists in SQLite before proceeding
                new Thread(() -> {
                    User user = userDao.findUserByEmail(email);
                    requireActivity().runOnUiThread(() -> {
                        if (user != null) {
                            // If user exists, send reset email via Firebase
                            firebaseAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // If user does not exist, show error message
                            Toast.makeText(getActivity(), "Email not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });

        return view;
    }
}
