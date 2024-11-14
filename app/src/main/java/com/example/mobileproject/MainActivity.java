package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {

    ImageView exploreButton, wishlistButton, productsButton, cartButton, profileButton;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        exploreButton = findViewById(R.id.diet);
        wishlistButton = findViewById(R.id.fitness);
        productsButton = findViewById(R.id.products);
        cartButton = findViewById(R.id.card);
        profileButton = findViewById(R.id.blog);

        bottomAppBar = findViewById(R.id.BottomApp);
        bottomAppBar.setVisibility(View.GONE);
        loadFragment(new LoginFragment());

        // Set click listeners for each button
        exploreButton.setOnClickListener(v -> loadFragment(new CandidatPlans()));
        wishlistButton.setOnClickListener(v -> loadFragment(new AccueilFragment()));
        productsButton.setOnClickListener(v -> loadFragment(new MarketFragment()));
        cartButton.setOnClickListener(v -> loadFragment(new CartFragment()));
       // profileButton.setOnClickListener(v -> loadFragment(new BlogFragment()));
    }

    // Method to load selected fragment into FrameLayout
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void showNavigationBar() {
        // Affiche la BottomAppBar après la connexion
        bottomAppBar.setVisibility(View.VISIBLE);
    }
}