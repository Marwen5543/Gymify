package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.DAO.ProductDAO;
import com.example.mobileproject.R;
import com.example.mobileproject.database.AppDataBaseS;
import com.example.mobileproject.entities.CartItem;
import com.example.mobileproject.entities.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private ProductDAO productDao;
    private AppDataBaseS dataBase;
    private TextView totalPriceTextView;

    public CartAdapter(Context context, List<CartItem> cartItemList, ProductDAO productDao, TextView totalPriceTextView) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.productDao = productDao;
        this.dataBase = AppDataBaseS.getAppDataBaseS(context);
        this.totalPriceTextView = totalPriceTextView;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        int productId = cartItem.getProductId();

        // Fetch product details from database and update UI
        Product product = dataBase.ProductDAO().getOne(productId);
        if (product != null) {
            holder.productTitle.setText(product.getTitle());
            holder.productPrice.setText(String.format("%.2f", product.getPrice()));
            String imageUrl = product.getImageUrls().get(0); // Assuming you want the first image in the list
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)  // Use the product image URL
                        .placeholder(R.drawable.not_found)  // Placeholder image while loading
                        .error(R.drawable.not_found)  // Image to show in case of an error
                        .into(holder.productImage);  // Set the image into the ImageView
            } else {
                holder.productImage.setImageResource(R.drawable.not_found);  // Fallback image
            }
        }

        // Handle delete click
        holder.deleteButton.setOnClickListener(v -> removeProductFromCart(holder, cartItem, productId));
    }

    private void removeProductFromCart(CartViewHolder holder, CartItem cartItem, int productId) {
        new Thread(() -> {
            dataBase.CartItemDAO().removeProductFromCart(productId);
            cartItemList.remove(cartItem);

            holder.deleteButton.post(() -> {
                calculateTotalPrice(); // Update total price after removal
                notifyDataSetChanged(); // Refresh the RecyclerView
            });
        }).start();
    }

    // Calculate total price of items in the cart
    public void calculateTotalPrice() {
        new Thread(() -> {
            double totalPrice = 0.0;
            for (CartItem cartItem : cartItemList) {
                Product product = dataBase.ProductDAO().getOne(cartItem.getProductId());
                if (product != null) {
                    totalPrice += product.getPrice();
                }
            }

            // Create a final variable to avoid the lambda expression error
            final double finalTotalPrice = totalPrice;

            // Update UI on the main thread
            totalPriceTextView.post(() ->
                    totalPriceTextView.setText(String.format("%.2f", finalTotalPrice, "TND"))
            );
        }).start();
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle;
        TextView productPrice;
        ImageView productImage;
        ImageView deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.cartTitle);
            productPrice = itemView.findViewById(R.id.cartPrice);
            productImage = itemView.findViewById(R.id.cartImage);
            deleteButton = itemView.findViewById(R.id.delete_cart_item);
        }
    }
}
