package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.entities.Exercise;


import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;
    private Context context;
    private AdapterView.OnItemClickListener listener;
    private int[] imageList;
    private FragmentManager fragmentManager;

    public ExerciseAdapter(Context context, List<Exercise> exercises ,  int[] images , FragmentManager fragmentManager) {
        this.context = context;
        this.exerciseList = exercises;
        this.imageList = images;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseName.setText(exercise.getName());

        // Assigner l'image correspondante à la carte de l'exercice
        if (position < imageList.length) {
            holder.exerciseImage.setImageResource(imageList[position]); // Set image based on position
        }

        // Lorsqu'on clique sur la carte
        holder.exerciseCard.setOnClickListener(v -> {

            Fragment fragment = new UpdateExerciseFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", exercise.getId());
            fragment.setArguments(bundle);

            // Use the passed fragment manager
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView exerciseImage;
        TextView exerciseName;
        CardView exerciseCard;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseCard = itemView.findViewById(R.id.exerciseCard);
        }
    }
}