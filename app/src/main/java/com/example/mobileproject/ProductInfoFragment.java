package com.example.mobileproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.example.mobileproject.database.AppDataBase;
import com.example.mobileproject.entities.Product;
import Adapters.ProductImagePagerAdapter;

public class ProductInfoFragment extends Fragment {

    private Dialog popupDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);

        Product product = getArguments() != null ? getArguments().getParcelable("product") : null;
        if (product == null) {
            Log.e("ProductInfoFragment", "Product is null");
            return view; // Exit early if no product found
        }

        setupAddToCartButton(view);
        setupBackButton(view);
        setupViewPager(view, product);
        displayProductDetails(view, product);

        return view;
    }

    private void setupAddToCartButton(View view) {
        ImageView addToCartButton = view.findViewById(R.id.add_to_cart_btn2);
        addToCartButton.setOnClickListener(anchorView -> showAddToCartPopup(anchorView));
    }

    private void showAddToCartPopup(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View popupView = inflater.inflate(R.layout.add_to_cart_popup, null);
            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(anchorView.getRootView(), Gravity.CENTER, 0, 0);
        } else {
            Log.e("ProductInfoFragment", "LayoutInflater is null");
        }
    }

    private void setupBackButton(View view) {
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            MarketFragment marketFragment = new MarketFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.ProductInfoFragment, marketFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void setupViewPager(View view, Product product) {
        ViewPager productImagesViewPager = view.findViewById(R.id.productImagesViewPager);
        ProductImagePagerAdapter adapter = new ProductImagePagerAdapter(getContext(), product.getImageUrls());
        productImagesViewPager.setAdapter(adapter);
        setupDotsIndicator(view, productImagesViewPager);
    }

    private void displayProductDetails(View view, Product product) {
        TextView productTitle = view.findViewById(R.id.title);
        TextView productDescription = view.findViewById(R.id.description);
        TextView productPrice = view.findViewById(R.id.price);

        productTitle.setText(product.getTitle());
        productDescription.setText(product.getDescription());
        productPrice.setText(product.getPrice() + "TND "  );

    }

    private void setupDotsIndicator(View view, ViewPager viewPager) {
        LinearLayout dotsLayout = view.findViewById(R.id.dotsLayout);
        if (dotsLayout == null || viewPager.getAdapter() == null) {
            Log.e("ProductInfoFragment", "Dots layout or ViewPager adapter is null");
            return;
        }

        int dotCount = viewPager.getAdapter().getCount();
        ImageView[] dots = new ImageView[dotCount];
        dotsLayout.removeAllViews();

        int inactiveDotRes = R.drawable.dot_inactive;
        int activeDotRes = R.drawable.dot_active;

        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(inactiveDotRes);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        dots[0].setImageResource(activeDotRes);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotCount; i++) {
                    dots[i].setImageResource(inactiveDotRes);
                }
                dots[position].setImageResource(activeDotRes);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
