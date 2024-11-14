package com.example.mobileproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import DataBase.AppDataBase; // Import your database class
import entities.PlanWithMeals; // Adjust the package name accordingly
import Adapters.MealPlanAdapter; // Ensure you create this adapter
import DAO.PlanDAO; // Import your PlanDAO

public class PlannerFragment extends Fragment {

    public PlannerFragment() {
        // Required empty public constructor
    }
    private RecyclerView plannerRecyclerView;
    private MealPlanAdapter mealPlanAdapter;
    private PlanDAO planDao; // PlanDAO to access the plans
    private ImageButton addPlanButton;
    private TextView Candidate_name; // TextView for displaying candidate name
    private ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate your layout
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ensure you're using the correct ID and view reference
        backButton = view.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> goBackToPlanner());
        } else {
            Log.e("MealFormFragment", "Back Button not found.");
        }


        addPlanButton = view.findViewById(R.id.addPlan);
        Candidate_name = view.findViewById(R.id.candidate_name); // Initialize TextView for candidate name

        // Set the onClickListener to navigate to MealFormFragment
        addPlanButton.setOnClickListener(v -> navigateToMealFormFragment());
        // Initialize RecyclerView and set LayoutManager
        int numberOfColumns = 2; // Change this number based on how many items you want per row
        plannerRecyclerView = view.findViewById(R.id.planner_recyclerView);
        plannerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        // Initialize the PlanDAO to access the database
        planDao = AppDataBase.getAppDataBase(getContext()).planDAO();

        // Load meal plans from the database
        loadMealPlans();
        if (getArguments() != null) {
            String candidateName = getArguments().getString("candidate_name");
            if (candidateName != null) {
                Candidate_name.setText(candidateName); // Update TextView with the candidate's name
            }
        }
    }

    private void goBackToPlanner() {
        Log.d("plannerFragment", "Going back to candidates");
        if (getActivity() != null) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new coach_candidates())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void loadMealPlans() {
        new Thread(() -> {
            // Fetch the meal plans from the database
            List<PlanWithMeals> mealPlans = planDao.getPlansWithMeals();
            Log.d("PlannerFragment", "Loaded meal plans: " + mealPlans.size());

            // Check if mealPlans is null or empty
            if (mealPlans != null && !mealPlans.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    mealPlanAdapter = new MealPlanAdapter(mealPlans, planDao);
                    plannerRecyclerView.setAdapter(mealPlanAdapter);
                });
            } else {
                Log.d("PlannerFragment", "No meal plans found.");
            }
        }).start();
    }

    private void navigateToMealFormFragment() {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, new MealFormFragment());  // Replace the fragment in the container
        transaction.addToBackStack(null);  // Optional: add to back stack if you want to navigate back
        transaction.commit();

    }



}
