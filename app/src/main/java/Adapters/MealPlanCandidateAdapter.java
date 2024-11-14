package Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.CandidatPlans;
import com.example.mobileproject.MealTracking;
import com.example.mobileproject.R;

import java.util.List;

import DAO.PlanDAO;
import entities.Plan;
import entities.PlanWithMeals;

public class MealPlanCandidateAdapter extends RecyclerView.Adapter<MealPlanCandidateAdapter.PlanViewHolder>{

    private List<PlanWithMeals> plans;
    private PlanDAO planDAO;  // PlanDAO reference
    private FragmentManager fragmentManager;  // Declare fragmentManager


    public MealPlanCandidateAdapter(List<PlanWithMeals> plans, PlanDAO planDAO) {
        this.plans = plans;
        this.planDAO = planDAO; // Initialize here

    }

    @NonNull
    @Override
    public MealPlanCandidateAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_day_section_candidate, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanCandidateAdapter.PlanViewHolder holder, int position) {
        PlanWithMeals planWithMeals = plans.get(position);
        Plan plan = planWithMeals.plan;

        // Set the plan data to the views
        holder.tvDay.setText(plan.getDay());
        holder.tvCalories.setText(planWithMeals.calculateTotalCalories() + " kcal");

        // Set breakfast details
        if (planWithMeals.breakfastFood != null) {
            holder.breakfastTitle.setText("Breakfast");
            holder.breakfastPlan.setText("• " + planWithMeals.breakfastFood.getName()); // Assuming getName() returns the dish name
        } else {
            holder.breakfastTitle.setText("Breakfast");
            holder.breakfastPlan.setText("• No food planned");
        }

        // Set lunch details
        if (planWithMeals.lunchFood != null) {
            holder.lunchTitle.setText("Lunch");
            holder.lunchPlan.setText("• " + planWithMeals.lunchFood.getName());
        } else {
            holder.lunchTitle.setText("Lunch");
            holder.lunchPlan.setText("• No food planned");
        }

        // Set snack details
        if (planWithMeals.snackFood != null) {
            holder.snackTitle.setText("Snack");
            holder.snackPlan.setText("• " + planWithMeals.snackFood.getName());
        } else {
            holder.snackTitle.setText("Snack");
            holder.snackPlan.setText("• No food planned");
        }

        // Set dinner details
        if (planWithMeals.dinnerFood != null) {
            holder.dinnerTitle.setText("Dinner");
            holder.dinnerPlan.setText("• " + planWithMeals.dinnerFood.getName());
        } else {
            holder.dinnerTitle.setText("Dinner");
            holder.dinnerPlan.setText("• No food planned");
        }

        // Set click listener for "More" button to navigate

        holder.btnMore.setOnClickListener(view -> {
            // Create a Bundle to pass data (planId) to the next fragment
            Bundle bundle = new Bundle();
            bundle.putInt("planId", planWithMeals.plan.getId());

            // Create an instance of MealTracking fragment
            MealTracking mealTrackingFragment = new MealTracking();
            mealTrackingFragment.setArguments(bundle);

            // Perform fragment transaction using the passed FragmentManager
            FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, mealTrackingFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvCalories, breakfastTitle, breakfastPlan;
        TextView lunchTitle, lunchPlan, snackTitle, snackPlan;
        TextView dinnerTitle, dinnerPlan;
        ImageButton btnMore;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvCalories = itemView.findViewById(R.id.calories);
            breakfastTitle = itemView.findViewById(R.id.breakfast_title);
            breakfastPlan = itemView.findViewById(R.id.breakfast_plan);
            lunchTitle = itemView.findViewById(R.id.lunch_title);
            lunchPlan = itemView.findViewById(R.id.lunch_plan);
            snackTitle = itemView.findViewById(R.id.snack_title);
            snackPlan = itemView.findViewById(R.id.snack_plan);
            dinnerTitle = itemView.findViewById(R.id.dinner_title);
            dinnerPlan = itemView.findViewById(R.id.dinner_plan);
            btnMore = itemView.findViewById(R.id.btn_more);
        }



    }
}
