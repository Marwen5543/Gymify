package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private List<String[]> candidateList;
    private OnItemClickListener onItemClickListener;

    // Interface to pass the click event
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CandidateAdapter(List<String[]> candidateList, OnItemClickListener onItemClickListener) {
        this.candidateList = candidateList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] candidate = candidateList.get(position);

        // Set the candidate details
        holder.name.setText(candidate[0]);
        holder.level.setText("Level: " + candidate[1]);
        holder.objective.setText("Objectif: " + candidate[2]);

        // Set click listener for the button inside each item
        holder.btnDetails.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    // The ViewHolder class should extend RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, level, objective;
        ImageView btnDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.candidate_name);
            level = itemView.findViewById(R.id.candidate_level);
            objective = itemView.findViewById(R.id.candidate_objective);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }
    }
}
