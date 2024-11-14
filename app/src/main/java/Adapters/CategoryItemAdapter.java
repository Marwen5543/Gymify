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

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {

    private final List<String> categoryList;
    private final OnCategoryClickListener onCategoryClickListener;

    public CategoryItemAdapter(List<String> categoryList, OnCategoryClickListener onCategoryClickListener) {
        this.categoryList = categoryList;
        this.onCategoryClickListener = onCategoryClickListener;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the category name in the TextView
        String category = categoryList.get(position);
        holder.categoryTextView.setText(category);
        holder.itemView.setOnClickListener(v -> onCategoryClickListener.onCategoryClick(category));

        // Convert category name to image name by replacing spaces with underscores
        String imageName = category.toLowerCase().replace(" ", "_");

        // Dynamically get the image resource by its name
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
                imageName, "drawable", holder.itemView.getContext().getPackageName());

        // Set the image resource to the ImageView, fallback to a default image if not found
        if (imageResource != 0) {
            holder.categoryImageView.setImageResource(imageResource);
        } else {
            holder.categoryImageView.setImageResource(R.drawable.not_found); // Default image if resource not found
        }
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        ImageView categoryImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.cartTitle); // Change this to your TextView ID
            categoryImageView = itemView.findViewById(R.id.cartImage); // Finding the ImageView

        }
    }
}
