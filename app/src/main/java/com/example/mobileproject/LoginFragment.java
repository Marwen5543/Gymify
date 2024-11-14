package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.User;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginFragment extends Fragment {

    private MainActivity mainActivity;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private SharedPreferences sharedPref;
    private AppDataBase db;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String LOGIN = "login";
    private static final String PWD = "password";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(requireContext());
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView registerNow = view.findViewById(R.id.tvRegisterNow);
        registerNow.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            RegisterFragment registerFragment = new RegisterFragment();
            transaction.replace(R.id.fragment, registerFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        TextView forgetPassword = view.findViewById(R.id.tvForgotPassword);
        forgetPassword.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            ForgetPasswordFragment forgetPasswordFragment = new ForgetPasswordFragment();
            transaction.replace(R.id.fragment, forgetPasswordFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword2);
        btnLogin = view.findViewById(R.id.btnLogin);

        sharedPref = requireActivity().getSharedPreferences(SHARED_PREF_NAME, requireActivity().MODE_PRIVATE);
        db = AppDataBase.getAppDataBase(requireContext());

        SharedPreferences.Editor editor = sharedPref.edit();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        btnLogin.setOnClickListener(v -> {
            new Thread(() -> {
                User user = db.UserDAO().login(etEmail.getText().toString(), etPassword.getText().toString());

                requireActivity().runOnUiThread(() -> {
                    if (user != null) {
                        editor.putString(LOGIN, etEmail.getText().toString());
                        editor.putString(PWD, etPassword.getText().toString());
                        editor.apply();
                        etEmail.setText("");
                        etPassword.setText("");
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        ProfileFragment profileFragment = new ProfileFragment();
                        transaction.replace(R.id.fragment, profileFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        mainActivity.showNavigationBar();
                    } else {
                        Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        ImageView googleSignInButton = view.findViewById(R.id.ic_google);
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        callbackManager = CallbackManager.Factory.create();
        ImageView facebookSignInButton = view.findViewById(R.id.ic_facebook);
        facebookSignInButton.setOnClickListener(v -> signInWithFacebook());

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.fragment, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Facebook Sign-In cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Facebook Sign-In failed.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            ProfileFragment profileFragment = new ProfileFragment();
            transaction.replace(R.id.fragment, profileFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (ApiException e) {
            Toast.makeText(getActivity(), "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

}
