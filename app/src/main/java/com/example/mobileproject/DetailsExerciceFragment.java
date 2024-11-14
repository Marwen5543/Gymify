package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.database.AppDataBaseL;
import com.example.mobileproject.entities.Exercise;

import java.util.List;

public class DetailsExerciceFragment extends Fragment {

    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter exerciseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_exercice, container, false);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up RecyclerView
        exerciseRecyclerView = view.findViewById(R.id.exercisesRecyclerView);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Get exercises from the database
        List<Exercise> exerciseList = AppDataBaseL.getAppDataBaseL(getContext()).ExerciseDAO().getAllExercises();
        int[] images = {
                R.drawable.cardio,
                R.drawable.boxing,
                R.drawable.yoga,
                R.drawable.coach,
                R.drawable.stretching,
                R.drawable.cobra,
                R.drawable.exercice
        };

        // Set up the adapter for RecyclerView
        exerciseAdapter = new ExerciseAdapter(getContext(), exerciseList, images,getParentFragmentManager());
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        // Set up Add Button click listener
        ImageView addButton = view.findViewById(R.id.AddIcon);
        addButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            AjoutExerciceFragment ajoutexerciceFragment = new AjoutExerciceFragment();
            transaction.replace(R.id.fragment, ajoutexerciceFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set up Statistics Button click listener
        ImageView statButton = view.findViewById(R.id.btnstat);
        statButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            transaction.replace(R.id.fragment, statisticsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set up Back Button click listener
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            AccueilFragment accueilFragment = new AccueilFragment();
            transaction.replace(R.id.fragment, accueilFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
