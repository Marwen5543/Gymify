package com.example.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.database.AppDataBaseS;
import com.example.mobileproject.entities.Category;
import com.example.mobileproject.entities.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddFormFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextPrice;
    private EditText editTextDescription;
    private Spinner categorySpinner;
    private ImageView imagePreview;
    private Button addButton, selectImageButton ;
    private SharedPreferences sharedPreferences ;
    public static final String MY_PREFERENCES = "MyPrefs" ;
    private AppDataBaseS dataBase;
    private Uri selectedImageUri;
    private List<String> imageUrls = new ArrayList<>();
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public AddFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_form, container, false);

        // Initialize UI elements
        editTextTitle = view.findViewById(R.id.title_text);
        editTextPrice = view.findViewById(R.id.price_text);
        editTextDescription = view.findViewById(R.id.description_text);
        categorySpinner = view.findViewById(R.id.spinner);
        addButton = view.findViewById(R.id.add_btn);
        selectImageButton = view.findViewById(R.id.select_image_btn);
        imagePreview = view.findViewById(R.id.image_preview);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        // Initialize Database
        dataBase = AppDataBaseS.getAppDataBaseS(getContext());

        // Initialize the list to store image URLs
        imageUrls = new ArrayList<>();

        // Populate the spinner with categories
        populateCategorySpinner();

        // Initialize the activity result launcher for image picking
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            // Handle multiple images selected
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                imageUrls.add(imageUri.toString()); // Add each URI to the list
                            }
                        } else if (data.getData() != null) {
                            // Handle single image selected
                            Uri imageUri = data.getData();
                            imageUrls.add(imageUri.toString()); // Add the URI to the list
                        }
                        updateImagePreview(); // Update your image preview method if needed
                    }
                }
        );

        // Set up click listeners
        addButton.setOnClickListener(v -> {
            // Handle the "Add Product" button click
            String title = editTextTitle.getText().toString();
            String priceValue = editTextPrice.getText().toString();
            double price = Double.parseDouble(priceValue);
            String description = editTextDescription.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();

            // Validate input fields
            if (title.isEmpty() || price <= 0 || description.isEmpty() || imageUrls.isEmpty()) {
                Toast.makeText(getContext(), "Please fill out all fields and select at least one image.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the product in the database
            Product newProduct = new Product(title, price, description, category, imageUrls);
            newProduct.setWish(Boolean.FALSE);
            dataBase.ProductDAO().addProduct(newProduct); // assuming you have a DAO and method for inserting

            // Optionally, store product data in SharedPreferences (for small temporary data)
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("last_added_title", title);
            editor.putFloat("last_added_price",(float) price);
            editor.putString("last_added_category", category);
            editor.apply(); // Save the data in SharedPreferences

            // Show a success message
            Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();

            // Reset form
            resetForm();
        });

        selectImageButton.setOnClickListener(v -> {
            // Open image picker to select images
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple images selection
            startActivityForResult(intent, 100); // 100 is the request code for image selection
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    // Handle multiple images selected
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        String imagePath = saveImageToInternalStorage(imageUri); // Save image and get file path
                        imageUrls.add(imagePath); // Add the file path to the list
                    }
                } else if (data.getData() != null) {
                    // Handle single image selected
                    Uri imageUri = data.getData();
                    String imagePath = saveImageToInternalStorage(imageUri); // Save image and get file path
                    imageUrls.add(imagePath); // Add the file path to the list
                }
                updateImagePreview(); // Update the image preview if needed
            }
        }
    }

    // Method to save the image to internal storage and return the file path
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            // Open an input stream from the URI
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            if (inputStream == null) return null;

            // Create a file in the internal storage
            File imageFile = new File(getContext().getFilesDir(), UUID.randomUUID().toString() + ".jpg");

            // Create an output stream to the file
            OutputStream outputStream = new FileOutputStream(imageFile);

            // Copy the content from the input stream to the output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams
            outputStream.close();
            inputStream.close();

            // Return the file path
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateImagePreview() {
        if (!imageUrls.isEmpty()) {
            String imagePath = imageUrls.get(0); // Get the first image for preview
            File imgFile = new File(imagePath);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imagePreview.setImageBitmap(myBitmap);
            }
        }
    }

    private void resetForm() {
        editTextTitle.setText("");
        editTextPrice.setText("");
        editTextDescription.setText("");
        categorySpinner.setSelection(0);
        imageUrls.clear(); // Clear the list of image URLs
        imagePreview.setImageResource(R.drawable.placeholder_image); // Set to a default placeholder
    }


    private void populateCategorySpinner() {
        // Get all enum values
        Category[] categories = Category.values();

        // Create an array of category names for the spinner
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].name(); // Use the name() method to get the enum name
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        // Set a listener to handle selection events
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected enum value
                Category selectedCategory = categories[position];
                // Do something with the selected category if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case when no selection is made
            }
        });
    }
}
