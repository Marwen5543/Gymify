package com.example.mobileproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mobileproject.database.AppDataBaseS;
import com.example.mobileproject.entities.Category;
import com.example.mobileproject.entities.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.CategoryItemAdapter;
import Adapters.ProductItemAdapter;

public class MarketFragment extends Fragment {

    private RecyclerView categoryrecyclerView;
    private RecyclerView productrecyclerView;
    private CategoryItemAdapter CategoryAdapter;
    private ProductItemAdapter ProductAdapter;
    private List<String> CategoryItemList;
    private List<Product> productList = new ArrayList<>();
    private AppDataBaseS dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No setup for RecyclerView here
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        // Initialize the RecyclerView for categories
        categoryrecyclerView = view.findViewById(R.id.categoriesrecyclerView);
        List<String> categoryItemList = new ArrayList<>();
        for (Category category : Category.values()) {
            categoryItemList.add(category.name()); // Add each enum value as a string
        }

        // Set up the RecyclerView for categories
        CategoryItemAdapter categoryAdapter = new CategoryItemAdapter(categoryItemList, categoryName -> {

        });

        categoryrecyclerView.setAdapter(categoryAdapter);
        categoryrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize the RecyclerView for products
        productrecyclerView = view.findViewById(R.id.productsrecyclerView);
        List<Product> productList = new ArrayList<>();

        ProductItemAdapter productAdapter = new ProductItemAdapter(productList, getContext());
        productrecyclerView.setAdapter(productAdapter);

        // Set LayoutManager
        int numberOfColumns = 2; // Change this number based on how many items you want per row
        productrecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        // Retrieve products from the database
        fetchProductsFromDatabase(productList, productAdapter);

        // Navigate to add form
        ImageView addButton = view.findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View anchorView) {
                // Create a new instance of AddFormFragment
                AddFormFragment addFormFragment = new AddFormFragment();
                // Replace the current fragment with AddFormFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, addFormFragment); // Use your actual container ID
                transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
                transaction.commit();
            }
        });

        return view;
    }


    private void fetchProductsFromDatabase(List<Product> productList, ProductItemAdapter productAdapter) {
        new Thread(() -> {
            dataBase = AppDataBaseS.getAppDataBaseS(getContext());

            // Fetch products from DB using your DAO
            List<Product> products = dataBase.ProductDAO().getAllProducts(); // Replace this with your actual DAO method

            // Update UI on the main thread
            getActivity().runOnUiThread(() -> {
                productList.clear(); // Clear the existing list
                productList.addAll(products); // Add fetched products
                productAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
            });
        }).start();
    }



}