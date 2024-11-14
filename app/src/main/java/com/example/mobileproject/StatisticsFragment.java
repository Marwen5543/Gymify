package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.database.AppDataBaseL;
import com.example.mobileproject.entities.Exercise;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private PieChartCustom pieChartCustom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Reference to the custom pie chart
        pieChartCustom = view.findViewById(R.id.pieChart);

        // Retrieve exercise data from the database in a background thread
        new Thread(() -> {
            List<Exercise> exercises = AppDataBaseL.getAppDataBaseL(getContext()).ExerciseDAO().getAllExercises();

            // Prepare data for the pie chart
            int[] points = new int[exercises.size()];
            String[] exerciseNames = new String[exercises.size()];

            for (int i = 0; i < exercises.size(); i++) {
                points[i] = exercises.get(i).getPoints().getValue();
                exerciseNames[i] = exercises.get(i).getName();
            }

            // Update the pie chart on the main thread
            getActivity().runOnUiThread(() -> pieChartCustom.setData(points, exerciseNames));
        }).start();

        // Set up the back button
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToDetails());
    }

    private void navigateToDetails() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        DetailsExerciceFragment detailsexerciceFragment = new DetailsExerciceFragment();
        transaction.replace(R.id.fragment, detailsexerciceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
