package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.DAO.ExerciseDAO;
import com.example.mobileproject.database.AppDataBaseL;
import com.example.mobileproject.entities.Exercise;

public class AjoutExerciceFragment extends Fragment {

    private EditText exerciseName;
    private EditText exerciseDuration;
    private EditText exerciseDescription;
    private RadioGroup radioGroupPoints;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajout_exercice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        exerciseName = view.findViewById(R.id.exerciseName);
        exerciseDuration = view.findViewById(R.id.exerciseDuration);
        exerciseDescription = view.findViewById(R.id.exerciseDescription);
        radioGroupPoints = view.findViewById(R.id.radioGroupPoints);
        Button addExerciseButton = view.findViewById(R.id.addExerciseButton);
        Button btnBack = view.findViewById(R.id.btnBack);

        // Set up the add exercise button
        addExerciseButton.setOnClickListener(v -> {
            addExercise();
            navigateToDetails();
        });

        // Set up the back button
        btnBack.setOnClickListener(v -> navigateToDetails());
    }

    private void addExercise() {
        String name = exerciseName.getText().toString().trim();
        String description = exerciseDescription.getText().toString().trim();
        String durationString = exerciseDuration.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || durationString.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid duration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine points based on selected radio button
        Exercise.Points points;
        int selectedId = radioGroupPoints.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Please select points", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RadioButton selectedRadioButton = getView().findViewById(selectedId);
            String pointsText = selectedRadioButton.getText().toString().split(" ")[0];
            int pointsValue = Integer.parseInt(pointsText);

            // Map the integer value to the Points enum
            switch (pointsValue) {
                case 3:
                    points = Exercise.Points.THREE;
                    break;
                case 5:
                    points = Exercise.Points.FIVE;
                    break;
                case 10:
                    points = Exercise.Points.TEN;
                    break;
                default:
                    Toast.makeText(getContext(), "Invalid points value", Toast.LENGTH_SHORT).show();
                    return;
            }
        }

        // Create Exercise object with Points enum
        Exercise exercise = new Exercise("photo_placeholder.jpg", name, duration, points, description);

        // Save to database
       AppDataBaseL db = AppDataBaseL.getAppDataBaseL(getContext());
        ExerciseDAO exerciseDAO = db.ExerciseDAO();
        new Thread(() -> {
            exerciseDAO.addExercise(exercise);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Exercise added successfully", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void navigateToDetails() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        DetailsExerciceFragment detailsexerciceFragment = new DetailsExerciceFragment();
        transaction.replace(R.id.fragment, detailsexerciceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
