package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;

import java.util.List;

import DAO.PlanDAO;
import entities.Plan;
import entities.PlanWithMeals;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.PlanViewHolder> {

    private List<PlanWithMeals> plans;
    private PlanDAO planDAO;  // PlanDAO reference

    public MealPlanAdapter(List<PlanWithMeals> plans, PlanDAO planDAO) {
        this.plans = plans;
        this.planDAO = planDAO; // Initialize here
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_day_section, parent, false);
        return new PlanViewHolder(view); // Pass context and list to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
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

        // Pass the plan and the DAO to the button click listener
        holder.btnMore.setOnClickListener(view -> {
            holder.showCustomDialog(view, plan);  // Pass the Plan object to the dialog
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

        void showCustomDialog(View view, Plan planToDelete) {
            // Create an AlertDialog.Builder
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());

            // Inflate the dialog layout
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_options, null);
            builder.setView(dialogView);

            // Find the buttons in the dialog layout
            Button cancelButton = dialogView.findViewById(R.id.option_cancel);
            Button deleteButton = dialogView.findViewById(R.id.option_delete);
            android.app.AlertDialog dialog = builder.create();

            cancelButton.setOnClickListener(v -> {
                // Close the dialog when "Cancel" is clicked
                dialog.dismiss();
            });

            deleteButton.setOnClickListener(v -> {
                // Get the position of the plan in the list
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Pass the plan and its position to the delete method
                    deletePlanFromDatabase(planToDelete, position);
                    Toast.makeText(view.getContext(), "Plan deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            // Show the dialog
            dialog.show();
        }

        private void deletePlanFromDatabase(Plan planToDelete, int position) {
            // Ensure planDAO is initialized before attempting deletion
            new Thread(() -> {
                // Delete the plan from the database
                planDAO.deletePlan(planToDelete);

                // Run on the UI thread to update the RecyclerView
                itemView.post(() -> {
                    // Remove the deleted plan from the list
                    plans.remove(position);

                    // Notify the adapter that the item has been removed
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, plans.size());  // This ensures the other items are adjusted
                });
            }).start();
        }

    }
}
