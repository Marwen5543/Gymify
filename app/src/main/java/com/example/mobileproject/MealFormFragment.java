package com.example.mobileproject;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import entities.Food;
import entities.Plan;
import DataBase.AppDataBase;

public class MealFormFragment extends Fragment {

    private EditText weekInput, dayInput;
    private EditText breakfastNameInput, breakfastRecipeInput, breakfastCaloriesInput, breakfastTimeInput;
    private EditText lunchNameInput, lunchRecipeInput, lunchCaloriesInput, lunchTimeInput;
    private EditText snackNameInput, snackRecipeInput, snackCaloriesInput, snackTimeInput;
    private EditText dinnerNameInput, dinnerRecipeInput, dinnerCaloriesInput, dinnerTimeInput;
    private AppDataBase dataBase;
    private ImageView backButton;
    private ImageView validateBreakfastButton, validateLunchButton, validateSnackButton, validateDinnerButton;
    private LinearLayout breakfastSection, lunchSection, snackSection, dinnerSection;

    public MealFormFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = AppDataBase.getAppDataBase(getContext());
        if (dataBase == null) {
            Log.e("MealFormFragment", "Database is null");
        } else {
            Log.d("MealFormFragment", "Database initialized");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_form, container, false);

        backButton = view.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> goBackToPlanner());
        } else {
            Log.e("MealFormFragment", "Back Button not found.");
        }
        initializeInputs(view);  // Initialize input fields
        initializeButtons(view); // Initialize buttons
        return view;
    }

    private void initializeInputs(View view) {
        weekInput = view.findViewById(R.id.week_input);
        dayInput = view.findViewById(R.id.day_input);

        breakfastNameInput = view.findViewById(R.id.breakfast_name_input);
        breakfastRecipeInput = view.findViewById(R.id.breakfast_recipe_input);
        breakfastCaloriesInput = view.findViewById(R.id.breakfast_calories_input);
        breakfastTimeInput = view.findViewById(R.id.breakfast_time_input);

        lunchNameInput = view.findViewById(R.id.lunch_name_input);
        lunchRecipeInput = view.findViewById(R.id.lunch_recipe_input);
        lunchCaloriesInput = view.findViewById(R.id.lunch_calories_input);
        lunchTimeInput = view.findViewById(R.id.lunch_time_input);

        snackNameInput = view.findViewById(R.id.snack_name_input);
        snackRecipeInput = view.findViewById(R.id.snack_recipe_input);
        snackCaloriesInput = view.findViewById(R.id.snack_calories_input);
        snackTimeInput = view.findViewById(R.id.snack_time_input);

        dinnerNameInput = view.findViewById(R.id.dinner_name_input);
        dinnerRecipeInput = view.findViewById(R.id.dinner_recipe_input);
        dinnerCaloriesInput = view.findViewById(R.id.dinner_calories_input);
        dinnerTimeInput = view.findViewById(R.id.dinner_time_input);
    }

    private void initializeButtons(View view) {
        validateBreakfastButton = view.findViewById(R.id.validate_breakfast_button);
        validateLunchButton = view.findViewById(R.id.validate_lunch_button);
        validateSnackButton = view.findViewById(R.id.validate_snack_button);
        validateDinnerButton = view.findViewById(R.id.validate_dinner_button);

        breakfastSection = view.findViewById(R.id.breakfast_section);
        lunchSection = view.findViewById(R.id.lunch_section);
        snackSection = view.findViewById(R.id.snack_section);
        dinnerSection = view.findViewById(R.id.dinner_section);

        validateBreakfastButton.setOnClickListener(v -> validateAndShowNext(breakfastSection, lunchSection));
        validateLunchButton.setOnClickListener(v -> validateAndShowNext(lunchSection, snackSection));
        validateSnackButton.setOnClickListener(v -> validateAndShowNext(snackSection, dinnerSection));
        validateDinnerButton.setOnClickListener(v -> validateAndShowNext(dinnerSection, null));

        Button addButton = view.findViewById(R.id.add);

        addButton.setOnClickListener(v -> addMeals());



    }

    private void goBackToPlanner() {
        Log.d("MealFormFragment", "Going back to PlannerFragment");
        if (getActivity() != null) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new PlannerFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void addMeals() {
        if (dataBase == null) {
            Toast.makeText(getContext(), "Database not initialized", Toast.LENGTH_LONG).show();
            return;
        }

        String week = weekInput.getText().toString().trim();
        String day = dayInput.getText().toString().trim();

        if (week.isEmpty() || day.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out week and day fields.", Toast.LENGTH_LONG).show();
            return;
        }

        createPlanWithMeals(
                breakfastNameInput, breakfastRecipeInput, breakfastCaloriesInput, breakfastTimeInput,
                lunchNameInput, lunchRecipeInput, lunchCaloriesInput, lunchTimeInput,
                snackNameInput, snackRecipeInput, snackCaloriesInput, snackTimeInput,
                dinnerNameInput, dinnerRecipeInput, dinnerCaloriesInput, dinnerTimeInput,
                week, day
        );

        clearInputFields();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new PlannerFragment()) // Use the container ID of your activity
                .addToBackStack(null) // Optionally add to the back stack so that the user can go back to the form
                .commit();


    }

    private void validateAndShowNext(LinearLayout currentSection, LinearLayout nextSection) {
        if (validateInputs(currentSection)) {
            currentSection.setVisibility(View.GONE);
            if (nextSection != null) {
                nextSection.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean validateInputs(LinearLayout section) {
        boolean isValid = true;

        for (int i = 0; i < section.getChildCount(); i++) {
            View child = section.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                if (editText.getText().toString().trim().isEmpty()) {
                    editText.setError("This field is required");
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private void createPlanWithMeals(EditText breakfastName, EditText breakfastRecipe, EditText breakfastCalories, EditText breakfastTime,
                                     EditText lunchName, EditText lunchRecipe, EditText lunchCalories, EditText lunchTime,
                                     EditText snackName, EditText snackRecipe, EditText snackCalories, EditText snackTime,
                                     EditText dinnerName, EditText dinnerRecipe, EditText dinnerCalories, EditText dinnerTime,
                                     String week, String day) {

        int breakfastId = insertMeal("Breakfast", breakfastName, breakfastRecipe, breakfastCalories, breakfastTime, week, day, false);
        int lunchId = insertMeal("Lunch", lunchName, lunchRecipe, lunchCalories, lunchTime, week, day,false);
        int snackId = insertMeal("Snack", snackName, snackRecipe, snackCalories, snackTime, week, day,false);
        int dinnerId = insertMeal("Dinner", dinnerName, dinnerRecipe, dinnerCalories, dinnerTime, week, day,false);
        boolean done =false;
        int totalCalories = getCalories(breakfastCalories) + getCalories(lunchCalories) +
                getCalories(snackCalories) + getCalories(dinnerCalories);

        String dishesSummary = "Breakfast: " + breakfastName.getText().toString().trim() +
                ", Lunch: " + lunchName.getText().toString().trim() +
                ", Snack: " + snackName.getText().toString().trim() +
                ", Dinner: " + dinnerName.getText().toString().trim();

        Plan plan = new Plan(day, breakfastId, dinnerId, dishesSummary, lunchId, snackId, totalCalories , done);

        new Thread(() -> {
            dataBase.planDAO().insertPlan(plan);
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Plan created successfully!", Toast.LENGTH_SHORT).show());
        }).start();
    }

    private int insertMeal(String mealType, EditText nameInput, EditText recipeInput, EditText caloriesInput, EditText timeInput, String week, String day , boolean done) {
        String name = nameInput.getText().toString().trim();
        String recipe = recipeInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        String caloriesString = caloriesInput.getText().toString().trim();
        if (name.isEmpty() || recipe.isEmpty() || time.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields for " + mealType, Toast.LENGTH_LONG).show();
            return -1;
        }

        int calories;
        try {
            calories = caloriesString.isEmpty() ? 0 : Integer.parseInt(caloriesString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number for calories in " + mealType, Toast.LENGTH_LONG).show();
            return -1;
        }

        Food newFood = new Food(week, day, recipe, calories, time, name, mealType ,done);
        return (int) dataBase.foodDAO().insertFood(newFood);
    }

    private int getCalories(EditText caloriesInput) {
        String caloriesString = caloriesInput.getText().toString().trim();
        try {
            return Integer.parseInt(caloriesString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void clearInputFields() {
        weekInput.setText("");
        dayInput.setText("");
        breakfastNameInput.setText("");
        breakfastRecipeInput.setText("");
        breakfastCaloriesInput.setText("");
        breakfastTimeInput.setText("");
        lunchNameInput.setText("");
        lunchRecipeInput.setText("");
        lunchCaloriesInput.setText("");
        lunchTimeInput.setText("");
        snackNameInput.setText("");
        snackRecipeInput.setText("");
        snackCaloriesInput.setText("");
        snackTimeInput.setText("");
        dinnerNameInput.setText("");
        dinnerRecipeInput.setText("");
        dinnerCaloriesInput.setText("");
        dinnerTimeInput.setText("");
    }
}
