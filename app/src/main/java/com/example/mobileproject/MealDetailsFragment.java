package com.example.mobileproject;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import entities.Food;
import DataBase.AppDataBase;

public class MealDetailsFragment extends Fragment {

    private TextView foodTitle, caloriesText, timeText, recipeText, typeText, dateText;
    private ImageView backButton; // For handling back navigation
    private ImageButton markAsDoneButton; // Button to mark food as done
    private AppDataBase database;
    private Food food;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        // Initialize the views
        foodTitle = view.findViewById(R.id.food_title);
        caloriesText = view.findViewById(R.id.calories);
        timeText = view.findViewById(R.id.time);
        recipeText = view.findViewById(R.id.recipe);
        typeText = view.findViewById(R.id.mealType);
        dateText = view.findViewById(R.id.date);
        backButton = view.findViewById(R.id.back_button);

        // Initialize the database
        database = AppDataBase.getAppDataBase(getContext());

        // Retrieve the food data from arguments
        food = (Food) getArguments().getSerializable("food");

        if (food != null) {
            // Populate the views with the food data
            foodTitle.setText(food.getName());
            caloriesText.setText(food.getCalories() + " kcal");
            timeText.setText("⏰ " + food.getTime());
            recipeText.setText(food.getRecipe());
            typeText.setText(food.getMealType());
            dateText.setText(food.getDay());

        }

        // Set up back button listener
        backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });



        return view;
    }

    private void toggleDoneStatus() {
        new Thread(() -> {
            boolean newStatus = !food.getDone(); // Toggle the done status
            database.foodDAO().markAsDone(food.getId(), newStatus);
            food.setDone(newStatus); // Update the local status

        }).start();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        database = null; // Clean up database reference
    }
}
