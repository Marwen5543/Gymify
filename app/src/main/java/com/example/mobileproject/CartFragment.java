package com.example.mobileproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobileproject.DAO.ProductDAO;
import com.example.mobileproject.database.AppDataBaseS;
import com.example.mobileproject.entities.CartItem;
import com.example.mobileproject.entities.Product;

import java.util.List;

import Adapters.CartAdapter;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartProducts;
    private ProductDAO productDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Fetch products in the cart
        cartProducts = getInCartProducts(); // Retrieve products
        TextView totalPriceTextView = view.findViewById(R.id.cart_total); // Replace with actual ID
        adapter = new CartAdapter(getContext(), cartProducts, productDao, totalPriceTextView);

        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Recalculate the total price whenever the fragment is resumed
        if (adapter != null) {
            adapter.calculateTotalPrice();
        }
    }

    private List<CartItem> getInCartProducts() {
        return AppDataBaseS.getAppDataBaseS(getContext()).CartItemDAO().getAllCartItems();
    }
}