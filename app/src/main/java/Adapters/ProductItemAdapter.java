package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.ProductInfoFragment;
import com.example.mobileproject.R;
import com.example.mobileproject.database.AppDataBaseS;
import com.example.mobileproject.entities.CartItem;
import com.example.mobileproject.entities.Product;

import java.util.List;
import android.os.Bundle;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    private List<Product> productList;
    private AppDataBaseS dataBase;
    private Context context;

    public ProductItemAdapter( List<Product> productList, Context context) {
        this.context = context;
        this.productList = productList;
        this.dataBase = AppDataBaseS.getAppDataBaseS(context);
    }
    // Update the product list
    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product != null) {
        holder.titleTextView.setText(product.getTitle());
        holder.priceTextView.setText(String.format("%.2f", product.getPrice(),"TND"));

            String imageUrl = product.getImageUrls().get(0); // Assuming you want the first image in the list
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)  // Use the product image URL
                        .placeholder(R.drawable.not_found)  // Placeholder image while loading
                        .error(R.drawable.not_found)  // Image to show in case of an error
                        .into(holder.imageView);  // Set the image into the ImageView
            } else {
                holder.imageView.setImageResource(R.drawable.not_found);  // Fallback image
            }
        }


        // Assuming you have an ImageView or something to click on
        holder.itemView.setOnClickListener(v -> {
            // Create a bundle to pass the Product object
            Bundle bundle = new Bundle();
            bundle.putParcelable("product", product);

            // Create an instance of ProductInfoFragment
            ProductInfoFragment productInfoFragment = new ProductInfoFragment();
            productInfoFragment.setArguments(bundle);

            // Assuming you're in a Fragment and using FragmentManager
            FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.MarketFragment, productInfoFragment)
                    .addToBackStack(null)
                    .commit();
        });


        //wishllist
        if (product.getWish() != null && product.getWish()) {
            holder.likebutton.setImageResource(R.drawable.liked);
        } else {
            holder.likebutton.setImageResource(R.drawable.disliked);
        }

        holder.likebutton.setOnClickListener(v -> {
            // Toggle the wish state
            boolean newWishState = !product.getWish();
            product.setWish(newWishState);


            if (newWishState) {
                holder.likebutton.setImageResource(R.drawable.liked); // Change image to liked
                product.setWish(newWishState);
            } else {
                holder.likebutton.setImageResource(R.drawable.disliked);
                product.setWish(false);
            }

            // Save the wish state in the database
            new Thread(() -> {
                dataBase.ProductDAO().updateProduct(product); // Assuming you have an update method in your DAO
            }).start();
        });


        // Check if product is in the cart
        new Thread(() -> {

            boolean isInCart = dataBase.CartItemDAO().isProductInCart(product.getId());
            holder.itemView.post(() -> {
                holder.cartbutton.setImageResource(isInCart ? R.drawable.cart_popup : R.drawable.cart);
            });
        }).start();

        // Toggle cart state on button click
        holder.cartbutton.setOnClickListener(v -> {
            new Thread(() -> {
                boolean isInCart = dataBase.CartItemDAO().isProductInCart(product.getId());

                if (isInCart) {
                    // Remove from cart
                    dataBase.CartItemDAO().removeProductFromCart(product.getId());
                    holder.itemView.post(() -> holder.cartbutton.setImageResource(R.drawable.cart));
                } else {
                    // Add to cart
                    dataBase.CartItemDAO().addCartItem(new CartItem(345, product.getId())); // Replace 345 with dynamic userId if needed
                    holder.itemView.post(() -> holder.cartbutton.setImageResource(R.drawable.cart_popup));
                }
            }).start();
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView priceTextView;
         ImageView imageView; // Define an ImageView if you want to show product images
        ImageView likebutton;
        ImageView cartbutton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.product_title); // Ensure this matches your product_item layout
            priceTextView = itemView.findViewById(R.id.product_price); // Ensure this matches your product_item layout
             imageView = itemView.findViewById(R.id.product_image); // Ensure this matches your product_item layout
            likebutton = itemView.findViewById(R.id.likebtn);
            cartbutton = itemView.findViewById(R.id.cartbtn);
        }
    }
}

