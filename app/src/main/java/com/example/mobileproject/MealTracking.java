package com.example.mobileproject;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import DAO.FoodDAO;
import DAO.PlanDAO;
import DataBase.AppDataBase;
import entities.Food;
import entities.PlanWithMeals;

public class MealTracking extends Fragment {

    private PlanDAO planDao;  // Declare PlanDAO
    private FoodDAO foodDao;  // Declare FoodDAO

    private TextView total_Calories ,planDateTextView;
    private TextView breakfastNameTextView, breakfastCaloriesTextView, breakfastTimeTextView;
    private TextView lunchNameTextView, lunchCaloriesTextView, lunchTimeTextView;
    private TextView snackNameTextView, snackCaloriesTextView, snackTimeTextView;
    private TextView dinnerNameTextView, dinnerCaloriesTextView, dinnerTimeTextView;
    private TextView totalCaloriesTextView;
    private ProgressBar progressBar;
    private Food breakfastFood, lunchFood, snackFood, dinnerFood;
    private ImageView backButton;
    private ImageButton breakfastDoneButton, lunchDoneButton, snackDoneButton, dinnerDoneButton;
    private boolean isBreakfastDone = false; // Set this based on your app's logic
    private boolean isLunchDone = false;
    private boolean isSnackDone = false;
    private boolean isDinnerDone = false;
    private int totalCalories = 0;

    public MealTracking() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int planId = getArguments().getInt("planId", -1);
            if (planId != -1) {
                // Initialize PlanDAO and FoodDAO before using them
                planDao = AppDataBase.getAppDataBase(getContext()).planDAO();  // Initialize planDao
                foodDao = AppDataBase.getAppDataBase(getContext()).foodDAO();  // Initialize foodDao

                if (planDao != null && foodDao != null) {
                    loadPlanWithMeals(planId);
                } else {
                    Log.e("MealTracking", "planDao or foodDao is null");
                }
            } else {
                Log.e("MealTracking", "No plan ID found in arguments");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_tracking, container, false);

        // Initialize TextViews and ProgressBar
        total_Calories = view.findViewById(R.id.totalCalories);

        planDateTextView = view.findViewById(R.id.Plan_date);
        breakfastNameTextView = view.findViewById(R.id.Breakfast_name);
        breakfastCaloriesTextView = view.findViewById(R.id.Breakfast_calories);
        lunchNameTextView = view.findViewById(R.id.Lunch_name);
        lunchCaloriesTextView = view.findViewById(R.id.Lunch_calories);
        snackNameTextView = view.findViewById(R.id.snack_name);
        snackCaloriesTextView = view.findViewById(R.id.snack_calories);
        dinnerNameTextView = view.findViewById(R.id.dinner_name);
        dinnerCaloriesTextView = view.findViewById(R.id.dinner_calories);
        totalCaloriesTextView = view.findViewById(R.id.total_calories);
        progressBar = view.findViewById(R.id.ProgressBar);

        view.findViewById(R.id.btn_more_breakfast).setOnClickListener(v -> openMealDetailsFragment(breakfastFood));
        view.findViewById(R.id.btn_more_Lunch).setOnClickListener(v -> openMealDetailsFragment(lunchFood));
        view.findViewById(R.id.btn_more_snack).setOnClickListener(v -> openMealDetailsFragment(snackFood));
        view.findViewById(R.id.btn_more_dinner).setOnClickListener(v -> openMealDetailsFragment(dinnerFood));

        breakfastDoneButton = view.findViewById(R.id.breakfast_done);
        lunchDoneButton = view.findViewById(R.id.Lunch_done);
        snackDoneButton = view.findViewById(R.id.snack_done);
        dinnerDoneButton = view.findViewById(R.id.dinner_done);

        // Set OnClickListeners for "Done" buttons
        breakfastDoneButton.setOnClickListener(v -> markMealAsDone(breakfastFood, "breakfast"));
        lunchDoneButton.setOnClickListener(v -> markMealAsDone(lunchFood, "lunch"));
        snackDoneButton.setOnClickListener(v -> markMealAsDone(snackFood, "snack"));
        dinnerDoneButton.setOnClickListener(v -> markMealAsDone(dinnerFood, "dinner"));

        backButton = view.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> goBackToYourPlans());
        } else {
            Log.e("MealFormFragment", "Back Button not found.");
        }
        updateButtonColors();

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of meals and total calories
        outState.putBoolean("isBreakfastDone", isBreakfastDone);
        outState.putBoolean("isLunchDone", isLunchDone);
        outState.putBoolean("isSnackDone", isSnackDone);
        outState.putBoolean("isDinnerDone", isDinnerDone);
// Extract the value from the TextView as a string and convert it to an integer
        String totalCaloriesText = total_Calories.getText().toString();
        int totalCalories = 0;

// Check if the text is not empty and then parse the integer
        if (!totalCaloriesText.isEmpty()) {
            try {
                totalCalories = Integer.parseInt(totalCaloriesText);
            } catch (NumberFormatException e) {
                // Handle the case where the text is not a valid integer (optional)
                Log.e("MealTracking", "Invalid calorie value: " + totalCaloriesText);
            }
        }

// Now put the integer value into the outState
        outState.putInt("totalCalories", totalCalories);
    }

    private void updateButtonColors() {
        if (isBreakfastDone) {
            breakfastDoneButton.setColorFilter(Color.parseColor("#5DD62C")); // Set to green
        }
        if (isLunchDone) {
            lunchDoneButton.setColorFilter(Color.parseColor("#5DD62C"));
        }
        if (isSnackDone) {
            snackDoneButton.setColorFilter(Color.parseColor("#5DD62C"));
        }
        if (isDinnerDone) {
            dinnerDoneButton.setColorFilter(Color.parseColor("#5DD62C"));
        }
    }

    private void goBackToYourPlans() {
        Log.d("MealFormFragment", "Going back to PlannerFragment");
        if (getActivity() != null) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new CandidatPlans())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void openMealDetailsFragment(Food food) {
        if (food != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("food", food);

            MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
            mealDetailsFragment.setArguments(bundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, mealDetailsFragment) // Replace with actual container ID
                    .addToBackStack(null)
                    .commit();
        } else {
            Log.e("MealTracking", "Food object is null. Cannot open details.");
        }
    }

    private void loadPlanWithMeals(int planId) {
        if (planDao != null && foodDao != null) {
            new Thread(() -> {
                PlanWithMeals planWithMeals = planDao.getPlanWithMealsById(planId);
                if (planWithMeals != null) {
                    breakfastFood = planWithMeals.breakfastFood;
                    lunchFood = planWithMeals.lunchFood;
                    snackFood = planWithMeals.snackFood;
                    dinnerFood = planWithMeals.dinnerFood;

                    requireActivity().runOnUiThread(() -> displayPlanDetails(planWithMeals));
                } else {
                    Log.e("MealTracking", "No plan found for ID: " + planId);
                }
            }).start();
        } else {
            Log.e("MealTracking", "planDao or foodDao is null in loadPlanWithMeals");
        }
    }

    private void displayPlanDetails(PlanWithMeals planWithMeals) {
        planDateTextView.setText("DAY : " + planWithMeals.plan.getDay());

        // Display Breakfast details
        if (planWithMeals.breakfastFood != null) {
            breakfastNameTextView.setText(planWithMeals.breakfastFood.getName());
            breakfastCaloriesTextView.setText(String.valueOf(planWithMeals.breakfastFood.getCalories()) + " kcal");
        } else {
            breakfastNameTextView.setText("No food planned");
            breakfastCaloriesTextView.setText("");
        }

        // Display Lunch details
        if (planWithMeals.lunchFood != null) {
            lunchNameTextView.setText(planWithMeals.lunchFood.getName());
            lunchCaloriesTextView.setText(String.valueOf(planWithMeals.lunchFood.getCalories()) + " kcal");
        } else {
            lunchNameTextView.setText("No food planned");
            lunchCaloriesTextView.setText("");
        }

        // Display Snack details
        if (planWithMeals.snackFood != null) {
            snackNameTextView.setText(planWithMeals.snackFood.getName());
            snackCaloriesTextView.setText(String.valueOf(planWithMeals.snackFood.getCalories()) + " kcal");
        } else {
            snackNameTextView.setText("No food planned");
            snackCaloriesTextView.setText("");
        }

        // Display Dinner details
        if (planWithMeals.dinnerFood != null) {
            dinnerNameTextView.setText(planWithMeals.dinnerFood.getName());
            dinnerCaloriesTextView.setText(String.valueOf(planWithMeals.dinnerFood.getCalories()) + " kcal");
        } else {
            dinnerNameTextView.setText("No food planned");
            dinnerCaloriesTextView.setText("");
        }


        // Display total calories
        int totalCalories = (breakfastFood != null ? breakfastFood.getCalories() : 0) +
                (lunchFood != null ? lunchFood.getCalories() : 0) +
                (snackFood != null ? snackFood.getCalories() : 0) +
                (dinnerFood != null ? dinnerFood.getCalories() : 0);

        totalCaloriesTextView.setText(totalCalories + " kcal");
        calculateTotalCalories();
    }

    private int calculateTotalCalories() {
        int totalCalories = 0;

        if (isBreakfastDone && breakfastFood != null) {
            totalCalories += breakfastFood.getCalories();
        }
        if (isLunchDone && lunchFood != null) {
            totalCalories += lunchFood.getCalories();
        }
        if (isSnackDone && snackFood != null) {
            totalCalories += snackFood.getCalories();
        }
        if (isDinnerDone && dinnerFood != null) {
            totalCalories += dinnerFood.getCalories();
        }

        // Optionally, you can show a message if all meals are done
        if (isBreakfastDone && isLunchDone && isSnackDone && isDinnerDone) {
            Log.d("MealTracking", "All meals done! Total Calories: " + totalCalories + " kcal");
        }

        return totalCalories;  // Return the total calories instead of just updating a view directly
    }


    private void markMealAsDone(Food food, String mealType) {
        if (food != null) {
            switch (mealType) {
                case "breakfast":
                    isBreakfastDone = true;
                    break;
                case "lunch":
                    isLunchDone = true;
                    break;
                case "snack":
                    isSnackDone = true;
                    break;
                case "dinner":
                    isDinnerDone = true;
                    break;
            }
            updateButtonColors();
            int totalCalories = calculateTotalCalories(); // Recalculate total calories after marking meal as done
            total_Calories.setText(totalCalories + " kcal"); // Update the TextView with new total calories
        } else {
            Log.e("MealTracking", mealType + " food is null. Cannot mark as done.");
        }
    }

}
