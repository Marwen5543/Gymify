package com.example.mobileproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Adapters.MealPlanAdapter;
import Adapters.MealPlanCandidateAdapter;
import DAO.PlanDAO;
import DataBase.AppDataBase;
import entities.PlanWithMeals;


public class CandidatPlans extends Fragment {

    private RecyclerView plannerRecyclerView;
    private MealPlanCandidateAdapter mealPlanAdapter;
    private PlanDAO planDao; // PlanDAO to access the plans
    private ImageButton addPlanButton;
    private TextView Candidate_name; // TextView for displaying candidate name
    private ImageView backButton;

    public CandidatPlans() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidat_plans, container, false);
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


        Candidate_name = view.findViewById(R.id.candidate_name); // Initialize TextView for candidate name

        // Set the onClickListener to navigate to MealFormFragment
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
            List<PlanWithMeals> mealPlans = planDao.getPlansWithMeals();

            if (mealPlans != null && !mealPlans.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    mealPlanAdapter = new MealPlanCandidateAdapter(mealPlans, planDao);
                    plannerRecyclerView.setAdapter(mealPlanAdapter);
                });
            }
        }).start();
    }



}