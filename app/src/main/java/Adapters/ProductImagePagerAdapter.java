package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mobileproject.R;

import java.util.List;

public class ProductImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageUrls;

    public ProductImagePagerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product_image, container, false);

        ImageView imageView = view.findViewById(R.id.productImageView);

        // Get the image name from the imageUrls list
        String imageUrl = imageUrls.get(position);

        // Load the image, assuming you are using drawable resources
        int imageResource = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
        imageView.setImageResource(imageResource);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
