package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.database.AppDataBaseL;
import com.example.mobileproject.entities.Exercise;

public class UpdateExerciseFragment extends Fragment {

    private EditText exerciseName, exerciseDuration, exerciseDescription;
    private RadioGroup radioGroupPoints;
    private Button addExerciseButton;

    private AppDataBaseL exerciseDatabase;
    private Exercise currentExercise;
    private ExerciseAdapter exerciseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_update_exercise, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        exerciseName = view.findViewById(R.id.exerciseName);
        exerciseDuration = view.findViewById(R.id.exerciseDuration);
        exerciseDescription = view.findViewById(R.id.exerciseDescription);
        radioGroupPoints = view.findViewById(R.id.radioGroupPoints);
        addExerciseButton = view.findViewById(R.id.addExerciseButton);

        exerciseDatabase = AppDataBaseL.getAppDataBaseL(getContext());


        // Load exercise data to update
        int exerciseId = getArguments() != null ? getArguments().getInt("id", -1) : -1;
        loadExercise(exerciseId);

        addExerciseButton.setOnClickListener(v -> updateExercise());

        ImageView deleteIcon = view.findViewById(R.id.deleteIcon);
        deleteIcon.setOnClickListener(v -> deleteExercise());

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToDetails());



    }

    private void loadExercise(int id) {
        new Thread(() -> {
            currentExercise = exerciseDatabase.ExerciseDAO().getExerciseById(id);

            if (currentExercise != null) {
                // Update UI on the main thread
                getActivity().runOnUiThread(() -> {
                    exerciseName.setText(currentExercise.getName());
                    exerciseDuration.setText(String.valueOf(currentExercise.getDuration()));
                    exerciseDescription.setText(currentExercise.getDescription());

                    switch (currentExercise.getPoints()) {
                        case THREE:
                            radioGroupPoints.check(R.id.radio3Points);
                            break;
                        case FIVE:
                            radioGroupPoints.check(R.id.radio5Points);
                            break;
                        case TEN:
                            radioGroupPoints.check(R.id.radio10Points);
                            break;
                    }
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Exercise not found", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Close fragment if not found
                });
            }
        }).start();
    }

    private void updateExercise() {
        String name = exerciseName.getText().toString();
        String durationStr = exerciseDuration.getText().toString();
        String description = exerciseDescription.getText().toString();

        if (name.isEmpty() || durationStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(durationStr);
        currentExercise.setName(name);
        currentExercise.setDuration(duration);
        currentExercise.setDescription(description);

        new Thread(() -> {
            exerciseDatabase.ExerciseDAO().updateExercise(currentExercise);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Exercise updated successfully", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack(); // Close fragment after update
            });
        }).start();
    }

    private void deleteExercise() {
        if (currentExercise != null) {
            new Thread(() -> {
                exerciseDatabase.ExerciseDAO().deleteExercise(currentExercise);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Exercise deleted successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Close fragment after deletion
                });
            }).start();
        } else {
            Toast.makeText(getContext(), "Error deleting exercise", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToDetails() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        DetailsExerciceFragment detailsexerciceFragment = new DetailsExerciceFragment();
        transaction.replace(R.id.fragment, detailsexerciceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
