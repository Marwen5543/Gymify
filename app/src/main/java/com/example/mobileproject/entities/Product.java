package com.example.mobileproject.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "product")
public class Product implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Double price;
    private List<String> imageUrls;
    private Category category;
    private String description;
    //private Boolean inCart;
    private Boolean wish;
    private Boolean available;

    public Product(String title, Double price, List<String> imageUrls, Category category, String description,  Boolean wish) {
        this.title = title;
        this.price = price;
        this.imageUrls = imageUrls;
        this.category = category;
        this.description = description;
       // this.inCart = inCart;
        this.wish = wish;
    }



    protected Product(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        imageUrls = in.createStringArrayList();
        description = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(String title, Double price, String description, String category, List<String> imageUrls) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = Category.valueOf(category);
        this.imageUrls = imageUrls; // Initialize the list of image URLs
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Boolean getWish() {
        return wish;
    }

    public void setWish(Boolean wish) {
        this.wish = wish;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeDouble(price);
        parcel.writeStringList(imageUrls);
        parcel.writeString(description);
    }
}

